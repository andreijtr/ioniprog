package com.ja.ioniprog.service;

import com.ja.ioniprog.dao.PatientDao;
import com.ja.ioniprog.dao.PatientDoctorDao;
import com.ja.ioniprog.dao.audit.PatientAuditDao;
import com.ja.ioniprog.model.dto.PatientDto;
import com.ja.ioniprog.model.dto.UserDto;
import com.ja.ioniprog.model.entity.Patient;
import com.ja.ioniprog.model.entity.PatientDoctor;
import com.ja.ioniprog.model.entity.User;
import com.ja.ioniprog.model.entity.audit.Audit;
import com.ja.ioniprog.model.entity.audit.PatientAudit;
import com.ja.ioniprog.model.params.PatientParams;
import com.ja.ioniprog.utils.application.JsonParser;
import com.ja.ioniprog.utils.enums.ApplicationEnum;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PatientServiceTest {

    // patient
    public static final String FIRST_NAME = "Jonny";
    public static final String LAST_NAME = "Crazy";
    public static final String PHONE = "0711223344";
    public static final String BIRTHDAY_DATE = "01.02.1990";
    public static final String DETAILS = "Testing details";
    public static final String STATUS = "ACTIVE";

    // user
    public static final String ID_USER = "1";

    @InjectMocks
    private PatientService patientServiceMock;
    @Mock
    private PatientDao patientDaoMock;
    @Mock
    private PatientAuditDao patientAuditDaoMock;
    @Mock
    private PatientDoctorDao patientDoctorDaoMock;
    @Mock
    private JsonParser jsonParserMock;
    @Mock
    private Mapper dozerMapperMock;

    @Nested
    class SavePatientTests {
        @Test
        void should_SavePatient_When_GivenPatientDto() {
            // given
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(ApplicationEnum.DATE_FORMATTER.getName());
            Patient patient = Patient.builder().firstName(FIRST_NAME).lastName(LAST_NAME).phone(PHONE)
                    .birthdayDate(LocalDate.parse(BIRTHDAY_DATE, formatter)).details(DETAILS).status(STATUS).build();
            PatientDto patientDto = PatientDto.builder().firstName(FIRST_NAME).lastName(LAST_NAME).phone(PHONE)
                    .birthdayDate(BIRTHDAY_DATE).details(DETAILS).status(STATUS).build();

            UserDto createdBy = UserDto.builder().idUser(ID_USER).build();
            PatientParams params = PatientParams.builder().createdBy(createdBy).build();

            when(dozerMapperMock.map(patientDto, Patient.class)).thenReturn(patient);

            // when
            patientServiceMock.save(patientDto, params);

            // then
            verify(patientDaoMock).save(patient);
            verifyNoMoreInteractions(patientDaoMock);
        }

        @Test
        void should_InsertPatientAudit_When_SavePatient() {
            // given
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(ApplicationEnum.DATE_FORMATTER.getName());
            Patient patient = Patient.builder().firstName(FIRST_NAME).lastName(LAST_NAME).phone(PHONE)
                                    .birthdayDate(LocalDate.parse(BIRTHDAY_DATE, formatter)).details(DETAILS).status(STATUS).build();
            PatientDto patientDto = PatientDto.builder().firstName(FIRST_NAME).lastName(LAST_NAME).phone(PHONE)
                                                .birthdayDate(BIRTHDAY_DATE).details(DETAILS).status(STATUS).build();

            UserDto createdByDto = UserDto.builder().idUser(ID_USER).build();
            PatientParams params = PatientParams.builder().createdBy(createdByDto).build();

            User createdBy = User.builder().id(Integer.parseInt(ID_USER)).build();
            String entityVersion = "{\"id\":\"\",\"firstName\":\"Jonny\",\"lastName\":\"Crazy\",\"phone\":\"0711223344\",\"birthdayDate\":\"1990-02-01\",\"details\":\"Testing details\",\"status\":\"ACTIVE\"}";
            Audit expectedAudit = Audit.builder()
                                        .createdOn(LocalDateTime.of(2022, 03, 06, 20, 07))
                                        .actionType("INSERT").entityVersion(entityVersion).createdBy(createdBy)
                                        .build();
            PatientAudit patientAudit = new PatientAudit(patient, expectedAudit);
            when(dozerMapperMock.map(patientDto, Patient.class)).thenReturn(patient);

            // when
            try (MockedStatic<Audit> auditMockedStatic = mockStatic(Audit.class)) {
                auditMockedStatic.when(() -> Audit.getAudit(any(), any(), any())).thenReturn(expectedAudit);
                patientServiceMock.save(patientDto, params);
            }

            // then
            verify(patientAuditDaoMock).save(patientAudit);
        }

        @Test
        void should_InsertPatientDoctor_When_SavePatient() {
            // given
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(ApplicationEnum.DATE_FORMATTER.getName());
            Patient patient = Patient.builder().firstName(FIRST_NAME).lastName(LAST_NAME).phone(PHONE)
                                        .birthdayDate(LocalDate.parse(BIRTHDAY_DATE, formatter)).details(DETAILS).status(STATUS).build();
            PatientDto patientDto = PatientDto.builder().firstName(FIRST_NAME).lastName(LAST_NAME).phone(PHONE)
                                                .birthdayDate(BIRTHDAY_DATE).details(DETAILS).status(STATUS).build();

            UserDto createdByDto = UserDto.builder().idUser(ID_USER).build();
            PatientParams params = PatientParams.builder().createdBy(createdByDto).build();

            User createdBy = User.builder().id(Integer.parseInt(ID_USER)).build();

            PatientDoctor patientDoctor = PatientDoctor.builder().patient(patient)
                    .doctor(createdBy).createdBy(createdBy)
                    .createdOn(LocalDateTime.of(2022, 03, 06, 20, 07))
                    .state("ACTIVE").build();

            when(dozerMapperMock.map(patientDto, Patient.class)).thenReturn(patient);

            // when
            try (MockedStatic<PatientDoctor> patientDoctorMockedStatic = mockStatic(PatientDoctor.class)) {
                patientDoctorMockedStatic.when(() -> PatientDoctor.createPatientDoctor(any(), any(), any())).thenReturn(patientDoctor);
                patientServiceMock.save(patientDto, params);
            }

            // then
            verify(patientDoctorDaoMock).save(patientDoctor);
        }
    }

}