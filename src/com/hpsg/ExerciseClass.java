package com.hpsg;

import java.util.Scanner;

public class ExerciseClass {
    private static final String inputString = "#(Value:\"Analyzer PKW100\"|Name:\"DeviceType\"(Value:004|Ref:5-10|Name:\"QC\"))(Value:\"New Test\"|Name:\"Testtype\"(Value:\"HB results\"|Name:\"Analysistype\")(Value:\"12.2\"|Name:\"HB\"(Value:13|Ref:8.1-10.5|Name:\"HB-Sub1\")))&";

    public static void main(final String[] args) {
        final Scanner scanner = new Scanner(System.in);

        System.out.println("Please enter serialized String:");
        final String userInputString = scanner.nextLine();

        final Deserializer deserializer = new Deserializer(userInputString);
        final Entity entity = new Entity(userInputString);

        System.out.println();
        System.out.println();
        System.out.println();

        System.out.println("Method One (Deserializer Class):");
        System.out.println(deserializer);
        System.out.println();
        System.out.println(deserializer.serialize());

        System.out.println();
        System.out.println();
        System.out.println();

        System.out.println("Method Two (Entity Class):");
        System.out.println(entity.formatOutput(0));
        System.out.println();
        System.out.println(entity.serialize(true));
    }
}
