package fr.syst3ms.skuared.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class StringUtils {
    public static List<String> getAllMatches(String s, String regex) {
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

    public static boolean isNumber(String n) {
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
}
