package tw.com.urad;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tw.com.urad.service.simsimi.SimSimiService;
import tw.com.urad.service.simsimi.SimSimiServiceBuilder;

/**
 * Created by jerry on 2017/1/9.
 */
@Configuration
public class BotConfiguration {

    /**
     * Create Simsim service
     *
     * @return {@link SimSimiService}
     */
    @Bean
    public SimSimiService simSimService() {
        return SimSimiServiceBuilder.create().build();
    }

}
