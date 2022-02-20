package com.ja.ioniprog.model.dto.audit;

import com.ja.ioniprog.model.dto.LocationDto;
import com.ja.ioniprog.model.entity.User;

public class LocationAuditDto {
    private String idLocationAudit;
    private LocationDto locationDto;
    private String action;
    private User user; //TO DO: UserDto
    private String dateAction;
    private LocationDto locationVersion;
}
