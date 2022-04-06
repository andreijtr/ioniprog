package com.ja.ioniprog.model.params;

import com.ja.ioniprog.model.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class PatientParams {
    private String idPatient;
    private String idDoctor;
    private String firstName;
    private String lastName;
    private String phone;
    private String globalSearch;
    private String state;

    private UserDto createdBy;
    private UserDto doctor;
    private UserDto loggedUser;

    private boolean orderByPatient;
    private boolean orderByDoctor;

    private int offset;
    private int pageSize;
}
