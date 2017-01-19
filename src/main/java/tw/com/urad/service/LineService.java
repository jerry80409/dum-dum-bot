package tw.com.urad.service;

import com.linecorp.bot.client.LineMessagingService;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.profile.UserProfileResponse;
import com.linecorp.bot.model.response.BotApiResponse;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import retrofit2.Response;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Collections;
import java.util.List;
import javax.inject.Inject;

/**
 * Created by jerry on 2017/1/4.
 */
@Slf4j
@Service
public class LineService {
    @Inject
    private LineMessagingService lineMessagingService;

    /**
     * 取得 user Profile
     *
     * @param userId
     * @return
     * @throws IOException
     */
    public UserProfileResponse getUserProfileResponse(String userId) throws IOException {
        Response<UserProfileResponse> response = lineMessagingService
                .getProfile(userId)
                .execute();

        if (response.isSuccessful()) {
            return response.body();

        } else {
            log.info("Get User Profile Fail : {}", response.errorBody().toString());
            return null;
        }

    }

    /**
     * 回覆 messages
     *
     * @param replyToken
     * @param messages
     */
    public void reply(@NonNull String replyToken, @NonNull List<Message> messages) {
        try {
            Response<BotApiResponse> apiResponse = lineMessagingService
                    .replyMessage(new ReplyMessage(replyToken, messages))
                    .execute();
            log.info("Sent messages: {}", apiResponse);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    /**
     * For Template 類型的 Message
     *
     * @param replyToken
     * @param message
     */
    public void reply(@NonNull String replyToken, @NonNull Message message) {
        reply(replyToken, Collections.singletonList(message));
    }
}
