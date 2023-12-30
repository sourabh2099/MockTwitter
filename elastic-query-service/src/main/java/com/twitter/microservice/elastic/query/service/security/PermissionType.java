package com.twitter.microservice.elastic.query.service.security;

public enum PermissionType {
    READ("read"), WRITE("write"), ADMIN("admin");
    private String type;

    PermissionType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
