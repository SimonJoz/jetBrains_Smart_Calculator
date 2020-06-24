package calculator;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;

import static java.lang.String.format;

public class VarAssigner {
    public static final String VAR_ASSIGNMENT_REGEX = "^(([a-zA-z]+)\\s*=+\\s*((\\d+)|([a-zA-Z]+)\\s*))$";
    public static final String INVALID_IDENTIFIER_REGEX =
            "^(([\\W&&[^\\s]]+.*|[\\D&&[^\\s]]+\\d.*|[\\w&&[^a-zA-Z]]+[a-zA-Z].*|\\S+[\\W&&[^\\s]].*)\\s*=.*)$";
    public static final String INVALID_ASSIGNMENT_REGEX = format("%s%s",
            "(.*=+\\s*([\\W&&[^\\s]]+.*|[\\D&&[^\\s]]+\\s*\\d.*|",
            "[\\w&&[^a-zA-Z]]+\\s*[a-zA-Z].*|\\S+\\s*[\\W&&[^\\s]].*))$");

    private final Map<String, String> variables = new HashMap<>();

//     Assignment to var -- groups: 1-5;
    public void mapAssignment(Matcher matcher) {
        String key = matcher.group(2);
        String number = matcher.group(4);
        String var = matcher.group(5);
        if (number != null) {
            variables.put(key, number);
        }
        if (var != null && variables.containsKey(var)) {
            String value = variables.get(var);
            variables.put(key, value);
        } else if (var != null && !variables.containsKey(var)) {
            System.out.println("Unknown variable");
        }
    }

    /*
     * Invalid identifier -- groups: 6,7;
     * Invalid assignment -- groups: 8,9;
     */
    public void isAssignmentCorrect(Matcher matcher) {
        if (matcher.group(6) != null) {
            System.out.println("Invalid identifier");
        } else if (matcher.group(8) != null) {
            System.out.println("Invalid assignment");
        }
    }

    public boolean isAssignment(Matcher matcher) {
        return matcher.group(1) != null;
    }

    public Map<String, String> getVariables() {
        return variables;
    }
}
