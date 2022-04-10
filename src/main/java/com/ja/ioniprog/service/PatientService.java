package com.ja.ioniprog.service;

import com.ja.ioniprog.config.security.annotations.IsDoctor;
import com.ja.ioniprog.dao.PatientDao;
import com.ja.ioniprog.dao.UserDao;
import com.ja.ioniprog.dao.audit.PatientAuditDao;
import com.ja.ioniprog.dao.PatientDoctorDao;
import com.ja.ioniprog.exception.NoChangeDetectedException;
import com.ja.ioniprog.model.dto.UserShortDto;
import com.ja.ioniprog.model.dto.audit.ChangeDto;
import com.ja.ioniprog.model.dto.PatientDoctorDto;
import com.ja.ioniprog.model.dto.PatientDto;
import com.ja.ioniprog.model.dto.audit.PatientAuditDto;
import com.ja.ioniprog.model.entity.Patient;
import com.ja.ioniprog.model.entity.PatientDoctor;
import com.ja.ioniprog.model.entity.User;
import com.ja.ioniprog.model.entity.audit.Audit;
import com.ja.ioniprog.model.entity.audit.PatientAudit;
import com.ja.ioniprog.model.paging.PageResult;
import com.ja.ioniprog.model.params.PatientParams;
import com.ja.ioniprog.model.params.UserParams;
import com.ja.ioniprog.utils.application.JsonParser;
import com.ja.ioniprog.utils.enums.ApplicationEnum;
import com.ja.ioniprog.utils.enums.AuditEnum;
import com.ja.ioniprog.utils.enums.RoleEnum;
import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.OptimisticLockException;
import javax.validation.Valid;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class PatientService {
    Logger logger = LoggerFactory.getLogger(PatientService.class);

    private PatientDao       patientDao;
    private PatientAuditDao  patientAuditDao;
    private PatientDoctorDao patientDoctorDao;
    private UserDao          userDao;
    private JsonParser       jsonParser;
    private Mapper           dozerMapper;

    public PatientService(PatientDao patientDao, PatientAuditDao patientAuditDao, PatientDoctorDao patientDoctorDao, UserDao userDao, JsonParser jsonParser, Mapper dozerMapper) {
        this.patientDao = patientDao;
        this.patientAuditDao = patientAuditDao;
        this.patientDoctorDao = patientDoctorDao;
        this.userDao = userDao;
        this.jsonParser = jsonParser;
        this.dozerMapper = dozerMapper;
    }

    public PatientDto getById(String idPatient) {
        return dozerMapper.map(patientDao.getById(Integer.parseInt(idPatient)), PatientDto.class);
    }

    @IsDoctor
    public PageResult<PatientDoctorDto> getPatientsPaging(PatientParams patientParams) {
        int offset = patientParams.getOffset();
        int pageSize = patientParams.getPageSize();

        List<PatientDoctorDto> patientDoctorDtos = new ArrayList<>();
        List<PatientDoctor> patientDoctors = patientDao.getPatientsPaging(patientParams);
        if (patientDoctors != null) {
            patientDoctorDtos = patientDoctors.stream()
                                              .map(patientDoctor -> dozerMapper.map(patientDoctor, PatientDoctorDto.class))
                                              .collect(Collectors.toList());
        }
        long totalRows = patientDao.getCountPaging(patientParams);
        Double doubleTotalPages = Math.ceil((double) totalRows / pageSize);
        long totalPages = doubleTotalPages.longValue();
        long currentPage = (offset / pageSize) + 1;

        return new PageResult<PatientDoctorDto>(patientDoctorDtos, totalPages, currentPage);
    }

    @Transactional
    @IsDoctor
    public void save(@Valid PatientDto patientDto, PatientParams patientParams) {
        logger.info("PatientService: save patient");
        User userResponsible = User.builder()
                .id(Integer.parseInt(patientParams.getCreatedBy().getIdUser()))
                .build();
        Patient patient = dozerMapper.map(patientDto, Patient.class);
        patientDao.save(patient);

        Audit audit = Audit.getAudit(AuditEnum.INSERT, userResponsible, jsonParser.getJson(patient));
        PatientAudit insertAudit = new PatientAudit(patient, audit);
        patientAuditDao.save(insertAudit);

        PatientDoctor patientDoctor = PatientDoctor.createPatientDoctor(patient, userResponsible, userResponsible);
        patientDoctorDao.save(patientDoctor);
    }

    @Transactional
    @IsDoctor
    public void update(@Valid PatientDto patientDto, PatientParams patientParams) throws NoChangeDetectedException, OptimisticLockException {
        logger.info("PatientService: update patient");

        Patient patientOld = patientDao.getById(Integer.parseInt(patientDto.getIdPatient()));

        if (patientOld.getVersion() == patientDto.getVersion()) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(ApplicationEnum.DATE_FORMATTER.getName());
            List<ChangeDto> changes = new ArrayList<>();

            if (!patientDto.getFirstName().contentEquals(patientOld.getFirstName())) {
                changes.add(new ChangeDto("First name", patientDto.getFirstName(), patientOld.getFirstName()));
                patientOld.setFirstName(patientDto.getFirstName());
            }

            if (!patientDto.getLastName().contentEquals(patientOld.getLastName())) {
                changes.add(new ChangeDto("Last name", patientDto.getLastName(), patientOld.getLastName()));
                patientOld.setLastName(patientDto.getLastName());
            }

            if (!patientDto.getPhone().contentEquals(patientOld.getPhone())) {
                changes.add(new ChangeDto("Phone", patientDto.getPhone(), patientOld.getPhone()));
                patientOld.setPhone(patientDto.getPhone());
            }

            if (!LocalDate.parse(patientDto.getBirthdayDate(), formatter).isEqual(patientOld.getBirthdayDate())) {
                changes.add(new ChangeDto("Birthday date", patientDto.getBirthdayDate(), patientOld.getBirthdayDate().toString()));
                patientOld.setBirthdayDate(LocalDate.parse(patientDto.getBirthdayDate(), formatter));
            }

            if (patientDto.getDetails() != null) {
                if ((patientOld.getDetails() != null && !patientDto.getDetails().contentEquals(patientOld.getDetails())) || patientOld.getDetails() == null) {
                    changes.add(new ChangeDto("Details", patientDto.getDetails(), patientOld.getDetails()));
                    patientOld.setDetails(patientDto.getDetails());
                }
            } else {
                if (patientOld.getDetails() != null) {
                    changes.add(new ChangeDto("Details", patientDto.getDetails(), patientOld.getDetails()));
                    patientOld.setDetails(patientDto.getDetails());
                }
            }

            if (patientDto.getStatus() != null && !patientDto.getStatus().contentEquals(patientOld.getStatus())) {
                changes.add(new ChangeDto("Status", patientDto.getStatus(), patientOld.getStatus()));
                patientOld.setStatus(patientDto.getStatus());
            }

            if (!changes.isEmpty()) {
                User userResponsible = User.builder()
                        .id(Integer.parseInt(patientParams.getLoggedUser().getIdUser()))
                        .build();
                patientDao.update(patientOld);

                Audit audit = Audit.getAudit(AuditEnum.UPDATE, userResponsible, jsonParser.getJson(patientOld));
                audit.setChanges(jsonParser.getJson(changes));
                PatientAudit patientAudit = new PatientAudit(patientOld, audit);
                patientAuditDao.save(patientAudit);
            } else {
                logger.info("Update failed because no change was detected!");
                throw new NoChangeDetectedException("Update failed because no change was detected.");
            }
        } else {
            logger.info("Update failed because entity was already modified!");
            throw new OptimisticLockException("Update failed because entity was already modified!");
        }
    }

    @IsDoctor
    public List<PatientAuditDto> getAuditByIdPatient(String idPatient) {
        List<PatientAuditDto> auditDto = new LinkedList<>();
        List<PatientAudit> audit = patientAuditDao.getAuditByIdPatient(Integer.parseInt(idPatient));
        if (null != audit) {
            auditDto = audit.stream()
                            .map(patientAudit -> dozerMapper.map(patientAudit, PatientAuditDto.class))
                            .collect(Collectors.toList());
        }

        return auditDto;
    }

    @IsDoctor
    public List<PatientDoctorDto> getPatientDoctors(PatientParams patientParams) {
        List<PatientDoctorDto> dtos = new LinkedList<>();
        List<PatientDoctor> patientDoctors = patientDoctorDao.getPatientDoctors(patientParams);

        dtos = patientDoctors.stream()
                            .map(patientDoctor -> dozerMapper.map(patientDoctor, PatientDoctorDto.class))
                            .collect(Collectors.toList());

        if (patientParams.isOrderByPatient()) {
            dtos = dtos.stream()
                        .sorted(Comparator.comparing(patientDoctorDto -> patientDoctorDto.getPatientDto().getLastName().toLowerCase()))
                        .collect(Collectors.toList());
        }
        else if (patientParams.isOrderByDoctor()) {
            dtos = dtos.stream()
                    .sorted(Comparator.comparing(patientDoctorDto -> patientDoctorDto.getDoctorDto().getLastName()))
                    .collect(Collectors.toList());
        }

        return dtos;
    }

    @IsDoctor
    public List<UserShortDto> getLovDoctors() {
        List<User> users = userDao.getUsers(UserParams.builder().expired("0").build());
        return users.stream()
                    .filter(user -> user.hasRole(RoleEnum.DOCTOR))
                    .map(user -> new UserShortDto(String.valueOf(user.getId()), user.getFirstName(), user.getLastName(), user.getPhone(), user.getUsername()))
                    .collect(Collectors.toList());
    }
}
