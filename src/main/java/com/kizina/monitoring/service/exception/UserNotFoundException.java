package com.kizina.monitoring.service.exception;

import lombok.Getter;

@Getter
public class UserNotFoundException extends RuntimeException {

    private final Long userId;

    public UserNotFoundException(Long userId) {
        super();
        this.userId = userId;
    }
}
