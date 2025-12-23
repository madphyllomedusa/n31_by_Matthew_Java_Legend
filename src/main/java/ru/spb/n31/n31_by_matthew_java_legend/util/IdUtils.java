package ru.spb.n31.n31_by_matthew_java_legend.util;

import java.math.BigInteger;
import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

public final class IdUtils {

    private IdUtils() {
    }

    public static String nullIfBlank(String s) {
        return (s == null || s.isBlank()) ? null : s;
    }

    public static boolean isNumeric(String s) {
        if (s == null || s.isBlank()) return false;
        for (int i = 0; i < s.length(); i++) {
            if (!Character.isDigit(s.charAt(i))) return false;
        }
        return true;
    }

    private static Optional<BigInteger> tryParseNumeric(String s) {
        if (!isNumeric(s)) return Optional.empty();
        try {
            return Optional.of(new BigInteger(s));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    /**
     * Comparator that sorts numeric string ids by numeric value (so "2" < "10"),
     * otherwise falls back to natural string ordering. Nulls are last.
     */
    public static Comparator<String> numericStringComparator() {
        return Comparator.nullsLast((a, b) -> {
            if (Objects.equals(a, b)) return 0;
            var na = tryParseNumeric(a);
            var nb = tryParseNumeric(b);
            if (na.isPresent() && nb.isPresent()) {
                return na.get().compareTo(nb.get());
            }
            if (na.isPresent()) return -1;
            if (nb.isPresent()) return 1;
            return a.compareTo(b);
        });
    }

    /**
     * Generates next numeric string id: max(existingNumericIds) + 1.
     * If there are no numeric ids, returns "1".
     */
    public static String nextNumericStringId(Stream<String> existingIds) {
        BigInteger max = existingIds
                .filter(Objects::nonNull)
                .map(IdUtils::tryParseNumeric)
                .flatMap(Optional::stream)
                .max(BigInteger::compareTo)
                .orElse(BigInteger.ZERO);
        return max.add(BigInteger.ONE).toString();
    }

    /**
     * Generates next Long id: max(existingIds) + 1.
     * If there are no ids, returns 1.
     */
    public static Long nextLongId(Stream<Long> existingIds) {
        long max = existingIds
                .filter(Objects::nonNull)
                .mapToLong(Long::longValue)
                .max()
                .orElse(0L);
        return max + 1L;
    }
}


