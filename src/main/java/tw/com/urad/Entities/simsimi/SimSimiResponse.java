package tw.com.urad.Entities.simsimi;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

/**
 * Created by jerry on 2017/1/10.
 * Sample :
 * {"response":"hola","id":"32949118","result":100,"msg":"OK."}
 */
@Value
public class SimSimiResponse {
    private final String id;
    private final String response;
    private final int result;
    private final String msg;

    public SimSimiResponse(@JsonProperty("id") String id,
                           @JsonProperty("result") int result,
                           @JsonProperty("msg") String msg,
                           @JsonProperty("response") String response) {
        this.id = id;
        this.result = result;
        this.msg = msg;
        this.response = response;
    }
}
