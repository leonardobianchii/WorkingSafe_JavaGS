package br.com.workingsafe.rabbit.producer;

import br.com.workingsafe.rabbit.config.RabbitConfig;
import br.com.workingsafe.rabbit.events.UsuarioEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class UsuarioEventProducer {

    private static final Logger logger = LoggerFactory.getLogger(UsuarioEventProducer.class);

    private final RabbitTemplate rabbitTemplate;

    public UsuarioEventProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void enviarUsuarioCreated(UsuarioEvent event) {
        enviar(event, RabbitConfig.RK_USUARIO_CREATED);
    }

    public void enviarUsuarioUpdated(UsuarioEvent event) {
        enviar(event, RabbitConfig.RK_USUARIO_UPDATED);
    }

    public void enviarUsuarioDeleted(UsuarioEvent event) {
        enviar(event, RabbitConfig.RK_USUARIO_DELETED);
    }

    private void enviar(UsuarioEvent event, String routingKey) {
        logger.info("[RABBIT] Enviando evento de usuario: {} - {}", routingKey, event);
        rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE_WORKINGSAFE, routingKey, event);
    }
}
