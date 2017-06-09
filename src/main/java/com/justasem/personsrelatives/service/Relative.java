package com.justasem.personsrelatives.service;

public enum Relative {
    HUSBAND("vyras"),
    WIFE("žmona"),
    SISTER("sesuo"),
    BROTHER("brolis"),
    MOTHER("motina"),
    FATHER("tėvas"),
    SON("sūnus"),
    DAUGHTER("dukra"),
    GRANDFATHER("senelis"),
    GRANDMOTHER("senelė"),
    GRANDSON("anūkas"),
    GRANDDAUGHTER("anūkė");

    private final String relativeType;

    Relative(String relativeType) {
        this.relativeType = relativeType;
    }

    public String getRelativeType() { return relativeType; }
}
