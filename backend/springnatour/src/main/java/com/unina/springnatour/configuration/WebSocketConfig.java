package com.unina.springnatour.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.DefaultContentTypeResolver;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import java.util.List;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    /**
     * The one configures a simple in-memory message broker
     * with one destination for sending and receiving messages, this destination is prefixed with /user.
     * The second designates the /app prefix for methods annotated with @MessageMapping.
     * The third sets User destination prefix /user, is used by ConvertAndSendToUser method of
     * SimpleMessagingTemplate to prefix all user-specific destinations with /user.
     * @param config Registry for message broker configuration
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/user");
        config.setApplicationDestinationPrefixes("/natour");
        config.setUserDestinationPrefix("/user");
    }

    /**
     * This method registers /ws STOMP endpoint. This endpoint is used by the client to connect to the STOMP server.
     * You can also enable the SockJS options, so that alternative messaging options may be used if WebSockets are not available.
     * @param registry Contract to register STOMP over end-points
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry
                .addEndpoint("/ws")
                .setAllowedOrigins("*");
                //.withSockJS();
    }

    /**
     * This method allows message conversion from/to JSON, used by Spring
     * @param messageConverters List of converters
     * @return false
     */
    @Override
    public boolean configureMessageConverters(List<MessageConverter> messageConverters) {
        DefaultContentTypeResolver resolver = new DefaultContentTypeResolver();
        resolver.setDefaultMimeType(MimeTypeUtils.APPLICATION_JSON);

        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setObjectMapper(new ObjectMapper());
        converter.setContentTypeResolver(resolver);

        messageConverters.add(converter);

        return false;
    }
}
