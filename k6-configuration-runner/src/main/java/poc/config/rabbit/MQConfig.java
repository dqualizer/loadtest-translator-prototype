package poc.config.rabbit;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for RabbitMQ
 */
@Configuration
public class MQConfig {

    @Bean
    public TopicExchange k6Exchange() { return new TopicExchange(Constant.EXCHANGE); }

    @Bean
    public Queue k6Queue() { return new Queue(Constant.QUEUE, false); }

    @Bean
    public Binding k6Binding(@Qualifier("k6Queue") Queue queue,
                                  @Qualifier("k6Exchange") TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(Constant.KEY);
    }

    @Bean
    public MessageConverter messageConverter() { return new Jackson2JsonMessageConverter(); }

    @Bean
    public AmqpTemplate template(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter());
        return template;
    }
}