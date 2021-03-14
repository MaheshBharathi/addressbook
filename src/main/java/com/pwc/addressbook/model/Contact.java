package com.pwc.addressbook.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import static com.pwc.addressbook.constants.StringConstants.PHONE_NO_PATTERN;

@Data
@AllArgsConstructor
public class Contact {

    @NotEmpty
    @Size(min = 1, max = 50)
    private String name;

    @Pattern(regexp = PHONE_NO_PATTERN, message = "Invalid Phone Number")
    @NotEmpty
    @Size(min = 5, max = 15)
    private String phoneNo;
}
