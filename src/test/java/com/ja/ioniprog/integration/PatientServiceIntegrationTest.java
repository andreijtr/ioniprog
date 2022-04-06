package com.ja.ioniprog.integration;

import com.ja.ioniprog.dao.PatientDao;
import com.ja.ioniprog.dao.PatientDoctorDao;
import com.ja.ioniprog.dao.audit.PatientAuditDao;
import com.ja.ioniprog.exception.NoChangeDetectedException;
import com.ja.ioniprog.model.dto.audit.ChangeDto;
import com.ja.ioniprog.model.dto.PatientDto;
import com.ja.ioniprog.model.dto.UserDto;
import com.ja.ioniprog.model.entity.Patient;
import com.ja.ioniprog.model.entity.PatientDoctor;
import com.ja.ioniprog.model.entity.User;
import com.ja.ioniprog.model.entity.audit.Audit;
import com.ja.ioniprog.model.entity.audit.PatientAudit;
import com.ja.ioniprog.model.params.PatientParams;
import com.ja.ioniprog.service.PatientService;
import com.ja.ioniprog.utils.application.JsonParser;
import com.ja.ioniprog.utils.enums.ApplicationEnum;
import org.dozer.Mapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PatientServiceIntegrationTest {

    // patient
    public static final String ID_PATIENT = "1";
    public static final String FIRST_NAME = "Jonny";
    public static final String LAST_NAME = "Crazy";
    public static final String PHONE = "0712345678";
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
    @Captor
    private ArgumentCaptor<PatientAudit> patientAuditCaptor;

    private DateTimeFormatter dateFormatter;
    private PatientParams patientParams;

    @Nested
    class SavePatientTests {
        @BeforeEach
        void setupEach() {
            dateFormatter = DateTimeFormatter.ofPattern(ApplicationEnum.DATE_FORMATTER.getName());

            UserDto createdBy = UserDto.builder().idUser(ID_USER).build();
            patientParams = PatientParams.builder().createdBy(createdBy).build();
        }

        @Test
        void should_SavePatient_When_GivenPatientDto() {
            // given
            Patient patient = Patient.builder().firstName(FIRST_NAME).lastName(LAST_NAME).phone(PHONE)
                    .birthdayDate(LocalDate.parse(BIRTHDAY_DATE, dateFormatter)).details(DETAILS).status(STATUS).build();
            PatientDto patientDto = PatientDto.builder().firstName(FIRST_NAME).lastName(LAST_NAME).phone(PHONE)
                    .birthdayDate(BIRTHDAY_DATE).details(DETAILS).status(STATUS).build();

            when(dozerMapperMock.map(patientDto, Patient.class)).thenReturn(patient);

            // when
            patientServiceMock.save(patientDto, patientParams);

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
                patientServiceMock.save(patientDto, patientParams);
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

            User createdBy = User.builder().id(Integer.parseInt(ID_USER)).build();

            PatientDoctor patientDoctor = PatientDoctor.builder().patient(patient)
                    .doctor(createdBy).createdBy(createdBy)
                    .createdOn(LocalDateTime.of(2022, 03, 06, 20, 07))
                    .state("ACTIVE").build();

            when(dozerMapperMock.map(patientDto, Patient.class)).thenReturn(patient);

            // when
            try (MockedStatic<PatientDoctor> patientDoctorMockedStatic = mockStatic(PatientDoctor.class)) {
                patientDoctorMockedStatic.when(() -> PatientDoctor.createPatientDoctor(any(), any(), any())).thenReturn(patientDoctor);
                patientServiceMock.save(patientDto, patientParams);
            }

            // then
            verify(patientDoctorDaoMock).save(patientDoctor);
        }
    }

    @Nested
    class UpdatePatientTests {
        @BeforeEach
        void setupEach() {
            dateFormatter = DateTimeFormatter.ofPattern(ApplicationEnum.DATE_FORMATTER.getName());

            UserDto createdBy = UserDto.builder().idUser(ID_USER).build();
            patientParams = PatientParams.builder().loggedUser(createdBy).build();
        }

        @Test
        void should_ThrowNoChangesDetectedException_When_PatientNotChanged() {
            // given
            Patient entityToUpdate = Patient.builder().id(Integer.parseInt(ID_PATIENT)).firstName(FIRST_NAME).lastName(LAST_NAME).phone(PHONE)
                    .birthdayDate(LocalDate.parse(BIRTHDAY_DATE, dateFormatter)).details(DETAILS).status(STATUS).build();
            PatientDto patientDto = PatientDto.builder().idPatient(ID_PATIENT).firstName(FIRST_NAME).lastName(LAST_NAME).phone(PHONE)
                    .birthdayDate(BIRTHDAY_DATE).details(DETAILS).status(STATUS).build();
            when(patientDaoMock.getById(Integer.parseInt(ID_PATIENT)))
                    .thenReturn(entityToUpdate);

            // when
            Executable executable = () -> patientServiceMock.update(patientDto, any());

            // then
            assertThrows(NoChangeDetectedException.class, executable);
        }

        @Test
        void should_UpdatePatient_When_CorrectPatientDto() {
            // given
            Patient entityToUpdate = Patient.builder().id(Integer.parseInt(ID_PATIENT)).firstName(FIRST_NAME).lastName(LAST_NAME).phone(PHONE)
                                                .birthdayDate(LocalDate.parse(BIRTHDAY_DATE, dateFormatter)).details(DETAILS).status(STATUS).build();
            PatientDto patientDto = PatientDto.builder().idPatient(ID_PATIENT).firstName("OTHER_FIRSTNAME").lastName("OTHER_LASTNAME").phone("0700112233")
                                                .birthdayDate("01.01.2020").details("OTHER_DETAILS").status(STATUS).build();
            Patient expectedPatient = Patient.builder().id(Integer.parseInt(ID_PATIENT)).firstName("OTHER_FIRSTNAME").lastName("OTHER_LASTNAME").phone("0700112233")
                                                .birthdayDate(LocalDate.parse("01.01.2020", dateFormatter)).details("OTHER_DETAILS").status(STATUS).build();

            when(patientDaoMock.getById(Integer.parseInt(ID_PATIENT)))
                    .thenReturn(entityToUpdate);

            // when
            patientServiceMock.update(patientDto, patientParams);

            // then
            verify(patientDaoMock, times(1)).update(expectedPatient);
        }

        @Test
        void should_InsertPatientAudit_When_CorrentPatientDto() {
            // given
            Patient patientOld = Patient.builder().id(Integer.parseInt(ID_PATIENT)).firstName(FIRST_NAME).lastName(LAST_NAME).phone(PHONE)
                                                    .birthdayDate(LocalDate.parse(BIRTHDAY_DATE, dateFormatter)).details(DETAILS).status(STATUS).build();
            PatientDto patientDto = PatientDto.builder().idPatient(ID_PATIENT).firstName("OTHER_FIRSTNAME").lastName("OTHER_LASTNAME").phone("0700112233")
                                                    .birthdayDate("01.01.2020").details("OTHER_DETAILS").status(STATUS).build();
            Patient patientNew = Patient.builder().id(Integer.parseInt(ID_PATIENT)).firstName("OTHER_FIRSTNAME").lastName("OTHER_LASTNAME").phone("0700112233")
                                                    .birthdayDate(LocalDate.parse("01.01.2020", dateFormatter)).details("OTHER_DETAILS").status(STATUS).build();

            List<ChangeDto> changes = new ArrayList<>();
            changes.add(new ChangeDto("First name", patientDto.getFirstName(), patientOld.getFirstName()));
            changes.add(new ChangeDto("Last name", patientDto.getLastName(), patientOld.getLastName()));
            changes.add(new ChangeDto("Phone", patientDto.getPhone(), patientOld.getPhone()));
            changes.add(new ChangeDto("Birthday date", patientDto.getBirthdayDate(), patientOld.getBirthdayDate().toString()));
            changes.add(new ChangeDto("Details", patientDto.getDetails(), patientOld.getDetails()));
            changes.add(new ChangeDto("Status", patientDto.getStatus(), patientOld.getStatus()));

            String changesString = "[{\"columnName\":\"First name\",\"newValue\":\"OTHER_FIRSTNAME\",\"oldValue\":\"Jonny\"},{\"columnName\":\"Last name\",\"newValue\":\"OTHER_LASTNAME\",\"oldValue\":\"Crazy\"},{\"columnName\":\"Phone\",\"newValue\":\"0700112233\",\"oldValue\":\"0712345678\"},{\"columnName\":\"Birthday date\",\"newValue\":\"01.01.2020\",\"oldValue\":\"01.02.1990\"},{\"columnName\":\"Details\",\"newValue\":\"OTHER_DETAILS\",\"oldValue\":\"Testing details\"}]";
            User createdBy = User.builder().id(Integer.parseInt(ID_USER)).build();
            String entityVersion = "{\"id\":\"\",\"firstName\":\"OTHER_FIRSTNAME\",\"lastName\":\"OTHER_LASTNAME\",\"phone\":\"OTHER_PHONE\",\"birthdayDate\":\"01.01.2020\",\"details\":\"OTHER_DETAILS\",\"status\":\"ACTIVE\"}";
            Audit audit = Audit.builder()
                                        .createdOn(LocalDateTime.of(2022, 03, 17, 20, 01))
                                        .actionType("UPDATE").entityVersion(entityVersion).createdBy(createdBy).changes(changesString)
                                        .build();
            PatientAudit expectedAudit = new PatientAudit(patientNew, audit);

            when(patientDaoMock.getById(Integer.parseInt(ID_PATIENT)))
                    .thenReturn(patientOld);

            // when
            try (MockedStatic<Audit> auditMockedStatic = mockStatic(Audit.class)) {
                auditMockedStatic.when(() -> Audit.getAudit(any(), any(), any())).thenReturn(audit);
                patientServiceMock.update(patientDto, patientParams);
            }

            // then
            verify(patientAuditDaoMock).save(patientAuditCaptor.capture());
            PatientAudit actualAudit = patientAuditCaptor.getValue();
            assertEquals(expectedAudit.getPatientEntity(), actualAudit.getPatientEntity());
            assertEquals(expectedAudit.getAudit(), actualAudit.getAudit());
        }
    }

}