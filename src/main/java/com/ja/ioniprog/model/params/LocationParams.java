package com.ja.ioniprog.model.params;

import lombok.*;

@Getter @Setter @ToString @NoArgsConstructor @AllArgsConstructor @Builder
public class LocationParams {
    private String idDoctor;
    private String idLocation;
    private String state;
}
