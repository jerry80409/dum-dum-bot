package tw.com.urad;

import com.linecorp.bot.spring.boot.LineBotProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan
@EnableAutoConfiguration
@EnableConfigurationProperties(LineBotProperties.class)
@SpringBootApplication
public class DumdumBotApplication {

    public static void main(String[] args) {
        SpringApplication.run(DumdumBotApplication.class, args);
    }
}
