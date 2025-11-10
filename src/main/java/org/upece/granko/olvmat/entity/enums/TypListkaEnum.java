package org.upece.granko.olvmat.entity.enums;

public enum TypListkaEnum {
    STUDENT("Študent"), NESTUDENT("Neštudent"), DOBROVOLNIK("Dobrovoľník"), TEAM("Team");

    private final String value;

    TypListkaEnum(String v) {
        value = v;
    }

    @Override
    public String toString() {
        return value;
    }
}
