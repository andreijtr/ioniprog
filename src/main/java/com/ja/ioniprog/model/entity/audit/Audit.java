package com.ja.ioniprog.model.entity.audit;

import com.ja.ioniprog.model.entity.User;
import com.ja.ioniprog.utils.enums.AuditEnum;
import lombok.*;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@Embeddable
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Audit {
    private LocalDateTime createdOn;
    private String actionType;
    private String changes;
    private String entityVersion;
    @ManyToOne(fetch = FetchType.LAZY)
    private User createdBy;

    public static Audit getAudit(AuditEnum auditType, User createdBy, String entityVersion) {
        Audit audit = Audit.builder()
                            .createdOn(LocalDateTime.now())
                            .actionType(auditType.getName())
                            .createdBy(createdBy)
                            .entityVersion(entityVersion)
                            .build();
        return audit;
    }
}
