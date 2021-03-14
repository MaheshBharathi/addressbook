package com.pwc.addressbook.repository;

import com.pwc.addressbook.entity.ContactEntity;
import com.pwc.addressbook.model.Contact;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
public class AddressBookJpaRepository implements AddressBookRepository {

    private static final String NAME = "name";
    private static final String GET_ALL_CONTACTS_QUERY = "SELECT c.name FROM ContactEntity AS c";
    private static final String DESC = "desc";

    @Autowired
    private EntityManager entityManager;

    @Override
    @Transactional
    public void addContact(final Contact contact) {
        log.debug("creating new contact {}:{}", contact.getName(), contact.getPhoneNo());
        final ContactEntity contactEntity = new ContactEntity();
        contactEntity.setName(contact.getName());
        contactEntity.setPhoneNo(contact.getPhoneNo());
        entityManager.persist(contactEntity);
    }

    @Override
    public List<ContactEntity> getAllContacts(final String sortOrder) {
        final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<ContactEntity> criteriaQuery = criteriaBuilder.createQuery(ContactEntity.class);
        final Root<ContactEntity> from = criteriaQuery.from(ContactEntity.class);
        final CriteriaQuery<ContactEntity> select = criteriaQuery.select(from);

        if(DESC.equalsIgnoreCase(sortOrder)) {
            criteriaQuery.orderBy(criteriaBuilder.desc(from.get(NAME)));
        } else {
            criteriaQuery.orderBy(criteriaBuilder.asc(from.get(NAME)));
        }

        return entityManager.createQuery(select).getResultList();
    }

    @Override
    public List<String> getAllContactNames() {
        final TypedQuery<String> query = entityManager.createQuery(
                GET_ALL_CONTACTS_QUERY, String.class);
        return query.getResultList();
    }

    @Override
    public Optional<ContactEntity> getContactEntity(final String name) {
        final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<ContactEntity> query = criteriaBuilder.createQuery(ContactEntity.class);
        final Root<ContactEntity> contactEntity = query.from(ContactEntity.class);
        query.where(criteriaBuilder.equal(criteriaBuilder.upper(contactEntity.get(NAME)), name.toUpperCase()));

        final List<ContactEntity> result = entityManager.createQuery(query).getResultList();

        final ContactEntity entity = result == null || result.size() == 0 ? null : result.get(0);

        return Optional.ofNullable(entity);
    }
}
