package calculator;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static calculator.Calculation.CALCULATION_REGEX;
import static calculator.VarAssigner.*;


public class Main {
    private static final String HELP = "The program calculates the sum of numbers.\n " +
            "Note that even number of minuses gives plus, odd gives minus:\n" +
            "   e.g 2 -- 2 equals 2 - (-2) equals 2 + 2.\n" +
            "Calculator support variables and numbers.\n" +
            "Assigning value pattern:\n" +
            "  <variable> = <number/existingVariable> e.g count = 10, a = 4, b=a, c= b.\n" +
            "It is possible to set new value to existing variable in any moment:\n" +
            "  <existingVariable> = <newValue> \n" +
            "To print the value of variable simply type its name.\n" +
            "Operation examples:\n" +
            "a + b - c,  a - 5, c -4 +6 -- 4, ";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        VarAssigner varAssigner = new VarAssigner();
        Calculation calculation = new Calculation(varAssigner);

        final String regex = String.format("%s|%s|%s|%s", VAR_ASSIGNMENT_REGEX,
                INVALID_IDENTIFIER_REGEX, INVALID_ASSIGNMENT_REGEX, CALCULATION_REGEX);
        Pattern pattern = Pattern.compile(regex);

        String input;
        do {
            input = scanner.nextLine();
            Matcher matcher = pattern.matcher(input);
            if (matcher.find()) {
                varAssigner.checkIsAssignmentCorrect(matcher);
                if (varAssigner.isAssignment(matcher)) {
                    varAssigner.mapAssignment(matcher);
                } else if (calculation.isCalculation(matcher)) {
                    calculation.sum(input);
                }
            } else
                switch (input) {
                    case "/exit":
                        System.out.print("Bye!");
                        break;
                    case "/help":
                        System.out.println(HELP);
                        break;
                    case "":
                        break;
                    default:
                        System.out.println("Unknown command");
                        break;
                }
        } while (!input.equals("/exit"));
    }
}

