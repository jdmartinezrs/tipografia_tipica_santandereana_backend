package com.miproyecto.backend.model;

public enum CategoriaTipografia {
    SERIF("Serif"),
    SANS_SERIF("Sans Serif"),
    SCRIPT("Script"),
    DISPLAY("Display"),
    MONOSPACE("Monospace");

    private final String displayName;

    CategoriaTipografia(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static CategoriaTipografia fromDisplayName(String displayName) {
        for (CategoriaTipografia categoria : values()) {
            if (categoria.displayName.equals(displayName)) {
                return categoria;
            }
        }
        throw new IllegalArgumentException("No enum constant for display name: " + displayName);
    }

    @Override
    public String toString() {
        return displayName;
    }
}
