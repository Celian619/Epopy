package net.epopy.network.utils;

public enum EnvType {

    DEV("dev"),
    BETA("beta"),
    PROD("prod");

    private String name;

    private EnvType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
