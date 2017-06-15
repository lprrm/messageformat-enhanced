package org.lprrm.format;

import org.lprrm.format.api.FormatMessage;
import org.lprrm.format.enabler.EscapeChars;
import org.lprrm.format.enabler.NamedParameterSupport;

import java.util.Map;

public class FormatMessageImpl implements FormatMessage {


    public String formatMessage(String text, Map<String, Object> placeHolderMap) throws Exception {
        text = EscapeChars.escapeApostrophe(text);
        return NamedParameterSupport.format(text,placeHolderMap);
    }

}
