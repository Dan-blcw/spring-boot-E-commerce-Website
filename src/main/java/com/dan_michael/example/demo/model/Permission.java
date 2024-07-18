package com.dan_michael.example.demo.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Permission {

    ADMIN_READ("admin:read"),
    ADMIN_UPDATE("admin:update"),
    ADMIN_CREATE("admin:create"),
    ADMIN_DELETE("admin:delete"),


    MANAGE_READ("manage:read"),
    MANAGE_UPDATE("manage:update"),
    MANAGE_CREATE("manage:create"),
    MANAGE_DELETE("manage:delete"),

    ;
    @Getter
    private final String permission;
}
