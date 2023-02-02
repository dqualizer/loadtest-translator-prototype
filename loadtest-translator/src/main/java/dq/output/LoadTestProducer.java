package dq.output;

import dq.config.rabbit.Constant;
import dq.dqlang.loadtest.LoadTest;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LoadTestProducer {

    @Autowired
    private RabbitTemplate template;

    public String produce(LoadTest loadTest) {
        template.convertAndSend(
                Constant.LOADTEST_EXCHANGE,
                Constant.POST_KEY,
                loadTest
        );

        return "LOADTEST CONFIGURATION WAS PRODUCED";
    }
}