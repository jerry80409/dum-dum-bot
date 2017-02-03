package tw.com.urad.service.simsimi;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.validation.constraints.NotNull;

/**
 * Created by jerry on 2017/2/2.
 */
@Data
@ConfigurationProperties(prefix = "simsimi.api")
public class SimSimiProperties {

    @NotNull
    private String token;

    @NotNull
    private String url;

}
