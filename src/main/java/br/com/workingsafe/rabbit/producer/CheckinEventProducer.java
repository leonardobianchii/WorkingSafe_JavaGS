package br.com.workingsafe.rabbit.producer;

import br.com.workingsafe.rabbit.config.RabbitConfig;
import br.com.workingsafe.rabbit.events.CheckinEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class CheckinEventProducer {

    private static final Logger logger = LoggerFactory.getLogger(CheckinEventProducer.class);

    private final RabbitTemplate rabbitTemplate;

    public CheckinEventProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void enviarCheckinCreated(CheckinEvent event) {
        logger.info("[RABBIT] Enviando evento de checkin: {}", event);
        rabbitTemplate.convertAndSend(
                RabbitConfig.EXCHANGE_WORKINGSAFE,
                RabbitConfig.RK_CHECKIN_CREATED,
                event
        );
    }
}
