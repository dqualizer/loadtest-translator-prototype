package poc.loadtest.mapper;

import org.springframework.stereotype.Component;
import poc.dqlang.request.Checks;
import poc.dqlang.request.Request;

import java.util.LinkedHashSet;

/**
 * Maps the expected responses to Javascript-Code
 */
@Component
public class ChecksMapper implements k6Mapper {

    @Override
    public String map(Request request) {
        StringBuilder checksBuilder = new StringBuilder();
        Checks checks = request.getChecks();

        int duration = checks.getDuration();
        String durationScript = String.format("\t'Duration < %d': x => x.timings && x.timings.duration < %d,%s",
                duration, duration, newLine);
        checksBuilder.append(durationScript);

        String type = request.getType();
        LinkedHashSet<Integer> statusCodes = checks.getStatusCodes();
        StringBuilder statusBuilder = new StringBuilder();
        for(int status: statusCodes) {
            String statusBooleanScript = String.format("x.status == %d || ", status);
            statusBuilder.append(statusBooleanScript);
        }
        String statusScript = String.format("\t'%s status was expected': x => x.status && (%sfalse),%s",
                type, statusBuilder, newLine);
        checksBuilder.append(statusScript);

        return String.format("check(response, {%s%s});%s",
                newLine, checksBuilder, newLine);
    }
}