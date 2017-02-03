package tw.com.urad.service.simsimi;

import lombok.NonNull;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

/**
 * Created by jerry on 2017/1/13.
 */
public class DefaultQueryInterceptor implements Interceptor {
    private static final String LANG = "ch";
    private static final String FILTER = "1.0";
    private String token;

    /**
     * @param token api token
     */
    public DefaultQueryInterceptor(@NonNull String token) {
        this.token = token;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        HttpUrl keyQuery = chain.request().url()
                .newBuilder()
                .addQueryParameter("key", token)
                .addQueryParameter("lc", LANG)
                .addQueryParameter("ft", FILTER)
                .build();
        Request request = chain.request().newBuilder().url(keyQuery).build();
        return chain.proceed(request);
    }
}
