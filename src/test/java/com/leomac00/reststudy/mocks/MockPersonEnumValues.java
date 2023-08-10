package com.leomac00.reststudy.mocks;

public enum MockPersonEnumValues {
    FIRST_NAME("First Name Test"),
    LAST_NAME("Last Name Test"),
    ADDRESS("Addres Test"),
    GENDER_MALE("Male"),
    GENDER_FEMALE("Female");

    public final String value;

    MockPersonEnumValues(String value) {

        this.value = value;
    }

    public String getMockValue(int id) {
        return this.value + id;
    }
}
