package com.justasem.personsrelatives.web;

public enum Relative {
    HUSBAND("vyras"),
    WIFE("žmona"),
    SISTER("sesuo"),
    BROTHER("brolis"),
    MOTHER("mama"),
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
