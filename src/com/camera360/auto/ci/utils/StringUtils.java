package com.camera360.auto.ci.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by tangsong on 12/19/14.
 */
public class StringUtils {

    public static boolean isNumeric(String str){
        Pattern pattern = Pattern.compile("(?:[0-9]+[\\-|\\_]{1})+[0-9]+");
        Matcher isNum = pattern.matcher(str);
        return isNum.matches();
    }
}
