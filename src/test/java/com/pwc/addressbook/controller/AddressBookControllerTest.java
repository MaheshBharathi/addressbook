package com.pwc.addressbook.controller;

import com.pwc.addressbook.exception.ContactValidationException;
import com.pwc.addressbook.model.Contact;
import com.pwc.addressbook.service.AddressBookService;
import com.pwc.addressbook.util.ReflectionHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import java.util.*;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddressBookControllerTest {

    @Mock
    private AddressBookService addressBookService;

    @InjectMocks
    private AddressBookController controller;

    @Mock
    private Errors errors;

    @Mock
    private FieldError fieldError;

    @BeforeEach
    public void setup() throws Exception {
        ReflectionHelper.setField(controller, "host", "localhost");
        ReflectionHelper.setField(controller, "port", "8080");
        ReflectionHelper.setField(controller, "uri", "/addressbook");
    }

    @Test
    public void addContactShouldAddNewContactToAddressBook() {
        final Contact contact = new Contact("contact1", "0470000000");

        given(errors.hasErrors()).willReturn(false);

        ResponseEntity result = controller.addContact(contact, errors);

        assertEquals(201, result.getStatusCodeValue());
        assertEquals("localhost:8080/addressbook/contact1", result.getHeaders().getLocation().toString());
    }

    @Test
    public void addContactShouldThrowValidationExceptionOnValidationErrors() {
        final Contact contact = new Contact("contact1", "0470000000");

        given(errors.hasErrors()).willReturn(true);
        given(fieldError.getField()).willReturn("phoneNo");
        given(fieldError.getDefaultMessage()).willReturn("cannot be empty");
        when(errors.getFieldErrors()).thenReturn(Collections.singletonList(fieldError));

        Exception exception = assertThrows(ContactValidationException.class, () -> controller.addContact(contact, errors));
        assertEquals("phoneNo: cannot be empty", exception.getMessage());
    }

    @Test
    public void shouldGetAllContactsFromAddressBook() {
        final Contact contact1 = new Contact("contact1", "0470000000");
        final Contact contact2 = new Contact("contact2", "0480000000");
        final List<Contact> contacts = asList(contact1, contact2);

        for(final String sortOrder : asList("", "blah", "asc", "desc")) {
            ArgumentCaptor<String> sortOrderArgument = ArgumentCaptor.forClass(String.class);

            given(addressBookService.getAllContacts(sortOrderArgument.capture())).willReturn(contacts);

            final ResponseEntity<List<Contact>> result = controller.getContacts(sortOrder);

            final List<Contact> resultContacts = result.getBody();
            assertEquals(200, result.getStatusCode().value());
            assertEquals(2, resultContacts.size());

            assertEquals(sortOrder, sortOrderArgument.getValue());
            verify(addressBookService, times(1)).getAllContacts(eq(sortOrder));

            assertEquals("contact1", resultContacts.get(0).getName());
            assertEquals("contact2", resultContacts.get(1).getName());
            assertEquals("0470000000", resultContacts.get(0).getPhoneNo());
            assertEquals("0480000000", resultContacts.get(1).getPhoneNo());
            reset(addressBookService);
        }
    }

    @Test
    public void shouldCompareContacts() {
        final List<String> requestContacts = asList("contact1", "contact2");
        final Set<String> responseContacts = new HashSet<>(asList("contact1", "contact4", "contact5"));

        given(addressBookService.getUniqueContacts(any(List.class))).willReturn(responseContacts);

        final ResponseEntity<Set<String>> result = controller.getUniqueContacts(requestContacts);

        final Set<String> resultContacts = result.getBody();
        assertEquals(200, result.getStatusCode().value());
        assertEquals(3, resultContacts.size());
        verify(addressBookService, times(1)).getUniqueContacts(any(List.class));
     }

    @Test
    public void shouldGetPhoneNoOfContact() {
        final Contact contact = new Contact("contact1", "0470000000");

        given(addressBookService.getContact(eq("contact1"))).willReturn(contact);

        final ResponseEntity<Contact> result = controller.getContact("contact1");

        final Contact resultContact = result.getBody();
        assertEquals(200, result.getStatusCode().value());
        assertEquals("contact1", resultContact.getName());
        assertEquals("0470000000", resultContact.getPhoneNo());
        verify(addressBookService, times(1)).getContact(eq("contact1"));
    }

}