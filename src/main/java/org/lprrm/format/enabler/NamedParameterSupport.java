package org.lprrm.format.enabler;

import java.text.MessageFormat;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NamedParameterSupport {

    public static String format(String text, Map<String, Object> placeHolderMap) throws Exception {
        Object[] placeHolders = new Object[placeHolderMap.size()];
        int position = 0;
        Iterator<String> keyIterator = placeHolderMap.keySet().iterator();
        while(keyIterator.hasNext()){
            String key = keyIterator.next();
            placeHolders[position] = placeHolderMap.get(key);
            Matcher patternMatcher = Pattern.compile("\\{" + key).matcher(text);
            text = patternMatcher.replaceAll("{" + position);
            position++;
        }
        return MessageFormat.format(text,placeHolders);
    }


}
