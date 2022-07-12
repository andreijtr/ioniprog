package com.ja.ioniprog.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "appointment")
@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor @ToString
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_appointment")
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_location")
    @NotNull(message = "Location cannot be empty.")
    private Location location;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_doctor")
    @NotNull(message = "Doctor cannot be empty.")
    private User doctor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_patient")
    @NotNull(message = "Patient cannot be empty.")
    private Patient patient;

    @Column(name = "day")
    @NotNull(message = "Day cannot be empty.")
//    @JsonDeserialize(using = LocalDateDeserializer.class)
//    @JsonSerialize(using = LocalDateSerializer.class)
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate day;

    @Column(name = "start_time")
    @NotNull(message = "Start time cannot be empty")
    @JsonFormat(pattern="HH:mm")
    private LocalTime startTime;

    @Column(name = "end_time")
    @NotNull(message = "End time cannot be empty.")
    @JsonFormat(pattern="HH:mm")
    private LocalTime endTime;

    @Column(name = "description")
    @Size(max = 255, message = "Description should not contain more than 255 letters")
    private String description;
}
