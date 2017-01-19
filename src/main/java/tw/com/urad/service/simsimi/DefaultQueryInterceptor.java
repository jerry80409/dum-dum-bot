package tw.com.urad.service.simsimi;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

/**
 * Created by jerry on 2017/1/13.
 */
public class DefaultQueryInterceptor implements Interceptor {
    private static final String TRIAL_KEY = "ebcad1d5-1d52-4771-9246-3a755400940a";
    /**
     * @see <a href="http://developer.simsimi.com/lclist">http://developer.simsimi.com/lclist</a>
     */
    private static final String LOCATION_LAN = "ch";
    private static final String BAD_WORD_DISCRIMINATOR = "1.0";

    @Override
    public Response intercept(Chain chain) throws IOException {
        HttpUrl keyQuery = chain.request().url()
                .newBuilder()
                .addQueryParameter("key", TRIAL_KEY)
                .addQueryParameter("lc", LOCATION_LAN)
                .addQueryParameter("ft", BAD_WORD_DISCRIMINATOR)
                .build();
        Request request = chain.request().newBuilder().url(keyQuery).build();
        return chain.proceed(request);
    }
}
