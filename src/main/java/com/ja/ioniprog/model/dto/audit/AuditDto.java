package com.ja.ioniprog.model.dto.audit;

import com.ja.ioniprog.model.dto.UserShortDto;
import lombok.*;

import java.util.List;

@Getter @Setter @ToString @Builder @NoArgsConstructor @AllArgsConstructor
public class AuditDto {
    private String createdOn;
    private String actionType;
    private List<ChangeDto> changes;
    private Object entityVersion;
    private UserShortDto createdBy;
}
