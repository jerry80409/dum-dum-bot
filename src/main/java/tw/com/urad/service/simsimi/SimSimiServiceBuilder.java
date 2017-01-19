package tw.com.urad.service.simsimi;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.linecorp.bot.client.LineMessagingService;
import com.linecorp.bot.client.LineMessagingServiceBuilder;
import lombok.NonNull;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * This Version For Trail Key
 */
public class SimSimiServiceBuilder {
    public static final String DEFAULT_API_END_POINT = "http://sandbox.api.simsimi.com";

    public static final long DEFAULT_CONNECT_TIMEOUT = 10_000;
    public static final long DEFAULT_READ_TIMEOUT = 10_000;
    public static final long DEFAULT_WRITE_TIMEOUT = 10_000;

    private String apiEndPoint = DEFAULT_API_END_POINT;
    private long connectTimeout = DEFAULT_CONNECT_TIMEOUT;
    private long readTimeout = DEFAULT_READ_TIMEOUT;
    private long writeTimeout = DEFAULT_WRITE_TIMEOUT;

    private List<Interceptor> interceptors = new ArrayList<>();

    private OkHttpClient.Builder okHttpClientBuilder;
    private Retrofit.Builder retrofitBuilder;

    private SimSimiServiceBuilder(List<Interceptor> interceptors) {
        this.interceptors.addAll(interceptors);
    }

    /**
     * Create a new {@link SimSimiServiceBuilder}
     */
    public static SimSimiServiceBuilder create() {
        return new SimSimiServiceBuilder(defaultInterceptors());
    }

    private static List<Interceptor> defaultInterceptors() {
        final Logger slf4jLogger = LoggerFactory.getLogger("tw.com.dum");
        final HttpLoggingInterceptor httpLoggingInterceptor =
                new HttpLoggingInterceptor(message -> slf4jLogger.info("{}", message));
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        return Arrays.asList(
                new HeaderInterceptor(),
                new DefaultQueryInterceptor(),
                httpLoggingInterceptor
        );
    }


    private static Retrofit.Builder createDefaultRetrofitBuilder() {
        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        // Register JSR-310(java.time.temporal.*) module and read number as millsec.
        objectMapper.registerModule(new JavaTimeModule())
                .configure(DeserializationFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS, false);

        return new Retrofit.Builder()
                .addConverterFactory(JacksonConverterFactory.create(objectMapper));
    }

    /**
     * 允許彈性的更改 api end point
     * Set apiEndPoint.
     */
    public SimSimiServiceBuilder apiEndPoint(@NonNull String apiEndPoint) {
        this.apiEndPoint = apiEndPoint;
        return this;
    }

    /**
     * 允許彈性的更改 connect timeout
     * Set connectTimeout in milliseconds.
     */
    public SimSimiServiceBuilder connectTimeout(long connectTimeout) {
        this.connectTimeout = connectTimeout;
        return this;
    }

    /**
     * 允許彈性的更改 read timeout
     * Set readTimeout in milliseconds.
     */
    public SimSimiServiceBuilder readTimeout(long readTimeout) {
        this.readTimeout = readTimeout;
        return this;
    }

    /**
     * 允許彈性的更改 write timeout
     * Set writeTimeout in milliseconds.
     */
    public SimSimiServiceBuilder writeTimeout(long writeTimeout) {
        this.writeTimeout = writeTimeout;
        return this;
    }

    /**
     * 允許彈性的增加 Interceptor
     * Add interceptor
     */
    public SimSimiServiceBuilder addInterceptor(Interceptor interceptor) {
        this.interceptors.add(interceptor);
        return this;
    }

    /**
     * 允許彈性的增加 First Interceptor
     * Add interceptor first
     */
    public SimSimiServiceBuilder addInterceptorFirst(Interceptor interceptor) {
        this.interceptors.add(0, interceptor);
        return this;
    }

    /**
     * <p>If you want to use your own setting, specify {@link OkHttpClient.Builder} instance.</p>
     */
    public SimSimiServiceBuilder okHttpClientBuilder(@NonNull OkHttpClient.Builder okHttpClientBuilder) {
        this.okHttpClientBuilder = okHttpClientBuilder;
        return this;
    }

    /**
     * <p>If you want to use your own setting, specify {@link Retrofit.Builder} instance.</p>
     * <p>
     * <p>ref: {@link LineMessagingServiceBuilder#createDefaultRetrofitBuilder()} ()}.</p>
     */
    public SimSimiServiceBuilder retrofitBuilder(@NonNull Retrofit.Builder retrofitBuilder) {
        this.retrofitBuilder = retrofitBuilder;
        return this;
    }

    /**
     * Creates a new {@link LineMessagingService}.
     */
    public SimSimiService build() {
        if (okHttpClientBuilder == null) {
            okHttpClientBuilder = new OkHttpClient.Builder();
            interceptors.forEach(okHttpClientBuilder::addInterceptor);
        }
        okHttpClientBuilder
                .connectTimeout(connectTimeout, TimeUnit.MILLISECONDS)
                .readTimeout(readTimeout, TimeUnit.MILLISECONDS)
                .writeTimeout(writeTimeout, TimeUnit.MILLISECONDS);
        final OkHttpClient okHttpClient = okHttpClientBuilder.build();

        if (retrofitBuilder == null) {
            retrofitBuilder = createDefaultRetrofitBuilder();
        }
        retrofitBuilder.client(okHttpClient);
        retrofitBuilder.baseUrl(apiEndPoint);
        final Retrofit retrofit = retrofitBuilder.build();

        return retrofit.create(SimSimiService.class);
    }
}
