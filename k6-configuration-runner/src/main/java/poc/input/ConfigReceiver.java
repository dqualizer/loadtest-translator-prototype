package poc.input;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import poc.config.rabbit.Constant;
import poc.dqlang.Config;
import poc.loadtest.ConfigRunner;

/**
 * Imports a k6 loadtest configuration via RabbitMQ
 */
@Component
public class ConfigReceiver {

    @Autowired
    private ConfigRunner runner;

    @RabbitListener(queues = Constant.QUEUE)
    public void receive(@Payload Config config) {
        runner.start(config);
    }


    /*
    Just for testing, will be deleted later
     */
    @EventListener(ApplicationReadyEvent.class)
    public void test() throws JsonProcessingException {
        String configString = "{\n" +
                "    \"name\": \"werkstattauftrag\",\n" +
                "    \"baseURL\": \"http://127.0.0.1:9000\",\n" +
                "    \"loadTests\": [\n" +
                "        {\n" +
                "            \"repetition\": 2,\n" +
                "            \"options\": {\n" +
                "                \"scenarios\": {\n" +
                "                    \"scenario\": {\n" +
                "                        \"executor\": \"ramping-vus\",\n" +
                "                        \"startVUs\": 0,\n" +
                "                        \"stages\": [\n" +
                "                            {\n" +
                "                                \"duration\": \"10s\",\n" +
                "                                \"target\": 0\n" +
                "                            },\n" +
                "                            {\n" +
                "                                \"duration\": \"5s\",\n" +
                "                                \"target\": 40\n" +
                "                            }\n" +
                "                        ]\n" +
                "                    }\n" +
                "                }\n" +
                "            },\n" +
                "            \"request\": {\n" +
                "                \"type\": \"GET\",\n" +
                "                \"path\": \"/auftrag/${auftragsnummer}\",\n" +
                "                \"params\": {},\n" +
                "                \"payload\": {},\n" +
                "                \"checks\": {\n" +
                "                    \"status_codes\": [\n" +
                "                        200\n" +
                "                    ],\n" +
                "                    \"duration\": 1200\n" +
                "                },\n" +
                "                \"path_variables\": {\n" +
                "                    \"auftragsnummer\": \"auftrag/auftragsnummern/angelegt.json\"\n" +
                "                }\n" +
                "            }\n" +
                "        },\n" +
                "        {\n" +
                "            \"repetition\": 2,\n" +
                "            \"options\": {\n" +
                "                \"scenarios\": {\n" +
                "                    \"scenario\": {\n" +
                "                        \"executor\": \"ramping-vus\",\n" +
                "                        \"startVUs\": 0,\n" +
                "                        \"stages\": [\n" +
                "                            {\n" +
                "                                \"duration\": \"2s\",\n" +
                "                                \"target\": 1\n" +
                "                            },\n" +
                "                            {\n" +
                "                                \"duration\": \"2s\",\n" +
                "                                \"target\": 4\n" +
                "                            },\n" +
                "                            {\n" +
                "                                \"duration\": \"2s\",\n" +
                "                                \"target\": 9\n" +
                "                            },\n" +
                "                            {\n" +
                "                                \"duration\": \"2s\",\n" +
                "                                \"target\": 16\n" +
                "                            },\n" +
                "                            {\n" +
                "                                \"duration\": \"2s\",\n" +
                "                                \"target\": 0\n" +
                "                            }\n" +
                "                        ]\n" +
                "                    }\n" +
                "                }\n" +
                "            },\n" +
                "            \"request\": {\n" +
                "                \"type\": \"POST\",\n" +
                "                \"path\": \"/auftrag/new\",\n" +
                "                \"params\": {\n" +
                "                    \"headers\": \"auftrag/allgemein/headers.json\"\n" +
                "                },\n" +
                "                \"payload\": {\n" +
                "                    \"auftraggeber_2022\": \"auftrag/auftraggeber/2022/auftraggeber.json\"\n" +
                "                },\n" +
                "                \"checks\": {\n" +
                "                    \"status_codes\": [\n" +
                "                        201\n" +
                "                    ],\n" +
                "                    \"duration\": 1200\n" +
                "                },\n" +
                "                \"path_variables\": {}\n" +
                "            }\n" +
                "        },\n" +
                "        {\n" +
                "            \"repetition\": 1,\n" +
                "            \"options\": {\n" +
                "                \"scenarios\": {\n" +
                "                    \"scenario\": {\n" +
                "                        \"executor\": \"constant-vus\",\n" +
                "                        \"vus\": 20,\n" +
                "                        \"duration\": 3600\n" +
                "                    }\n" +
                "                }\n" +
                "            },\n" +
                "            \"request\": {\n" +
                "                \"type\": \"PUT\",\n" +
                "                \"path\": \"/auftrag/${auftragsnummer}\",\n" +
                "                \"params\": {\n" +
                "                    \"headers\": \"auftrag/allgemein/headers.json\"\n" +
                "                },\n" +
                "                \"payload\": {\n" +
                "                    \"auftragstatus\": \"auftrag/auftragsstatus/auftragsstatus.json\"\n" +
                "                },\n" +
                "                \"checks\": {\n" +
                "                    \"status_codes\": [\n" +
                "                        200\n" +
                "                    ],\n" +
                "                    \"duration\": 2400\n" +
                "                },\n" +
                "                \"path_variables\": {\n" +
                "                    \"auftragsnummer\": \"auftrag/auftragsnummern/angelegt.json\"\n" +
                "                }\n" +
                "            }\n" +
                "        }\n" +
                "    ]\n" +
                "}";

        ObjectMapper mapper = new ObjectMapper();
        Config config = mapper.readValue(configString, Config.class);
        runner.start(config);
    }
}