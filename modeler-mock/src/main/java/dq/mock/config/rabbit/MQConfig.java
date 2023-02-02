package dq.mock.config.rabbit;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MQConfig {

    @Bean
    public TopicExchange modelerExchange() { return new TopicExchange(Constant.EXCHANGE); }

    @Bean
    public Queue modelerQueue() { return new Queue(Constant.QUEUE, false); }

    @Bean
    public Binding configBinding(@Qualifier("modelerQueue") Queue queue,
                                 @Qualifier("modelerExchange") TopicExchange exchange) {
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