package tw.com.urad.web.rest;

import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.LocationMessageContent;
import com.linecorp.bot.model.event.message.StickerMessageContent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.StickerMessage;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.profile.UserProfileResponse;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;
import lombok.extern.slf4j.Slf4j;
import retrofit2.Response;
import tw.com.urad.Entities.simsimi.SimSimiResponse;
import tw.com.urad.service.LineService;
import tw.com.urad.service.simsimi.SimSimiService;

import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;
import javax.inject.Inject;

/**
 * Created by jerry on 2016/12/28.
 * Default Request Mapping : /callback
 */
@Slf4j
@LineMessageHandler
public class MessagingHooksResources {
    @Inject
    private LineService lineService;
    @Inject
    private SimSimiService simSimiService;

    /**
     * 接收文字事件
     *
     * @param event
     * @throws IOException
     */
    @EventMapping
    public void handleTextMessageEvent(MessageEvent<TextMessageContent> event) throws IOException {
        TextMessageContent message = event.getMessage();
        handleTextContent(event.getReplyToken(), event, message);
    }

    /**
     * 接收貼圖事件
     *
     * @param event
     */
    @EventMapping
    public void handleStickerMessageEvent(MessageEvent<StickerMessageContent> event) {
        handleSticker(event.getReplyToken(), event.getMessage());
    }

    /**
     * 接收定位事件
     *
     * @param event
     */
    @EventMapping
    public void handleLocationMessageEvent(MessageEvent<LocationMessageContent> event) {
        LocationMessageContent locationMessage = event.getMessage();
//        lineService.reply(event.getReplyToken(), new LocationMessage(
//                locationMessage.getTitle(),
//                locationMessage.getAddress(),
//                locationMessage.getLatitude(),
//                locationMessage.getLongitude()
//        ));
    }

    /**
     * 回應貼圖
     *
     * @param replyToken
     * @param content
     */
    private void handleSticker(String replyToken, StickerMessageContent content) {
        lineService.reply(replyToken, new StickerMessage(
                content.getPackageId(), content.getStickerId())
        );
    }

    /**
     * 用 Simsimi Service 做文字訊息回覆
     *
     * @param replyToken
     * @param event
     * @param content
     * @throws IOException
     */
    private void handleTextContent(String replyToken, Event event, TextMessageContent content)
            throws IOException {
        String text = content.getText();
        log.info("Got text message from {}: {}", replyToken, text);

        String userId = event.getSource().getUserId();

        if (Objects.nonNull(userId)) {
            UserProfileResponse userProfile = lineService.getUserProfileResponse(userId);
            log.info("UserProfile: {}", userProfile);

            // Sim-Simi Reply
            Response<SimSimiResponse> simSimiResponse = simSimiService.chat("Hi~").execute();
            lineService.reply(replyToken, Arrays.asList(
                    new TextMessage(simSimiResponse.body().getResponse())));
        }
    }
}
