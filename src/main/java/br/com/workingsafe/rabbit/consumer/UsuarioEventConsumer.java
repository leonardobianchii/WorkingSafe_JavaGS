package br.com.workingsafe.rabbit.consumer;

import br.com.workingsafe.rabbit.config.RabbitConfig;
import br.com.workingsafe.rabbit.events.UsuarioEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class UsuarioEventConsumer {

    private static final Logger logger = LoggerFactory.getLogger(UsuarioEventConsumer.class);

    @RabbitListener(queues = RabbitConfig.QUEUE_USUARIO)
    public void receber(UsuarioEvent event) {
        logger.info("[CONSUMER] Evento de usuario recebido: {}", event);
    }
}
