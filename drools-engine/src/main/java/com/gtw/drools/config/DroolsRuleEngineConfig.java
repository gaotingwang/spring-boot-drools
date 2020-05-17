package com.gtw.drools.config;

import com.gtw.drools.compent.KieTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author gtw
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(DroolsProperties.class)
@ConditionalOnProperty(value = "spring.drools.enable", havingValue = "true")
public class DroolsRuleEngineConfig {

    @Bean
    @ConditionalOnMissingBean(name = "kieTemplate")
    public KieTemplate kieTemplate(DroolsProperties droolsProperties) {
        KieTemplate kieTemplate = new KieTemplate();
        return kieTemplate;
    }

}
