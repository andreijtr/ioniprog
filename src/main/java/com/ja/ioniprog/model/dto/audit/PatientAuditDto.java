package com.ja.ioniprog.model.dto.audit;

import lombok.*;

@Getter @Setter @ToString @Builder @NoArgsConstructor @AllArgsConstructor
public class PatientAuditDto {
    private String idPatientAudit;
    private String idPatient;
    private AuditDto auditDto;
}
