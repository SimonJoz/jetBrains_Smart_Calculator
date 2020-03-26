package calculator;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Postfix {
    private static final String LEFT_PARENTHESIS = "(";
    private static final String RIGHT_PARENTHESIS = ")";
    public static final String ASTERISK = "*";
    public static final String BACKSLASH = "/";
    public static final String PLUS = "+";
    public static final String MINUS = "-";
    public static final String DASH = "^";

    private static boolean isCorrectOperator = true;
    private static boolean hasBalancedBrackets = true;

    private String postfixResult;
    private Deque<String> stack = new ArrayDeque<>();
    private StringBuilder postfix = new StringBuilder();

    private final String regex = "([-]+)|([+]+)|([*/^]+)|([()])|(\\w+)";
    private Pattern pattern = Pattern.compile(regex);

    public void convertToPostfix(String infix) {
        if (infix.startsWith(MINUS)) {
            postfix.append(MINUS);
            infix = infix.substring(1);
        }
        Matcher matcher = pattern.matcher(infix);
        while (matcher.find()) {
            String minusOperator = matcher.group(1);
            String plusOperator = matcher.group(2);
            String asteriskOrBackslash = matcher.group(3);
            String parenthesis = matcher.group(4);
            String digitOrVar = matcher.group(5);
            if (minusOperator != null) {
                String result = checkIsItPlusOrMinus(minusOperator);
                pushOrPop(result);
            }
            if (plusOperator != null) {
                pushOrPop(PLUS);
            }
            if (asteriskOrBackslash != null) {
                if (hasCorrectLength(asteriskOrBackslash)) {
                    pushOrPop(asteriskOrBackslash);
                } else {
                    isCorrectOperator = false;
                    break;
                }
            }
            if (parenthesis != null) {
                pushOrPop(parenthesis);
            }
            if (digitOrVar != null) {
                postfixAppend(digitOrVar);
            }
        }
        ifValidAppendAll();
        postfixResult = postfix.toString();
        resetStringBuilder();  // otherwise will append to previous value
    }

    public boolean isExpressionValid() {
        return !stack.contains(LEFT_PARENTHESIS)
                && isCorrectOperator
                && hasBalancedBrackets;
    }

    private void ifValidAppendAll() {
        if (isExpressionValid()) {
            stack.forEach(element -> postfixAppend(stack.pop()));
        }
    }

    public void resetStack() {
        stack.clear();
    }

    public Deque<String> getStack() {
        return stack;
    }

    public String getPostfixResult() {
        return postfixResult;
    }

    private void pushOrPop(String operator) {
        if (stack.isEmpty() || LEFT_PARENTHESIS.equals(operator)) {
            stack.push(operator);
        } else if (RIGHT_PARENTHESIS.equals(operator)) {
            ifStackContainsLeftAppendAndPopLeft();
        } else {
            comparePriorityAndAppend(operator);
            stack.push(operator);
        }
    }

    private void ifStackContainsLeftAppendAndPopLeft() {
        if (stack.contains(LEFT_PARENTHESIS)) {
            String head = stack.getFirst();
            while (!LEFT_PARENTHESIS.equals(head)) {
                popAndAppendToResult();
                head = stack.getFirst();
            }
            stack.pop(); // left parenthesis
        } else {
            hasBalancedBrackets = false;
        }
    }

    private void comparePriorityAndAppend(String operator) {
        String head = stack.getFirst();
        int headPriority = checkPriority(head);
        int currentPriority = checkPriority(operator);
        if (currentPriority <= headPriority && !LEFT_PARENTHESIS.equals(head)) {
            while (!(checkPriority(head) < currentPriority || stack.isEmpty())) {
                head = stack.getFirst();
                if (!LEFT_PARENTHESIS.equals(head)) {
                    popAndAppendToResult();
                    break;
                }
            }
        }
    }

    private int checkPriority(String operator) {
        final int lowPriority = 0;
        final int midPriority = 1;
        final int highPriority = 2;
        int priority = lowPriority;
        if (ASTERISK.equals(operator) || BACKSLASH.equals(operator)) {
            priority = midPriority;
        } else if (DASH.equals(operator)) {
            priority = highPriority;
        }
        return priority;
    }

    private boolean hasCorrectLength(String asteriskOrBackslash) {
        final int correctLength = 1;
        int length = asteriskOrBackslash.length();
        return length == correctLength;
    }

    private String checkIsItPlusOrMinus(String operator) {
        // "--" = "+";
        int length = operator.length();
        return length % 2 == 1 ? MINUS : PLUS;
    }

    private void popAndAppendToResult() {
        postfixAppend(stack.pop());
    }

    private void postfixAppend(String string) {
        postfix.append(string).append(" ");
    }

    private void resetStringBuilder() {
        postfix.delete(0, postfix.length());
    }

}