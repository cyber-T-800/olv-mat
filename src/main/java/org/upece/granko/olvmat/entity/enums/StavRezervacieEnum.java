package org.upece.granko.olvmat.entity.enums;

public enum StavRezervacieEnum {
    REZERVOVANY("Rezervovaný"), ZAPLATENY("Zaplatený"), ZRUSENY("Zrušený"), POUZITY("Použitý");

    private final String value;

    StavRezervacieEnum(String v) {
        value = v;
    }

    @Override
    public String toString() {
        return value;
    }
}
