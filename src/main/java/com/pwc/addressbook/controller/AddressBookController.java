package com.pwc.addressbook.controller;

import com.pwc.addressbook.exception.ContactValidationException;
import com.pwc.addressbook.model.Contact;
import com.pwc.addressbook.service.AddressBookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

import static com.pwc.addressbook.constants.StringConstants.COLON;
import static com.pwc.addressbook.constants.StringConstants.SLASH;
import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.http.HttpStatus.CREATED;

@Slf4j
@Tag(name = "Address Book")
@RestController
public class AddressBookController {

    @Autowired
    private AddressBookService addressBookService;


    @Value("${base.uri:localhost}")
    private String host;

    @Value("${server.port}")
    private String port;

    @Value("${server.servlet.context-path}")
    private String uri;

    @Operation(summary = "This end point add a new contact to address book")
    @PostMapping(path="/add")
    public ResponseEntity<?> addContact(@RequestBody @Valid final Contact contact, final Errors errors) {
        log.info("creating new contact");
        if(errors.hasErrors()) {
            throw new ContactValidationException(errors);
        }
        addressBookService.addContact(contact);
        return ResponseEntity.status(CREATED).header(LOCATION, location(contact.getName())).build();
    }

    @Operation(summary = "This end point returns all the contacts in the address book. sortOrder = asc | desc")
    @GetMapping(value = "/contacts")
    public ResponseEntity<List<Contact>> getContacts(final @RequestParam(name = "sortOrder", defaultValue = "asc") String sortOrder) {
        log.info("get all contacts");
        return ResponseEntity.ok(addressBookService.getAllContacts(sortOrder));
    }

    @Operation(summary = "This end point returns unique contacts between two address books")
    @PostMapping("/compare")
    public ResponseEntity<Set<String>> getUniqueContacts(@RequestBody final List<String> contactNames) {
        return ResponseEntity.ok(addressBookService.getUniqueContacts(contactNames));
    }

    @Operation(summary = "This end point returns phone no of a given contact name")
    @GetMapping("/{name}")
    public ResponseEntity<Contact> getContact(@PathVariable final String name) {
        log.info("Get phone no for {}", name);
        return ResponseEntity.ok(addressBookService.getContact(name));
    }

    private String location(final String name) {
        return host + COLON + port + uri + SLASH + name;
    }
}
