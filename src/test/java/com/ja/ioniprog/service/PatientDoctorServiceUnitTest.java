package com.ja.ioniprog.service;

import com.ja.ioniprog.dao.PatientDoctorDao;
import com.ja.ioniprog.exception.IllegalOperationException;
import com.ja.ioniprog.model.dto.UserDto;
import com.ja.ioniprog.model.entity.PatientDoctor;
import com.ja.ioniprog.model.entity.User;
import com.ja.ioniprog.model.params.PatientParams;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PatientDoctorServiceUnitTest {
    // user
    public static final String ID_USER = "1";

    @InjectMocks
    private PatientDoctorService patientDoctorServiceMock;
    @Mock
    private PatientDoctorDao patientDoctorDaoMock;
    @Captor
    private ArgumentCaptor<PatientDoctor> patientDoctorCaptor;

    @Nested
    class DeletePatientDoctorTestClass {

        private PatientParams patientParams;

        @BeforeEach
        void setup() {
            UserDto userDto = UserDto.builder().idUser(ID_USER).build();
            this.patientParams = PatientParams.builder().loggedUser(userDto).build();
        }

        @Test
        public void when_PatientDoctorNotFound_Then_NoDelete(){
            String idPatientDoctor = "1";
            when(patientDoctorDaoMock.getById(1))
                    .thenReturn(null);

            patientDoctorServiceMock.delete(idPatientDoctor, patientParams);

            verify(patientDoctorDaoMock, times(0)).update(any());
        }

        @Test
        public void when_UserTryToDeletePatientThatBelongsToOtherUser_Then_ThrowException() {
            String idPatientDoctor = "1";
            User createBy = User.builder().id(1).build();
            PatientDoctor pd = PatientDoctor.builder().id(1).createdBy(createBy).deletedOn(LocalDateTime.now()).state("DELETED").build();
            patientParams.getLoggedUser().setIdUser("2");

            when(patientDoctorDaoMock.getById(1))
                    .thenReturn(pd);

            Executable executable = () -> patientDoctorServiceMock.delete(idPatientDoctor, patientParams);

            assertThrows(IllegalOperationException.class, executable);
        }

        @Test
        public void when_PatientDoctorIsDeleted_Then_ThrowException() {
            String idPatientDoctor = "1";
            User createBy = User.builder().id(1).build();
            PatientDoctor pd = PatientDoctor.builder().id(1).createdBy(createBy).deletedOn(LocalDateTime.now()).state("DELETED").build();
            when(patientDoctorDaoMock.getById(1))
                    .thenReturn(pd);

            Executable executable = () -> patientDoctorServiceMock.delete(idPatientDoctor, patientParams);

            assertThrows(IllegalOperationException.class, executable);
        }

        @Test
        public void when_PatientDoctorIsActive_Then_Delete() {
            String idPatientDoctor = "1";
            User createBy = User.builder().id(1).build();
            PatientDoctor pd = PatientDoctor.builder().id(1).createdBy(createBy).state("ACTIVE").build();
            when(patientDoctorDaoMock.getById(1))
                    .thenReturn(pd);

            patientDoctorServiceMock.delete(idPatientDoctor, patientParams);

            verify(patientDoctorDaoMock, times(1)).update(patientDoctorCaptor.capture());

            PatientDoctor actual = patientDoctorCaptor.getValue();
            assertEquals("DELETED", actual.getState());
            assertNotNull(actual.getDeletedOn());
        }
    }
}