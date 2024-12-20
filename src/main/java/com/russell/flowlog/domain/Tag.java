package com.russell.flowlog.domain;

import javax.annotation.Nonnull;
import java.util.Locale;
import java.util.Objects;

/**
 * Represents a tag.<br>
 */
public class Tag {

    public static final Tag UNKNOWN = Tag.of("Unknown");

    /** Original value */
    private final String tag;

    /** Intentional trade-off: more memory in favor of performance */
    private final String sanitizedValue;

    /** Constructor */
    private Tag(@Nonnull final String tag) {
        this.tag = tag;
        this.sanitizedValue = tag.toLowerCase(Locale.ROOT);
    }

    public String getTag() {
        return tag;
    }

    public String getSanitizedValue() {
        return sanitizedValue;
    }

    /** Static constructor method */
    public static Tag of(@Nonnull final String tag) {
        return new Tag(tag);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Tag tag1 = (Tag) o;
        return Objects.equals(sanitizedValue, tag1.sanitizedValue);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(sanitizedValue);
    }
}
