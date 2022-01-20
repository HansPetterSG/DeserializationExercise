package com.hpsg;

import java.util.ArrayList;

public class Deserializer {
    private ArrayList<Object> obj;

    public Deserializer(final String inputString) {
        if (inputString.startsWith("#") && inputString.endsWith("&")) {
            obj = deserialize(inputString.replace("#", "").replace("&", ""));
        }
    }

    @Override
    public String toString() {
        return arrayListToString(obj, 0);
    }

    private String arrayListToString(final ArrayList<Object> list, final int indentation) {
        final StringBuilder returnString = new StringBuilder();

        returnString.append("        ".repeat(Math.max(0, ((indentation * 2) - 3))));

        if (indentation > 1) {
            returnString.append("--------");
        }

        boolean firstEntry = true;

        for (final Object entry : list) {
            if (entry.getClass() == ArrayList.class) {
                returnString.append("\n").append(arrayListToString((ArrayList<Object>) entry, (indentation + 1)));
            } else {
                if (firstEntry) {
                    firstEntry = false;
                } else {
                    returnString.append(", ");
                }

                returnString.append(entry);
            }
        }

        return returnString.toString().replace("\"", "");
    }

    private ArrayList<Object> deserialize(final String input) {
        final ArrayList<Object> elements = new ArrayList<>();

        String parseString = input;

        while (!parseString.isEmpty()) {
            if (parseString.startsWith("(")) {
                String parenthesesContent = getOutermostParentheses(parseString);

                parseString = parseString.substring(parenthesesContent.length());

                parenthesesContent = parenthesesContent.substring(1, (parenthesesContent.length() - 1));

                elements.add(deserialize(parenthesesContent));
            } else {
                if (parseString.startsWith("|")) {
                    parseString = parseString.substring(1);
                }

                int count = 0;

                while (count < parseString.length() &&
                       !parseString.substring(0, (count + 1)).endsWith("|") &&
                       !parseString.substring(0, (count + 1)).endsWith("(")) {
                    count++;
                }

                final String keyValuePairString = parseString.substring(0, count);

                parseString = parseString.substring(count);

                final String[] keyValuePair = keyValuePairString.split(":");

                final String newPairKey = keyValuePair[0];
                final String newPairValueString = keyValuePair[1];

                if (newPairValueString.contains("\"")) {
                    final String newPairValue = newPairValueString.replace("\"", "");

                    elements.add(new Pair(newPairKey, newPairValue));
                } else if (newPairValueString.contains("-")) {
                    final String[] refStringValues = newPairValueString.split("-");

                    final Ref newRef = new Ref(Float.parseFloat(refStringValues[0]),
                                               Float.parseFloat(refStringValues[1]));

                    elements.add(new Pair(newPairKey, newRef));
                } else {
                    elements.add(new Pair(newPairKey, Integer.parseInt(newPairValueString)));
                }
            }
        }

        return elements;
    }

    public String serialize() {
        return "#" + serializeArrayList(obj) + "&";
    }

    public String serializeArrayList(final ArrayList<Object> list) {
        final StringBuilder serialized = new StringBuilder();

        for (final Object element : list) {
            if (element.getClass() == ArrayList.class) {
                serialized.append("(").append(serializeArrayList((ArrayList<Object>) element)).append(")");
            } else {
                if (serialized.length() > 0) {
                    serialized.append("|");
                }

                serialized.append(element);
            }
        }

        return serialized.toString().replace(": ", ":");
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
}
