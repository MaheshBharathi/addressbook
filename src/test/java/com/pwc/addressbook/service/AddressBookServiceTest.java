package com.pwc.addressbook.service;

import com.pwc.addressbook.entity.ContactEntity;
import com.pwc.addressbook.exception.ContactExistException;
import com.pwc.addressbook.exception.ContactNotFoundException;
import com.pwc.addressbook.model.Contact;
import com.pwc.addressbook.repository.AddressBookRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;

@ExtendWith(MockitoExtension.class)
class AddressBookServiceTest {

    @Mock
    AddressBookRepository repository;

    @InjectMocks
    AddressBookService service;

    @Test
    public void shouldAddContact() {
        final Contact contact = new Contact("contact1", "0470000000");
        given(repository.getContactEntity(eq("contact1"))).willReturn(Optional.empty());

        service.addContact(contact);

        verify(repository, times(1)).getContactEntity(eq("contact1"));
        verify(repository, times(1)).addContact(any(Contact.class));
    }

    @Test
    public void addContactShouldThrowContactExistExceptionWhenContactExistInDatabase() {
        final Contact contact = new Contact("contact1", "0470000000");
        given(repository.getContactEntity(eq("contact1"))).willReturn(Optional.of(new ContactEntity()));

        assertThrows(ContactExistException.class, () -> service.addContact(contact));
        verify(repository, times(1)).getContactEntity(eq("contact1"));
        verify(repository, times(0)).addContact(any(Contact.class));
    }

    @Test
    public void shouldGetAllContacts() {
        final ContactEntity entity1 = new ContactEntity();
        entity1.setName("contact1");
        entity1.setPhoneNo("0470000000");
        final ContactEntity entity2 = new ContactEntity();
        entity2.setName("contact2");
        entity2.setPhoneNo("0480000000");

        given(repository.getAllContacts(eq("asc"))).willReturn(asList(entity1, entity2));

        final List<Contact> contacts = service.getAllContacts("asc");

        assertEquals(2, contacts.size());
        assertEquals("contact1", contacts.get(0).getName());
        assertEquals("0470000000", contacts.get(0).getPhoneNo());
        assertEquals("contact2", contacts.get(1).getName());
        assertEquals("0480000000", contacts.get(1).getPhoneNo());
        verify(repository, times(1)).getAllContacts(eq("asc"));
    }

    @Test
    public void shouldGetContactByContactName() {
        final ContactEntity entity = new ContactEntity();
        entity.setName("contact1");
        entity.setPhoneNo("0470000000");

        given(repository.getContactEntity(eq("contact1"))).willReturn(Optional.of(entity));

        final Contact contact = service.getContact("contact1");

        assertEquals("contact1", contact.getName());
        assertEquals("0470000000", contact.getPhoneNo());
        verify(repository, times(1)).getContactEntity(eq("contact1"));
    }

    @Test
    public void getContactShouldThrowNotFoundExceptionWhenContactNameNotAvailableInAddressBook() {
        given(repository.getContactEntity(eq("contact1"))).willReturn(Optional.empty());

        assertThrows(ContactNotFoundException.class, () -> service.getContact("contact1"));

        verify(repository, times(1)).getContactEntity(eq("contact1"));
    }

    @Test
    public void shouldGetUniqueContacts() {
        given(repository.getAllContactNames()).willReturn(asList("contact 5", "Contact 25", "contact 1", "Contact12", "Contact 15", "contact 3", "Contact99"));

        final Set<String> result = service.getUniqueContacts(asList("contact 1", "contact 7", "contact 3", "contact21"));

        assertThat(result, hasSize(7));
        assertThat(result, containsInAnyOrder("contact 5", "Contact 25", "Contact12", "Contact 15", "Contact99",  "contact 7", "contact21"));
        verify(repository, times(1)).getAllContactNames();
    }
}