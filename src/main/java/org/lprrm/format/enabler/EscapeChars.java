package org.lprrm.format.enabler;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EscapeChars {

    public static String escapeApostrophe(String text) {
        Matcher aposReplacer = Pattern.compile("'").matcher(text);
        return aposReplacer.replaceAll("''");
    }

}
