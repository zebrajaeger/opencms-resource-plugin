package de.zebrajaeger.opencms.resourceplugin.namingstrategy;

import org.apache.commons.lang3.StringUtils;

import java.util.LinkedList;
import java.util.List;

public class CamelCaseNamingStrategy extends AbstractNamingStrategy {
    public CamelCaseNamingStrategy(String name) {
        super(name);
    }

    @Override
    protected String convert(String name) {
        List<String> tokens = new LinkedList<>();

        StringBuilder sb = new StringBuilder();
        for (char c : name.toCharArray()) {
            if (c == '-' || c == '_') {
                tokens.add(sb.toString());
                sb.setLength(0);
            } else if (Character.isUpperCase(c)) {
                tokens.add(sb.toString());
                sb.setLength(0);
                sb.append(c);
            } else {
                sb.append(c);
            }
        }
        tokens.add(sb.toString());

        StringBuilder result = new StringBuilder();
        for (String token : tokens) {
            if (StringUtils.isNotBlank(token)) {
                char[] chars = token.toCharArray();
                chars[0] = Character.toUpperCase(chars[0]);
                result.append(chars);
            }
        }

        return result.toString();
    }
}
