package dq.config.rabbit;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelingMQConfig {

    @Bean
    public TopicExchange modelerExchange() { return new TopicExchange(Constant.MODELER_EXCHANGE); }

    @Bean
    public Queue modelerQueue() { return new Queue(Constant.MODELING_QUEUE, false); }

    @Bean
    public Binding configBinding(@Qualifier("modelerQueue") Queue queue,
                                 @Qualifier("modelerExchange") TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(Constant.GET_KEY);
    }
}