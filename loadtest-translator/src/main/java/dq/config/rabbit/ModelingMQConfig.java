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
    public TopicExchange modelingExchange() { return new TopicExchange(Constant.MODELER_EXCHANGE); }

    @Bean
    public Queue modelingQueue() { return new Queue(Constant.MODELING_QUEUE, false); }

    @Bean
    public Binding modelingBinding(@Qualifier("modelingQueue") Queue queue,
                                 @Qualifier("modelingExchange") TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(Constant.GET_KEY);
    }
}