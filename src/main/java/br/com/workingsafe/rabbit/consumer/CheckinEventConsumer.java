package br.com.workingsafe.rabbit.consumer;

import br.com.workingsafe.rabbit.config.RabbitConfig;
import br.com.workingsafe.rabbit.events.CheckinEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class CheckinEventConsumer {

    private static final Logger logger = LoggerFactory.getLogger(CheckinEventConsumer.class);

    @RabbitListener(queues = RabbitConfig.QUEUE_CHECKIN)
    public void receber(CheckinEvent event) {
        logger.info("[CONSUMER] Evento de checkin recebido: {}", event);
    }
}
