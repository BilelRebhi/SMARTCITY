package com.campusverse.ecocity.event;

import com.campusverse.ecocity.config.RabbitConfig;
import com.campusverse.ecocity.entity.PollutionAlert;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

/** Publishes alert events. If RabbitMQ is down, MS4 logs a warning and keeps working. */
@Slf4j
@Component
@RequiredArgsConstructor
public class AlertEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public void alertRaised(PollutionAlert alert) {
        PollutionAlertRaised event = new PollutionAlertRaised(
                UUID.randomUUID().toString(),
                alert.getId(),
                alert.getMeasurement().getStation().getId(),
                alert.getMeasurement().getStation().getZone().getId(),
                alert.getIndicator(),
                alert.getValue(),
                alert.getThreshold().getMaxValue(),
                alert.getThreshold().getUnit(),
                alert.getSeverity(),
                alert.getTriggeredAt());
        send(RabbitConfig.ALERT_RAISED_KEY, event);
    }

    public void alertResolved(PollutionAlert alert) {
        PollutionAlertResolved event = new PollutionAlertResolved(
                UUID.randomUUID().toString(),
                alert.getId(),
                alert.getMeasurement().getStation().getZone().getId(),
                LocalDateTime.now());
        send(RabbitConfig.ALERT_RESOLVED_KEY, event);
    }

    private void send(String routingKey, Object event) {
        try {
            rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE, routingKey, event);
        } catch (AmqpException e) {
            log.warn("Could not publish {} to RabbitMQ: {}", routingKey, e.getMessage());
        }
    }
}
