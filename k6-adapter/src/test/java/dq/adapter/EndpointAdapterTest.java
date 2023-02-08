package dq.adapter;

import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

public class EndpointAdapterTest {

    private String testPath = "/auftrag/{auftragsnummer}/{userID}";

    @Test
    void pathVariablesHaveBeenFoundByRegexPattern() {
        Pattern pathVariablePattern = Pattern.compile("\\{.*?}");
        Matcher matcher = pathVariablePattern.matcher(testPath);
        List<String> foundVariables = new LinkedList<>();

        while(matcher.find()) {
            String variable = matcher.group();
            foundVariables.add(variable);
        }
        assertFalse(foundVariables.isEmpty());
        assertTrue(foundVariables.contains("{auftragsnummer}") && foundVariables.contains("{userID}"));
    }

    @Test
    void pathVariablesHaveBeenMarked() {
        String field = "/auftrag/{auftragsnummer}/{userID}";
        String markedField = "/auftrag/${auftragsnummer}/${userID}";

        Pattern pattern = Pattern.compile("\\{.*?}");
        Matcher matcher = pattern.matcher(field);
        List<String> variables = new LinkedList<>();

        while (matcher.find()) {
            String foundVariable = matcher.group();
            variables.add(foundVariable);
        }

        String markedPathVariables = variables.stream()
                .distinct()
                .reduce(field, (path, variable) -> path.replace(variable, "$"+ variable));

        assertEquals(markedPathVariables, markedField);
    }
}