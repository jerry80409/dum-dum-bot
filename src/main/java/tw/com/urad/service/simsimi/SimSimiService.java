package tw.com.urad.service.simsimi;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import tw.com.urad.Entities.simsimi.SimSimiResponse;

/**
 * Created by jerry on 2017/1/5.
 */
public interface SimSimiService {

    /**
     * @param msg
     * @return SimSimiResponse {@link SimSimiResponse}
     * @see <a href="http://developer.simsimi.com/api">http://developer.simsimi.com/api</a>
     */
    @GET("request.p")
    Call<SimSimiResponse> chat(@Query("text") String msg);

}
