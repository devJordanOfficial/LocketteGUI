package com.infamousgc.lockette.Utils;

import net.md_5.bungee.api.ChatColor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Formatter {
    private static final Pattern PLACEHOLDER_PATTERN = Pattern.compile("\\{(\\d+)}");
    private static final Pattern HEX_COLOR_PATTERN = Pattern.compile("#[a-fA-F0-9]{6}");
    private static final Pattern FORMAT_CODE_PATTERN = Pattern.compile("&[klmno]");

    public static final String PREFIX_GENERAL = "&8[&5»&8] ";
    public static final String PREFIX_ERROR = "&8[&4»&8] ";
    public static final String PREFIX_INFO = "&8[&e»&8] ";

    public static String format(String msg, Object... args) {
        String parsed = parseVariables(msg, args);
        parsed = replaceHexColors(parsed);
        return ChatColor.translateAlternateColorCodes('&', parsed);
    }

    public static String capitalize(String text) {
        return Character.toUpperCase(text.charAt(0)) + text.substring(1);
    }

    private static String parseVariables(String msg, Object... args) {
        if (args.length > 0) {
            for (int i = 0; i < args.length; i++) {
                msg = msg.replace("{" + i + "}", String.valueOf(args[i]));
            }
        }
        return msg;
    }

    private static String replaceHexColors(String input) {
        StringBuilder result = new StringBuilder();
        Matcher matcher = HEX_COLOR_PATTERN.matcher(input);
        while (matcher.find()) {
            matcher.appendReplacement(result, ChatColor.of(matcher.group()) + "");
        }
        matcher.appendTail(result);
        return result.toString();
    }
}
