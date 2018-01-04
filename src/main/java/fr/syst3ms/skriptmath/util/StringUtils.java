package fr.syst3ms.skriptmath.util;

import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class StringUtils {
    @NotNull
    public static List<String> getAllMatches(@NotNull String s, @NotNull String regex) {
        List<String> matches = new ArrayList<>();
        Pattern pat;
        try {
            pat = Pattern.compile(regex);
        } catch (PatternSyntaxException e) {
            return matches;
        }
        Matcher m = pat.matcher(s);
        while (m.find()) {
            matches.add(m.group());
        }
        return matches;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static boolean isNumeric(@NotNull String n) {
        try {
            Long.parseLong(n);
            return true;
        } catch (NumberFormatException e) {
            try {
                new BigInteger(n);
                return true;
            } catch (NumberFormatException e2) {
                try {
                    Double.parseDouble(n);
                    return true;
                } catch (NumberFormatException e1) {
                    try {
                        new BigDecimal(n);
                        return true;
                    } catch (NumberFormatException e3) {
                        return false;
                    }
                }
            }
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static boolean isInteger(@NotNull String s) {
        try {
            Long.parseLong(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static Number parseNumber(@NotNull String n) {
        assert isNumeric(n);
        try {
            return Long.parseLong(n);
        } catch (NumberFormatException e) {
            try {
                return Double.parseDouble(n);
            } catch (NumberFormatException e1) {
                try {
                    return new BigInteger(n);
                } catch (NumberFormatException e2) {
                    try {
                        return new BigDecimal(n);
                    } catch (NumberFormatException e3) {
                        return Double.NaN;
                    }
                }
            }
        }
    }

    public static String toString(Number n) {
        if (n instanceof Float || n instanceof Double) {
            return Double.toString(n.doubleValue());
        } else if (n instanceof BigInteger) {
            return ((BigInteger) n).toString(10);
        } else if (n instanceof BigDecimal) {
            return ((BigDecimal) n).toPlainString();
        } else {
            return Long.toString(n.longValue());
        }
    }

    public static Number parseHex(@NotNull String hex) {
        try {
            return Long.parseLong(hex, 16);
        } catch (NumberFormatException e) {
            try {
                return new BigInteger(hex, 16);
            } catch (NumberFormatException e1) {
                return Double.NaN;
            }
        }
    }

    public static Number parseBin(@NotNull String bin) {
        try {
            return Long.parseLong(bin, 2);
        } catch (NumberFormatException e) {
            try {
                return new BigInteger(bin, 2);
            } catch (NumberFormatException e1) {
                return Double.NaN;
            }
        }
    }

    public static Number parseOctal(@NotNull String oct) {
        try {
            return Long.parseLong(oct, 8);
        } catch (NumberFormatException e) {
            try {
                return new BigInteger(oct, 8);
            } catch (NumberFormatException e1) {
                return Double.NaN;
            }
        }
    }

    public static String join(String delimiter, Iterable<String> strings) {
        StringJoiner joiner = new StringJoiner(delimiter);
        for (String s : strings) {
            joiner.add(s);
        }
        return joiner.toString();
    }
}
