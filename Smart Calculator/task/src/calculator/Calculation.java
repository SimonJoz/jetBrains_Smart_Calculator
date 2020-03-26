package calculator;

import java.util.Deque;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static calculator.Postfix.*;

public class Calculation {
    public static final String CALCULATION_REGEX = String.format("%s%s%s",
            "^(?![/*^])(\\s*((?<![a-zA-Z])\\d+\\s*(?![a-zA-Z])|(?<!\\d)[a-zA-Z]+\\s*(?!\\d)",
                    "|[-]+\\s*(?!\\W-[()])|[+]+\\s*(?!\\W-[()])|[*]+\\s*(?!\\W-[()])|",
                    "[/]+\\s*(?!\\W-[()])|\\^+\\s*(?!\\W-[()])|[()]+\\s*))+$");

    private VarAssigner assigner;
    private Postfix postfix;

    public Calculation(VarAssigner assigner, Postfix postfix) {
        this.assigner = assigner;
        this.postfix = postfix;
    }

    private final String regex = "([-+*/^]\\s+)|([-]?\\d+)|([a-zA-Z]+)";
    private Pattern pattern = Pattern.compile(regex);

    public void calculateResult() {
        Map<String, String> map = assigner.getVariables();
        Deque<String> stack = postfix.getStack();
        String postfixResult = postfix.getPostfixResult();
        Matcher matcher = pattern.matcher(postfixResult);
        long result;
        boolean unknown = false;
        while (matcher.find()) {
            String operator = matcher.group(1);
            String number = matcher.group(2);
            String var = matcher.group(3);
            if (operator != null) {
                operator = operator.trim(); // regex catch operators with space -- "- ","* "
                result = performOperationDependOnOperator(operator, stack);
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
        } else {
            result = Long.parseLong(stack.pop());
            System.out.println(result);
        }
    }

    public boolean isCalculation(Matcher matcher) {
        return matcher.group(10) != null;
    }

    private long performOperationDependOnOperator(String operator, Deque<String> stack) {
        long result = 0;
        long number2 = Long.parseLong(stack.pop());
        long number1 = Long.parseLong(stack.pop());
        switch (operator){
            case PLUS:
                result = number1 + number2;
                break;
            case MINUS:
                result = number1 - number2;
                break;
            case ASTERISK:
                result = number1 * number2;
                break;
            case BACKSLASH:
                result = number1 / number2;
                break;
            case DASH:
                result = (long) Math.pow(number1, number2);
                break;
        }
        return result;
    }
}
