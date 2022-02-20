package com.ja.ioniprog.model.params;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder @AllArgsConstructor @NoArgsConstructor
public class UserParams {
    String id;
    String username;
    String expired;
    String loginAttemptsRemaining;
}
