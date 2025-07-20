package pl.osetoctet.common.utils;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public final class OffsetDateTimeUtils {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");

    private OffsetDateTimeUtils() {
    }

    public static String getCurrentUtcTimestamp() {
        return OffsetDateTime.now().withOffsetSameInstant(ZoneOffset.UTC).format(DATE_TIME_FORMATTER);
    }

}
