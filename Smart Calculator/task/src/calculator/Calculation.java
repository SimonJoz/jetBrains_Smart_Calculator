package calculator;

import java.math.BigInteger;
import java.util.Deque;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static calculator.Constance.*;
import static java.lang.String.format;

public class Calculation {

    public static final String CALCULATION_REGEX = format("%s%s%s",
            "^(?![/*^])(\\s*((?<![a-zA-Z])\\d+\\s*(?![a-zA-Z])|(?<!\\d)[a-zA-Z]+\\s*(?!\\d)",
            "|[-]+\\s*(?!\\W-[()])|[+]+\\s*(?!\\W-[()])|[*]+\\s*(?!\\W-[()])|",
            "[/]+\\s*(?!\\W-[()])|\\^+\\s*(?!\\W-[()])|[()]+\\s*))+$");

    private final VarAssigner assigner;
    private final Postfix postfix;

    private final String regex = "([-+*/^]\\s+)|([-]?\\d+)|([a-zA-Z]+)";
    private final Pattern pattern = Pattern.compile(regex);

    public Calculation(VarAssigner assigner, Postfix postfix) {
        this.assigner = assigner;
        this.postfix = postfix;
    }

    public void calculateResult() {
        Map<String, String> map = assigner.getVariables();
        Deque<String> stack = postfix.getStack();
        String postfixResult = postfix.getPostfixResult();
        Matcher matcher = pattern.matcher(postfixResult);
        BigInteger result = BigInteger.ZERO;
        boolean unknown = false;
        while (matcher.find()) {
            String operator = matcher.group(1);
            String number = matcher.group(2);
            String var = matcher.group(3);
            if (operator != null) {
                operator = operator.trim(); // regex catch operators with space -- "- ","* "
                result = performOperationDependOnOperator(operator, stack);
                if (result == null) {
                    stack.clear();
                    break;
                }
                String value = String.valueOf(result);
                stack.push(value);
            }
            if (number != null) {
                stack.push(number);
            }
            if (var != null && map.containsKey(var)) {
                String value = map.get(var);
                stack.push(value);
            } else if (var != null && !map.containsKey(var)) {
                unknown = true;
                break;
            }
        }
        if (unknown) {
            System.out.println("Unknown variable");
        } else if (result != null) {
            result = new BigInteger(stack.pop());
            System.out.println(result);
        }
    }

    /*
     *  in case of division by zero result = null !
     *  stack must be reset otherwise remain
     *  values will effect next calculation
     */

    public boolean isCalculation(Matcher matcher) {
        return matcher.group(10) != null;
    }

    private BigInteger performOperationDependOnOperator(String operator, Deque<String> stack) {
        BigInteger result = BigInteger.ZERO;
        BigInteger number2 = new BigInteger(stack.pop());
        BigInteger number1 = new BigInteger(stack.pop());
        switch (operator) {
            case PLUS:
                return result.add(number1.add(number2));
            case MINUS:
                return result.add(number1.subtract(number2));
            case ASTERISK:
                return result.add(number1.multiply(number2));
            case BACKSLASH:
                return tryForDivisionByZero(result, number2, number1);
            case DASH:
                int pow = Integer.parseInt(String.valueOf(number2));
                return result.add(number1.pow(pow));
        }
        return result;
    }

    private BigInteger tryForDivisionByZero(BigInteger result, BigInteger number2, BigInteger number1) {
        try {
            return result.add(number1.divide(number2));
        } catch (ArithmeticException ex) {
            System.out.println("Cannot divide by zero.");
            return null;
        }
    }
}
