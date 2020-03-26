package calculator;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static calculator.Calculation.CALCULATION_REGEX;
import static calculator.VarAssigner.*;


public class Main {

    public static void main(String[] args) throws FileNotFoundException {
        Scanner scanner = new Scanner(System.in);
        VarAssigner varAssigner = new VarAssigner();
        Postfix postfix = new Postfix();
        Calculation calculation = new Calculation(varAssigner, postfix);

        final String regex = String.format("%s|%s|%s|%s", VAR_ASSIGNMENT_REGEX,
                INVALID_IDENTIFIER_REGEX, INVALID_ASSIGNMENT_REGEX, CALCULATION_REGEX);
        Pattern pattern = Pattern.compile(regex);

        String input;
        do {
            input = scanner.nextLine();
            Matcher matcher = pattern.matcher(input);
            if (matcher.find()) {
                varAssigner.isAssignmentCorrect(matcher);
                if (varAssigner.isAssignment(matcher)) {
                    varAssigner.mapAssignment(matcher);
                } else if (calculation.isCalculation(matcher)) {
                    postfix.convertToPostfix(input);
                    calculateOrResetStack(postfix, calculation);
                }
            } else
                switch (input) {
                    case "/exit":
                        System.out.print("Bye!");
                        break;
                    case "/help":
                        importHelp();
                        break;
                    case "":
                        break;
                    default:
                        System.out.println("Unknown command");
                        break;
                }
        } while (!input.equals("/exit"));
    }

    private static void calculateOrResetStack(Postfix postfix, Calculation calculation) {
        if (postfix.isExpressionValid()) {
            calculation.calculateResult();
        } else {
            System.out.println("Invalid expression");
            postfix.resetStack();
            /* call stack.clear() -- otherwise values will remain on stack
                        and will be used at the next conversion  */
        }
    }

    private static void importHelp() throws FileNotFoundException {
        StringBuilder sb = new StringBuilder();
        Scanner sc = new Scanner(new File("./help.txt"));
        while (sc.hasNextLine()) {
            System.out.println(sc.nextLine());
        }
    }
}

