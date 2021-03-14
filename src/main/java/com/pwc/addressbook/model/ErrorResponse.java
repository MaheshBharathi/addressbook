package com.pwc.addressbook.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;

@Data
@JsonInclude(NON_EMPTY)
public class ErrorResponse {

    private String type;

    @NotNull
    private Integer status;

    @NotEmpty
    private String description;

    public ErrorResponse(final String type, final Integer status, final String description) {
        this.type = type;
        this.status = status;
        this.description = description;
    }

    public ErrorResponse(final Integer status, final String description) {
        this.status = status;
        this.description = description;
    }

}
