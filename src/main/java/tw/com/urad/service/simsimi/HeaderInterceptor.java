package tw.com.urad.service.simsimi;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

/**
 * Created by jerry on 2017/1/5.
 * 這邊用 攔截器 替 Request 附加 Header
 * 附加 User-Agent
 * <p>
 */
public class HeaderInterceptor implements Interceptor {
    private static final String USER_AGENT = "urad-sdk-java";

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request().newBuilder()
                .addHeader("User-Agent", USER_AGENT)
                .build();
        return chain.proceed(request);
    }
}
