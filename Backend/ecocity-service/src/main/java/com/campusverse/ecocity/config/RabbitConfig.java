package com.campusverse.ecocity.config;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** MS4 publishes its events on the "ecocity.events" topic exchange, as JSON. */
@Configuration
public class RabbitConfig {

    public static final String EXCHANGE = "ecocity.events";
    public static final String ALERT_RAISED_KEY = "ecocity.alert.raised";
    public static final String ALERT_RESOLVED_KEY = "ecocity.alert.resolved";

    @Bean
    public TopicExchange ecocityExchange() {
        return new TopicExchange(EXCHANGE);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new JacksonJsonMessageConverter();
    }
}
