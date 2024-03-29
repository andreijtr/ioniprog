package com.ja.ioniprog.model.dto;

import lombok.*;

@Getter @Setter @ToString @NoArgsConstructor @AllArgsConstructor @Builder
public class LocationDto {
    private String idLocation;
    private String name;
    private UserShortDto userDto;
    private String state;
}
