package calculator;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static calculator.Constance.*;

public class Postfix {
    private static final String LEFT_PARENTHESIS = "(";
    private static final String RIGHT_PARENTHESIS = ")";

    private static boolean isCorrectOperator = true;
    private static boolean hasBalancedBrackets = true;

    private final Deque<String> stack = new ArrayDeque<>();
    private final StringBuilder postfix = new StringBuilder();
    private final String regex = "([-]+)|([+]+)|([*/^]+)|([()])|(\\w+)";
    private final Pattern pattern = Pattern.compile(regex);
    private String postfixResult;

    public void convertToPostfix(String infix) {
        if (infix.startsWith(MINUS)) {
            postfix.append(MINUS);
            infix = infix.substring(1);
        }
        Matcher matcher = pattern.matcher(infix);
        while (matcher.find()) {
            final String minusOperator = matcher.group(1);
            final String plusOperator = matcher.group(2);
            final String asteriskOrBackslash = matcher.group(3);
            final String parenthesis = matcher.group(4);
            final String digitOrVar = matcher.group(5);
            if (minusOperator != null) {
                pushOrPop(checkIsItPlusOrMinus(minusOperator));
            }
            ifNotNullPushOrPopOperator(plusOperator);
            if (asteriskOrBackslash != null) {
                if (asteriskOrBackslash.length() == 1) {
                    pushOrPop(asteriskOrBackslash);
                } else {
                    isCorrectOperator = false;
                    break;
                }
            }
            ifNotNullPushOrPopOperator(parenthesis);
            if (digitOrVar != null) {
                postfixAppend(digitOrVar);
            }
        }
        ifValidAppendAll();
        postfixResult = postfix.toString();
        resetStringBuilder();  // otherwise will append to previous value
    }


    private void ifNotNullPushOrPopOperator(String operator) {
        if (operator != null) {
            pushOrPop(operator);
        }
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

    private void ifValidAppendAll() {
        if (isExpressionValid()) {
            stack.forEach(element -> postfixAppend(stack.pop()));
        }
    }

    public boolean isExpressionValid() {
        return !stack.contains(LEFT_PARENTHESIS)
                && isCorrectOperator
                && hasBalancedBrackets;
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
        int priority = 0;
        final int mid = 1;
        final int high = 2;
        if (ASTERISK.equals(operator) || BACKSLASH.equals(operator)) {
            priority = mid;
        } else if (DASH.equals(operator)) {
            priority = high;
        }
        return priority;
    }

    private String checkIsItPlusOrMinus(String operator) {
        return operator.length() % 2 == 1 ? MINUS : PLUS; // "--" = "+";
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


    public void resetStack() {
        stack.clear();
    }

    public Deque<String> getStack() {
        return stack;
    }

    public String getPostfixResult() {
        return postfixResult;
    }
}