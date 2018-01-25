package de.zebrajaeger.opencms.resourceplugin.namingstrategy;

public class LowerCaseWithHyphenNamingStrategy extends AbstractNamingStrategy {
    public LowerCaseWithHyphenNamingStrategy(String name) {
        super(name);
    }

    @Override
    protected String convert(String name) {
        StringBuilder sb = new StringBuilder();
        for (char c : name.toCharArray()) {
            if (Character.isUpperCase(c)) {
                sb.append("-");
                sb.append(Character.toLowerCase(c));
            } else if (c == '_') {
                sb.append("-");
            } else {
                sb.append(c);
            }
        }

        // remove multible hyphens
        String result = sb.toString();
        result = result.replaceAll("-{2,}", "-");
        while (result.startsWith("-")) {
            result = result.substring(1);
        }
        while (result.endsWith("-")) {
            result = result.substring(0, result.length() - 1);
        }

        return result;
    }
}
