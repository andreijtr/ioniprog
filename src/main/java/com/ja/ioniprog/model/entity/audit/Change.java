package com.ja.ioniprog.model.entity.audit;

import lombok.*;

@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class Change {
    private int id;
    private int idAudit;
    private String entityType;
    private String columnName;
    private String oldValue;
    private String newValue;
}
