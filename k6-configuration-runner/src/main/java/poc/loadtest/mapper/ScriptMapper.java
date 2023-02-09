package poc.loadtest.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import poc.dqlang.LoadTest;
import poc.dqlang.options.Options;
import poc.dqlang.request.Request;

import javax.swing.text.html.Option;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

@Component
public class ScriptMapper implements k6Mapper {

    @Autowired
    private ParamsMapper paramsMapper;
    @Autowired
    private PayloadMapper payloadMapper;
    @Autowired
    private PathVariablesMapper pathVariablesMapper;
    @Autowired
    private HttpMapper httpMapper;
    @Autowired
    private ChecksMapper checksMapper;

    public List<String> getScript(String baseURL, LoadTest loadTest) throws JsonProcessingException {
        List<String> script = new LinkedList<>();

        Options options = loadTest.getOptions();
        script.add(this.startScript(baseURL, options));

        Request request = loadTest.getRequest();
        String requestScript = this.map(request);
        script.add(requestScript);

        script.add("}");
        return script;
    }

    @Override
    public String map(Request request) {
        StringBuilder requestBuilder = new StringBuilder();

        if(!request.getParams().isEmpty()) {
            String paramsScript = paramsMapper.map(request);
            requestBuilder.append(paramsScript);
        }
        if(!request.getPayload().isEmpty()) {
            String payloadScript = payloadMapper.map(request);
            requestBuilder.append(payloadScript);
        }
        if(!request.getPathVariables().isEmpty()) {
            String pathVariablesScript = pathVariablesMapper.map(request);
            requestBuilder.append(pathVariablesScript);
        }

        String httpScript = httpMapper.map(request);
        requestBuilder.append(httpScript);
        requestBuilder.append(trackDataPerURLScript());

        if(request.getChecks() != null) {
            String checksScript = checksMapper.map(request);
            requestBuilder.append(checksScript);
        }
        requestBuilder.append(this.sleepScript());

        return requestBuilder.toString();
    }

    private String startScript(String baseURL, Options options) throws JsonProcessingException {
        String optionsString = objectMapper.writeValueAsString(options);
        String trackDataPerURL = this.trackDataPerURLInitScript();
        return """
                import http from 'k6/http';
                import {check, sleep} from 'k6';
                %s

                let baseURL = '%s';

                export let options = %s;
                                
                """.formatted(trackDataPerURL, baseURL, optionsString);
    }

    private String trackDataPerURLInitScript() {
        return """
                import {Counter} from 'k6/metrics';
                                
                export const epDataSent = new Counter('data_sent_endpoint');
                export const epDataRecv = new Counter('data_received_endpoint');
                                
                function sizeOfHeaders(headers) {
                    return Object.keys(headers).reduce((sum, key) => sum + key.length + headers[key].length, 0);
                }
                
                function trackDataMetricsPerURL(res) {
                    epDataSent.add(sizeOfHeaders(res.request.headers) + res.request.body.length, { url: res.url });
                    epDataRecv.add(sizeOfHeaders(res.headers) + res.body.length, { url: res.url });
                }
                """;
    }

    private String trackDataPerURLScript() {
        return String.format("trackDataMetricsPerURL(response);%s", newLine);
    }

    private String sleepScript() {
        Random random = new Random();
        int duration = random.nextInt(5) + 1;
        return String.format("sleep(%d);%s",
                duration, newLine);
    }


}