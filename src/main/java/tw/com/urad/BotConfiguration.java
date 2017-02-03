package tw.com.urad;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tw.com.urad.service.simsimi.SimSimiProperties;
import tw.com.urad.service.simsimi.SimSimiService;
import tw.com.urad.service.simsimi.SimSimiServiceBuilder;

import javax.inject.Inject;

/**
 * Created by jerry on 2017/1/9.
 */
@Configuration
@EnableConfigurationProperties(SimSimiProperties.class)
public class BotConfiguration {

    @Inject
    private SimSimiProperties simSimiProperties;

    /**
     * Create Simsim service
     *
     * @return {@link SimSimiService}
     */
    @Bean
    public SimSimiService simSimService() {
        return SimSimiServiceBuilder
                .create(simSimiProperties.getToken())
                .apiEndPoint(simSimiProperties.getUrl())
                .build();
    }

}
