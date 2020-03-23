package calculator;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Calculation {
    public static final String CALCULATION_REGEX =
            "^(\\s*((?<![a-zA-Z])\\d+(?![a-zA-Z])|(?<!\\d)[a-zA-Z]+(?!\\d)|[-+]+)\\s*)+$";
    private VarAssigner assigner;

    public Calculation(VarAssigner assigner) {
        this.assigner = assigner;
    }

    public void sum(String input) {
        String regex = "([-+]+)|(\\d+)|([a-zA-Z]+)";
        Map<String, Long> map = assigner.getVariables();
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        long sum = 0L;
        boolean minus = false;
        boolean unknown = false;
        while (matcher.find()) {
            String sing = matcher.group(1);
            String digit = matcher.group(2);
            String var = matcher.group(3);
            long num = 0;
            if (sing != null) {
                int length = sing.replaceAll("[^-]", "").length();
                minus = length % 2 == 1;
            }
            if (digit != null) {
                num = Long.parseLong(digit);
            }
            if (var != null && map.containsKey(var)) {
                num = map.get(var);
            } else if (var != null && !map.containsKey(var)) {
                unknown = true;
                break;
            }
            sum += minus ? -num : num;
        }
        if (unknown) {
            System.out.println("Unknown variable");
        } else {
            System.out.println(sum);
        }
    }

    public boolean isCalculation(Matcher matcher) {
        return matcher.group(10) != null;
    }
}
