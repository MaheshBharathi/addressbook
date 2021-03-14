package com.pwc.addressbook.service;

import com.pwc.addressbook.entity.ContactEntity;
import com.pwc.addressbook.exception.ContactExistException;
import com.pwc.addressbook.exception.ContactNotFoundException;
import com.pwc.addressbook.model.Contact;
import com.pwc.addressbook.repository.AddressBookRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import static java.util.stream.Collectors.toList;

@Slf4j
@Service
public class AddressBookService {

    @Autowired
    private AddressBookRepository addressBookRepository;

    public void addContact(final Contact contact) {
        log.debug("adding new contact");
        final Optional<ContactEntity> contactEntity = addressBookRepository.getContactEntity(contact.getName());

        if(contactEntity.isPresent()) {
            log.error("Contact {} already exist", contact.getName());
            throw new ContactExistException(contact.getName());
        }
        addressBookRepository.addContact(contact);
    }

    public List<Contact> getAllContacts(final String sortOrder) {
        log.debug("getting all contacts");
        final List<ContactEntity> contactEntities = addressBookRepository.getAllContacts(sortOrder);

        return contactEntities.stream()
                .map(contactEntity ->  new Contact(contactEntity.getName(), contactEntity.getPhoneNo()))
                .collect(toList());
    }

    public Set<String> getUniqueContacts(final List<String> contactNames) {
        final List<String> contactsInDb = addressBookRepository.getAllContactNames();

        final Set<String> uniqueContacts = new HashSet<>(contactsInDb);
        uniqueContacts.addAll(contactNames);

        final Set<String> intersection = new HashSet<>(contactsInDb);
        intersection.retainAll(contactNames);

        uniqueContacts.removeAll(intersection);
        return uniqueContacts;



        }

    public Contact getContact(final String name) {
        final Optional<ContactEntity> contactEntity = addressBookRepository.getContactEntity(name);
        return contactEntity.map(e -> new Contact(e.getName(), e.getPhoneNo())).orElseThrow(() -> new ContactNotFoundException(name));
    }

}
