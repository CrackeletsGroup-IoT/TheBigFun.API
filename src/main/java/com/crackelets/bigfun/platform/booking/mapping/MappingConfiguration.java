package com.crackelets.bigfun.platform.booking.mapping;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration("bookingMappingConfiguration")
public class MappingConfiguration {

    @Bean
    public EventMapper eventMapper() {
        return new EventMapper();
    }

    @Bean
    public EventAttendeeMapper eventAttendeeMapper(){
        return new EventAttendeeMapper();
    }

    @Bean
    public EventPaymentMapper eventPaymentMapper(){ return new EventPaymentMapper(); }


}
