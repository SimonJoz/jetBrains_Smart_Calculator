package calculator;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;

public class VarAssigner {
    public static final String VAR_ASSIGNMENT_REGEX = "^(([a-zA-z]+)\\s*=+\\s*((\\d+)|([a-zA-Z]+)\\s*))$";
    public static final String INVALID_IDENTIFIER_REGEX =
            "^(([\\W&&[^\\s]]+.*|[\\D&&[^\\s]]+\\d.*|[\\w&&[^a-zA-Z]]+[a-zA-Z].*|\\S+[\\W&&[^\\s]].*)\\s*=.*)$";
    public static final String INVALID_ASSIGNMENT_REGEX =
            "(.*=+\\s*([\\W&&[^\\s]]+.*|[\\D&&[^\\s]]+\\s*\\d.*|" +
                    "[\\w&&[^a-zA-Z]]+\\s*[a-zA-Z].*|\\S+\\s*[\\W&&[^\\s]].*))$";

    private Map<String, Long> variables = new HashMap<>();

    /*
     Assignment to var:
        -- ^(([a-zA-z]+)\s*=+\s*((\d+)|([a-zA-Z]+)\s*)$
        -- Groups: 1-5;
     */

    public boolean isAssignment(Matcher matcher) {
        return matcher.group(1) != null;
    }

    public void mapAssignment(Matcher matcher) {
        String key = matcher.group(2);
        String digit = matcher.group(4);
        String var = matcher.group(5);
        Long value;
        if (digit != null) {
            value = Long.parseLong(digit);
            variables.put(key, value);
        }
        if (var != null && variables.containsKey(var)) {
            value = variables.get(var);
            variables.put(key, value);
        } else if (var != null && !variables.containsKey(var)) {
            System.out.println("Unknown variable");
        }
    }

         /*
        Invalid identifier ---  Groups: 6,7;
        Invalid assignment ---  Groups: 8,9
         */
    public void checkIsAssignmentCorrect(Matcher matcher) {
        if (matcher.group(6) != null) {
            System.out.println("Invalid identifier");
        } else if (matcher.group(8) != null) {
            System.out.println("Invalid assignment");
        }
    }

    public Map<String, Long> getVariables() {
        return variables;
    }


}
