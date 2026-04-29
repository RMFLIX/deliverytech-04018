package com.deliveryth.delivery_api.health;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class CustomHealthIndicator implements HealthIndicator {
    
    @Override
    public Health health(){
        boolean serviceOk = true;

        if(serviceOk){
            return Health.up()
            .withDetail("deliveryApi", "Funcionando")
            .build();
        }
        return Health.down()
        .withDetail("deliveryApi", "Falhou")
        .build();
    }
}
