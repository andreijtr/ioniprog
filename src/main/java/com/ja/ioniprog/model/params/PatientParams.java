package com.ja.ioniprog.model.params;

import com.ja.ioniprog.model.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class PatientParams {
    private UserDto createdBy;
    private int offset;
    private int pageSize;
}
