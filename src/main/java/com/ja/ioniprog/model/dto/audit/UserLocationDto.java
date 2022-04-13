package com.ja.ioniprog.model.dto.audit;

import com.ja.ioniprog.model.dto.LocationDto;
import com.ja.ioniprog.model.dto.UserShortDto;
import lombok.*;

@Getter @Setter @ToString @NoArgsConstructor @AllArgsConstructor @Builder
public class UserLocationDto {
    private String idUserLocation;
    private UserShortDto doctorDto;
    private LocationDto locationDto;
    private String assignmentDate;
    private String retreatDate;
    private String details;
    private String state;
}
