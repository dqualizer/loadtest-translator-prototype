package dq.config.rabbit;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for the input queue
 */
@Configuration
public class TranslatorMQConfig {

    @Bean
    public TopicExchange loadtestExchange() { return new TopicExchange(Constant.LOADTEST_EXCHANGE); }

    @Bean
    public Queue loadtestQueue() { return new Queue(Constant.LOADTEST_QUEUE, false); }

    @Bean
    public Binding loadtestBinding(@Qualifier("loadtestQueue") Queue queue,
                                 @Qualifier("loadtestExchange") TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(Constant.GET_KEY);
    }
}