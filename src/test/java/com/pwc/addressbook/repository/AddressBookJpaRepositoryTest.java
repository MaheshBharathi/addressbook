package com.pwc.addressbook.repository;

import com.pwc.addressbook.entity.ContactEntity;
import com.pwc.addressbook.model.Contact;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddressBookJpaRepositoryTest {

    @Mock
    EntityManager entityManager;

    @InjectMocks
    AddressBookJpaRepository repository;

    @Test
    public void shouldPersistNewContact() {
        final Contact contact = new Contact("contact1", "0470000000");
        final ArgumentCaptor<ContactEntity> contactEntityArgumentCaptor = ArgumentCaptor.forClass(ContactEntity.class);
        doNothing().when(entityManager).persist(contactEntityArgumentCaptor.capture());

        repository.addContact(contact);
        final ContactEntity entity = contactEntityArgumentCaptor.getValue();
        assertEquals("contact1", entity.getName());
        assertEquals("0470000000", entity.getPhoneNo());
    }

    @Test
    public void shouldGetAllContactNames() {
        final TypedQuery query = mock(TypedQuery.class);
        given(entityManager.createQuery(anyString(), eq(String.class))).willReturn(query);

        repository.getAllContactNames();

        verify(query, times(1)).getResultList();
        verify(entityManager, times(1)).createQuery(eq("SELECT c.name FROM ContactEntity AS c"), eq(String.class));
    }

    @Test
    public void shouldGetContactEntityByNames() {
        final ContactEntity contactEntity = new ContactEntity();
        contactEntity.setPhoneNo("0470000000");
        contactEntity.setName("contact1");

        for(final List<ContactEntity> entities : asList(null, new ArrayList<ContactEntity>(), asList(contactEntity))) {

            final CriteriaBuilder criteriaBuilder = mock(CriteriaBuilder.class);
            final CriteriaQuery criteriaQuery = mock(CriteriaQuery.class);
            final Root entity = mock(Root.class);
            final TypedQuery typedQuery = mock(TypedQuery.class);

            given(entityManager.getCriteriaBuilder()).willReturn(criteriaBuilder);
            given(criteriaBuilder.createQuery(eq(ContactEntity.class))).willReturn(criteriaQuery);
            given(criteriaQuery.from(eq(ContactEntity.class))).willReturn(entity);

            given(entityManager.createQuery(any(CriteriaQuery.class))).willReturn(typedQuery);
            given(typedQuery.getResultList()).willReturn(entities);

            final Optional<ContactEntity> result = repository.getContactEntity("contact1");

            if(entities == null || entities.size() ==0) {
                assertFalse(result.isPresent());
            } else {
                assertTrue(result.isPresent());
                assertEquals("contact1", result.get().getName());
                assertEquals("0470000000", result.get().getPhoneNo());
            }

            reset(criteriaBuilder, criteriaQuery, entity, typedQuery, entityManager);
        }

    }

    @Test
    public void shouldGetAllContactEntities() {

        for(final String sortOrder : asList("asc", "desc")) {
            final ContactEntity contactEntity = new ContactEntity();
            contactEntity.setPhoneNo("0470000000");
            contactEntity.setName("contact1");

            final CriteriaBuilder criteriaBuilder = mock(CriteriaBuilder.class);
            final CriteriaQuery criteriaQuery = mock(CriteriaQuery.class);
            final Root root = mock(Root.class);
            final TypedQuery typedQuery = mock(TypedQuery.class);
            final CriteriaQuery select = mock(CriteriaQuery.class);
            final Path path = mock(Path.class);

            given(entityManager.getCriteriaBuilder()).willReturn(criteriaBuilder);
            given(criteriaBuilder.createQuery(eq(ContactEntity.class))).willReturn(criteriaQuery);
            given(criteriaQuery.from(eq(ContactEntity.class))).willReturn(root);
            given(criteriaQuery.select(any(Selection.class))).willReturn(select);

            given(entityManager.createQuery(any(CriteriaQuery.class))).willReturn(typedQuery);
            given(typedQuery.getResultList()).willReturn(asList(contactEntity));

            given(root.get(anyString())).willReturn(path);

            final List<ContactEntity> result = repository.getAllContacts(sortOrder);

            if(sortOrder.equals("desc")) {
                verify(criteriaBuilder, times(1)).desc(any(Expression.class));
            } else {
                verify(criteriaBuilder, times(1)).asc(any(Expression.class));
            }

            assertEquals(1, result.size());
            assertEquals("contact1", result.get(0).getName());
            assertEquals("0470000000", result.get(0).getPhoneNo());

            reset(criteriaBuilder, criteriaQuery, root, typedQuery, select, entityManager);
        }
    }
}