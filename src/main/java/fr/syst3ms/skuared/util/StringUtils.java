package fr.syst3ms.skuared.util;

import com.google.common.base.Objects;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
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
				Double.parseDouble(n);
				return true;
			} catch (NumberFormatException e1) {
				return false;
			}
		}
	}

	@SuppressWarnings("UnnecessaryLocalVariable")
	public static Number parseNumber(@NotNull String n) {
		assert isNumeric(n);
		try {
			long l = Long.parseLong(n);
			return l;
		} catch (NumberFormatException e) {
			try {
				double d = Double.parseDouble(n);
				return d;
			} catch (NumberFormatException e1) {
				return Double.NaN;
			}
		}
	}

	public static String toString(Number n) {
		if (n instanceof Float || n instanceof Double) {
			return Double.toString(n.doubleValue());
		} else {
			return Long.toString(n.longValue());
		}
	}

	public static Number parseHex(@NotNull String hex) {
		try {
			return Long.parseLong(hex, 16);
		} catch (NumberFormatException e) {
			return Double.NaN;
		}
	}

	public static Number parseBin(@NotNull String bin) {
		try {
			return Long.parseLong(bin, 2);
		} catch (NumberFormatException e) {
			return Double.NaN;
		}
	}

	public static Number parseOctal(@NotNull String oct) {
		try {
			return Long.parseLong(oct, 8);
		} catch (NumberFormatException e) {
			return Double.NaN;
		}
	}

	public static String toString(@NotNull Map<?, ?> map) {
		StringBuilder sb = new StringBuilder();
		for (Map.Entry<?, ?> e : map.entrySet()) {
			sb.append(e.getKey()).append(" -> ").append(e.getValue()).append("\n");
		}
		return sb.toString();
	}

	public static int findClosing(@NotNull String text, char open, char close, int openPos) {
		int closePos = openPos;
		int counter = 1;
		while (counter > 0) {
			if (closePos + 1 == text.length())
				return -1;
			char c = text.charAt(++closePos);
			if (c == open) {
				counter++;
			}
			else if (c == close) {
				counter--;
			}
		}
		return closePos;
	}
}