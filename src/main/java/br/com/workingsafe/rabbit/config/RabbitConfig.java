package br.com.workingsafe.rabbit.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    // Nomes de exchange / filas / routing keys
    public static final String EXCHANGE_WORKINGSAFE = "workingsafe.events";

    public static final String QUEUE_USUARIO = "workingsafe.usuario.queue";
    public static final String QUEUE_CHECKIN = "workingsafe.checkin.queue";

    public static final String RK_USUARIO = "usuario.*";
    public static final String RK_USUARIO_CREATED = "usuario.created";
    public static final String RK_USUARIO_UPDATED = "usuario.updated";
    public static final String RK_USUARIO_DELETED = "usuario.deleted";

    public static final String RK_CHECKIN_CREATED = "checkin.created";

    @Bean
    public TopicExchange workingsafeExchange() {
        return new TopicExchange(EXCHANGE_WORKINGSAFE);
    }

    @Bean
    public Queue usuarioQueue() {
        return QueueBuilder.durable(QUEUE_USUARIO).build();
    }

    @Bean
    public Queue checkinQueue() {
        return QueueBuilder.durable(QUEUE_CHECKIN).build();
    }

    @Bean
    public Binding usuarioBinding(Queue usuarioQueue, TopicExchange workingsafeExchange) {
        return BindingBuilder.bind(usuarioQueue)
                .to(workingsafeExchange)
                .with(RK_USUARIO);
    }

    @Bean
    public Binding checkinBinding(Queue checkinQueue, TopicExchange workingsafeExchange) {
        return BindingBuilder.bind(checkinQueue)
                .to(workingsafeExchange)
                .with(RK_CHECKIN_CREATED);
    }

    // Converter JSON para os eventos
    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
