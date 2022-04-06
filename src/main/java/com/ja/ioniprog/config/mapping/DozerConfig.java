package com.ja.ioniprog.config.mapping;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ja.ioniprog.config.mapping.converters.ChangesCustomConverter;
import com.ja.ioniprog.config.mapping.converters.LocalDateCustomConverter;
import com.ja.ioniprog.config.mapping.converters.LocalDateTimeCustomConverter;
import com.ja.ioniprog.model.dto.UserShortDto;
import com.ja.ioniprog.model.dto.PatientDoctorDto;
import com.ja.ioniprog.model.dto.PatientDto;
import com.ja.ioniprog.model.dto.UserDto;
import com.ja.ioniprog.model.dto.audit.AuditDto;
import com.ja.ioniprog.model.dto.audit.PatientAuditDto;
import com.ja.ioniprog.model.entity.Patient;
import com.ja.ioniprog.model.entity.PatientDoctor;
import com.ja.ioniprog.model.entity.User;
import com.ja.ioniprog.model.entity.audit.Audit;
import com.ja.ioniprog.model.entity.audit.PatientAudit;
import org.dozer.CustomConverter;
import org.dozer.DozerBeanMapper;
import org.dozer.loader.api.BeanMappingBuilder;
import org.dozer.loader.api.FieldsMappingOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DozerConfig {

    @Bean
    public DozerBeanMapper beanMapper() {
        DozerBeanMapper dozerBeanMapper = new DozerBeanMapper();

        dozerBeanMapper.addMapping(userToUserDtoMapping());
        dozerBeanMapper.addMapping(patientToPatientDtoMapping());
        dozerBeanMapper.addMapping(userToUserShortDtoMapping());
        dozerBeanMapper.addMapping(patientDoctorToPatientDoctorDtoMapping());
        dozerBeanMapper.addMapping(patientAuditToPatientAuditDtoMapping());
        dozerBeanMapper.addMapping(auditToAuditDtoMapping());

        return dozerBeanMapper;
    }

    // MAPPINGS
    @Bean
    public BeanMappingBuilder userToUserDtoMapping() {
        return new BeanMappingBuilder() {
            @Override
            protected void configure() {
                mapping(User.class, UserDto.class)
                        .fields("id", "idUser")
                        .exclude("roles");
            }
        };
    }

    @Bean
    public BeanMappingBuilder patientToPatientDtoMapping() {
        return new BeanMappingBuilder() {
            @Override
            protected void configure() {
                mapping(Patient.class, PatientDto.class)
                        .fields("id", "idPatient")
                        .fields("birthdayDate", "birthdayDate", FieldsMappingOptions.customConverter(LocalDateCustomConverter.class));
            }
        };
    }

    @Bean
    public BeanMappingBuilder userToUserShortDtoMapping() {
        return new BeanMappingBuilder() {
            @Override
            protected void configure() {
                mapping(User.class, UserShortDto.class)
                        .fields("id", "idUser");
            }
        };
    }

    @Bean
    public BeanMappingBuilder patientDoctorToPatientDoctorDtoMapping() {
        return new BeanMappingBuilder() {
            @Override
            protected void configure() {
                mapping(PatientDoctor.class, PatientDoctorDto.class)
                        .fields("id", "idPatientDoctor")
                        .fields("patient", "patientDto")
                        .fields("doctor", "doctorDto")
                        .fields("createdOn", "createdOn", FieldsMappingOptions.customConverter(LocalDateTimeCustomConverter.class))
                        .fields("deletedOn", "deletedOn", FieldsMappingOptions.customConverter(LocalDateTimeCustomConverter.class));

            }
        };
    }

    @Bean
    public BeanMappingBuilder patientAuditToPatientAuditDtoMapping() {
        return new BeanMappingBuilder(){

            @Override
            protected void configure() {
                mapping(PatientAudit.class, PatientAuditDto.class)
                        .fields("id", "idPatientAudit")
                        .fields("patientEntity.id", "idPatient")
                        .fields("audit", "auditDto");
            }
        };
    }
    @Bean
    public BeanMappingBuilder auditToAuditDtoMapping() {
        return new BeanMappingBuilder() {
            @Override
            protected void configure() {
                mapping(Audit.class, AuditDto.class)
                        .fields("createdOn", "createdOn", FieldsMappingOptions.customConverter(LocalDateTimeCustomConverter.class))
                        .fields("changes", "changes", FieldsMappingOptions.customConverter(ChangesCustomConverter.class))
                        .exclude("entityVersion");
            }
        };
    }

    // CONVERTERS
    @Bean
    public CustomConverter localDateCustomConverter() {
        return new LocalDateCustomConverter();
    }

    @Bean
    public CustomConverter localDateTimeCustomConverter() {
        return new LocalDateTimeCustomConverter();
    }

    @Bean
    public CustomConverter changesCustomConverter() {
        return new ChangesCustomConverter();
    }
}
