package com.deliveryth.delivery_api.health;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component 
public class DatabaseHealthIndicator implements HealthIndicator {

    @Override
    public Health health() {
      
        boolean conexaoOk = verificarBancoDeDados(); 

        if (conexaoOk) {
            return Health.up()
                         .withDetail("status", "Conectado ao banco de dados")
                         .withDetail("sistema", "Delivery API")
                         .build();
        } else {
            return Health.down()
                         .withDetail("erro", "Não foi possível conectar ao banco")
                         .build();
        }
    }

    private boolean verificarBancoDeDados() {
        return true; 
    }
}
