package com.ja.ioniprog.model.params;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor @ToString
public class AppointmentParams {
    private int id;
    private int idLocation;
    private int idDoctor;
    private int idPatient;
    private LocalDate day;
    private LocalTime startTime;
    private LocalTime endTime;

    private LocalDate startCalendar;
    private LocalDate endCalendar;
}
