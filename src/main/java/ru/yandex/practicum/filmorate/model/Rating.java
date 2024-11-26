package ru.yandex.practicum.filmorate.model;

public enum Rating {
    G("G"),
    PG("PG"),
    PG_13("PG-13"),
    R("R"),
    NC_17("NC-17");

    private final String displayName;

    Rating(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static Rating fromDisplayName(String displayName) {
        for (Rating rating : Rating.values()) {
            if (rating.displayName.equalsIgnoreCase(displayName)) {
                return rating;
            }
        }
        throw new IllegalArgumentException("No enum constant with display name " + displayName);
    }
}
