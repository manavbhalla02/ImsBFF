package com.ims.bff.authZ.orgDiscovery.config;

import java.time.Clock;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrgDiscoveryBeansConfiguration {

    @Bean
    Clock orgDiscoveryClock() {
        return Clock.systemUTC();
    }
}
