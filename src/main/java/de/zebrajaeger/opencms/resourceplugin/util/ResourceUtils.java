package de.zebrajaeger.opencms.resourceplugin.util;

/**
 * Created by lars on 11.02.2017.
 */
public class ResourceUtils {

    private ResourceUtils() {

    }

    public static String toResourceName(String name) {
        StringBuilder sb = new StringBuilder();

        boolean firstChar = true;

        for (char c : name.toCharArray()) {
            if (firstChar) {
                firstChar = false;
            } else {
                if (Character.isUpperCase(c)) {
                    sb.append("-");
                }
            }
            sb.append(Character.toLowerCase(c));
        }
        return sb.toString();
    }
}
