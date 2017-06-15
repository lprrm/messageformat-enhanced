package org.lprrm.format.enabler;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class is still WIP.
 */
public class PluralFormatter {

    public String pluralSupport(String text, Object[] placeHolders) throws Exception {
        Matcher matchPlurals = Pattern.compile(",[ ]*?plural[ ]*?,").matcher(text);
        String formattedText = text;
        for (int z = 0; matchPlurals.find(); z++) {
            int length = text.length();
            int matchBeginsAt = matchPlurals.start();
            int startsAt = 0;
            for (int i = matchBeginsAt; i > 0; i--) {
                char valueAtIndex = text.charAt(i);
                if (valueAtIndex == '{') {
                    startsAt = i;
                    break;
                }
            }
            boolean child = false;
            int endsAt = length;
            int matchEndsAt = matchPlurals.end();
            for (int i = matchEndsAt; i < length; i++) {
                char valueAtIndex = text.charAt(i);
                if (valueAtIndex == '{') {
                    child = true;
                } else if (valueAtIndex == '}' && child) {
                    child = false;
                } else if (valueAtIndex == '}' && !child) {
                    child = false;
                    endsAt = i;
                    break;
                }
            }
            String pluralText = text.substring(startsAt + 1, endsAt);
            String textToReplce = processPlurals(pluralText, placeHolders);
            formattedText = formattedText.replace("{" + pluralText + "}", textToReplce);
        }
        return formattedText;
    }

    private String processPlurals(String text, Object[] placeHolders) throws Exception {
        String value = "";
        String[] pluralConfig = text.split(",");
        String condition = pluralConfig[0];
        int actualNumber = (int) placeHolders[Integer.valueOf(condition)];
        Matcher tokenizer = Pattern.compile("(((>=|<=|=|>|<|!=|=)[0-9])|other)\\s\\{(.*?)\\}").matcher(pluralConfig[2]);
        boolean matchfound = false;
        String other = "";
        while (tokenizer.find()) {
            String option = tokenizer.group();
            String operator = extractOperator(option);
            String expectedNumber = "";
            if (!"other".equals(operator)) {
                expectedNumber = getNumber(option);
                if (evaluateCondition(actualNumber, operator, expectedNumber)) {
                    value = extractTextToReplace(option);
                    matchfound = true;
                    break;
                }
            } else if ("other".equals(operator)) {
                other = extractTextToReplace(option);
            }
        }
        if (!matchfound)
            value = other;

        if (value.contains("# ")) {
            value = value.replace("#", actualNumber + "");
        }
        return value;
    }

    private boolean evaluateCondition(double actual, String operator, String expected) {
        boolean use = true;
        double expectedNumber = Double.valueOf(expected.replaceAll(",", ""));
        if ("=".equals(operator)) {
            use = (actual == expectedNumber);
        } else if ("!=".equals(operator)) {
            use = (actual != expectedNumber);
        } else if (">=".equals(operator)) {
            use = (actual >= expectedNumber);
        } else if (">".equals(operator)) {
            use = (actual > expectedNumber);
        } else if ("<=".equals(operator)) {
            use = (actual <= expectedNumber);
        } else if ("<".equals(operator)) {
            use = (actual < expectedNumber);
        }
        return use;
    }

    private String getNumber(String token) throws Exception {
        Matcher operatorMatcher = Pattern.compile("((>=|<=|=|>|<|!=)|other)").matcher(token);
        int start = 0;
        StringBuilder number = new StringBuilder();
        if (operatorMatcher.find()) {
            start = operatorMatcher.end();
            for (int i = start; i < token.length(); i++) {
                String charAtIndex = token.charAt(i) + "";
                if ("1234567890,.".contains(charAtIndex)) {
                    number.append(charAtIndex);
                } else {
                    break;
                }
            }
        } else {
            throw new Exception(token);
        }
        return number.toString();
    }

    private String extractOperator(String token) throws Exception {
        Matcher operatorMatcher = Pattern.compile("((>=|<=|=|>|<|!=)|other)").matcher(token);
        if (operatorMatcher.find()) {
            return operatorMatcher.group();
        } else {
            throw new Exception(token);
        }
    }

    private String extractTextToReplace(String token) throws Exception {
        Matcher operatorMatcher = Pattern.compile("\\{(.*?)\\}").matcher(token);
        if (operatorMatcher.find()) {
            return operatorMatcher.group(1);
        } else {
            throw new Exception(token);
        }
    }


}
