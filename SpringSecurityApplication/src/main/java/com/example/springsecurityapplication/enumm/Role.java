package com.example.springsecurityapplication.enumm;

public enum Role {
    Admin("ROLE_ADMIN"),
    User("ROLE_USER");

    private String value;
    private Role(String value) {
        this.value = value;
    }
    public String getValue() {
        return value;
    }
}
