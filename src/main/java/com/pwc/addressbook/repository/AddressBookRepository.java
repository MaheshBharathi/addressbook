package com.pwc.addressbook.repository;

import com.pwc.addressbook.entity.ContactEntity;
import com.pwc.addressbook.model.Contact;

import java.util.List;
import java.util.Optional;

public interface AddressBookRepository {

    void addContact(Contact contact);
    List<ContactEntity> getAllContacts(String sortOrder);
    List<String> getAllContactNames();
    Optional<ContactEntity> getContactEntity(String name);
}
