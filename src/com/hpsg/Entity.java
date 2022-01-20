package com.hpsg;

import java.util.ArrayList;

public class Entity {
    private final Object value;
    private final Object name;
    private final Object ref;
    private final ArrayList<Entity> entities;

    public Entity(final String serializedString) {
        String parseString = serializedString.replace("#", "").replace("&", "");

        entities = new ArrayList<>();

        while (parseString.contains("(")) {
            final int parenthesesStart = parseString.indexOf("(");

            final String parenthesesContent = getOutermostParentheses(parseString.substring(parenthesesStart));

            parseString = parseString.substring(0, parenthesesStart) +
                          parseString.substring((parenthesesStart + parenthesesContent.length()));

            entities.add(new Entity(parenthesesContent.substring(1, (parenthesesContent.length() - 1))));
        }

        value = parseString(extractValue("Value:", parseString));
        name = parseString(extractValue("Name:", parseString));
        ref = parseString(extractValue("Ref:", parseString));
    }

    private String extractValue(final String key, final String serializedString) {
        if (serializedString.contains(key)) {
            for (int i = 0; i < serializedString.length(); i++) {
                if (serializedString.substring(i).startsWith(key)) {
                    return serializedString.substring(i + key.length()).split("[|(]")[0];
                }
            }
        }

        return null;
    }

    public String formatOutput(final int indentation) {
        final StringBuilder returnString = new StringBuilder("        ".repeat(Math.max(0, ((indentation * 2) - 3))));

        if (indentation > 1) {
                returnString.append("--------");
        }

        if (!(value == null)) {
            returnString.append("Value: ").append(value);
        }

        if (!(name == null)) {
            if (returnString.length() > 0) {
                returnString.append(", ");
            }

            returnString.append("Name: ").append(name);
        }

        if (!(ref == null)) {
            if (returnString.length() > 0) {
                returnString.append(", ");
            }

            returnString.append("Ref: ").append(ref);
        }

        for (final Entity entity : entities) {
            returnString.append("\n").append(entity.formatOutput(indentation + 1));
        }

        return returnString.toString();
    }

    public String serialize(final boolean outermost) {
        String returnString = "";

        if (!(value == null)) {
            if (value.getClass() == String.class) {
                returnString += "Value:" + "\"" + value + "\"";
            } else {
                returnString += "Value:" + value;
            }
        }

        if (!(name == null)) {
            if (!returnString.isEmpty()) {
                returnString += "|";
            }

            if (name.getClass() == String.class) {
                returnString += "Name:" + "\"" + name + "\"";
            } else {
                returnString += "Name:" + name;
            }
        }

        if (!(ref == null)) {
            if (!returnString.isEmpty()) {
                returnString += "|";
            }

            if (ref.getClass() == String.class) {
                returnString += "Ref:" + "\"" + ref + "\"";
            } else {
                returnString += "Ref:" + ref;
            }
        }

        for (final Entity entity : entities) {
            returnString += "(" + entity.serialize(false) + ")";
        }

        if (outermost) {
            returnString = "#" + returnString + "&";
        }

        return returnString;
    }

    private String getOutermostParentheses(final String input) {
        int count = 1;

        while (stringCharCount(")".charAt(0), input.substring(0, count)) !=
               stringCharCount("(".charAt(0), input.substring(0, count))) {
            count++;
        }

        return input.substring(0, count);
    }

    private int stringCharCount(final char character, final String string) {
        int count = 0;

        for (int i = 0; i < string.length(); i++) {
            if (string.charAt(i) == character) {
                count++;
            }
        }

        return count;
    }

    private Object parseString(final String string) {
        if (string == null || string.equals("")) {
            return null;
        } else if (string.contains("\"")) {
            return string.replace("\"", "");
        } else if (string.contains("-")) {
            final String[] splitString = string.split("-");

            return new Ref(Float.parseFloat(splitString[0]), Float.parseFloat(splitString[1]));
        } else {
            return Integer.parseInt(string);
        }
    }
}
