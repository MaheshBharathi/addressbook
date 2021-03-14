package com.pwc.addressbook.entity;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.UUID;

@Entity
@Getter
@Setter
public class ContactEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(unique=true)
    private String name;

    @Column
    private String phoneNo;

}
