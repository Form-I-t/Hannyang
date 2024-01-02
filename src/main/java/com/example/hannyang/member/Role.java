package com.example.hannyang.member;

public enum Role {
    ROLE_MEMBER("MEMBER"),
    ROLE_ADMIN("ADMIN");

    //"MEMBER" or "ADMIN"
    private final String value;

    Role(String value) {
        this.value = value;
    }

    // GetValue
    public String getValue() {
        return this.value;
    }
}
