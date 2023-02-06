package dq.rabbit;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class K6MQConfig {

    @Bean
    public TopicExchange k6Exchange() { return new TopicExchange(Constant.K6_EXCHANGE); }

    @Bean
    public Queue k6Queue() { return new Queue(Constant.K6_QUEUE, false); }

    @Bean
    public Binding k6Binding(@Qualifier("k6Queue") Queue queue,
                                 @Qualifier("k6Exchange") TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(Constant.POST_KEY);
    }
}