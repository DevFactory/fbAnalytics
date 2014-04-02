package net.mormolhs.facebook.fbanalytics.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by toikonomakos on 3/31/14.
 */
public class JSonParser {

    public static String getValueFromJsonBasedOnKey(String JSonString, String key) {
        String value = null;
        Pattern pattern = Pattern.compile("\"" + key + "\" : (.*?),");
        Matcher matcher = pattern.matcher(JSonString);
        if (matcher.find()) {
            value = matcher.group(1);
        }
        return value;
    }

    public static String getValueFromJson(String json, String key) {
        json = json.replaceAll(" ", "").replace("\n", "").replace("\r", "");
        Pattern pattern = Pattern.compile("\"" + key + "\":(.*),\"");
        Matcher matcher = pattern.matcher(json);
        if (matcher.find()) {
//            /System.out.println(matcher.group(1).split(",")[0]);
            return matcher.group(1).split(",")[0];
        } else {
            return null;
        }
    }

}
