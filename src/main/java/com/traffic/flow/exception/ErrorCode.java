package com.traffic.flow.exception;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
public enum ErrorCode {
    QUEUE_ALREADY_REGISTERED_USER(HttpStatus.CONFLICT, "UQ-001", "Already registered user");

    private final HttpStatus httpStatus;
    private final String code;
    private final String reason;

    public ApplicationException build(){
        return new ApplicationException(httpStatus, code, reason);
    }

    public ApplicationException build(Object ...args){
        return new ApplicationException(httpStatus, code, reason.formatted(args));
    }
}
