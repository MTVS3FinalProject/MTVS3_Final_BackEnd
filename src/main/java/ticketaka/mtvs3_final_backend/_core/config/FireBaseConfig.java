package ticketaka.mtvs3_final_backend._core.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FireBaseConfig {

    @Value("${FIREBASE.KEY}")
    private String firebaseKeyPath;
}
