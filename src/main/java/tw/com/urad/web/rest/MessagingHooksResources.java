package tw.com.urad.web.rest;

import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.PostbackEvent;
import com.linecorp.bot.model.event.message.LocationMessageContent;
import com.linecorp.bot.model.event.message.StickerMessageContent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.StickerMessage;
import com.linecorp.bot.model.message.TemplateMessage;
import com.linecorp.bot.model.message.template.ButtonsTemplate;
import com.linecorp.bot.model.message.template.CarouselTemplate;
import com.linecorp.bot.model.profile.UserProfileResponse;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;
import lombok.extern.slf4j.Slf4j;
import tw.com.urad.service.LineService;
import tw.com.urad.service.fonfood.FonFoodCralwerService;
import tw.com.urad.service.wear.WearService;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Objects;
import javax.inject.Inject;

/**
 * Created by jerry on 2016/12/28.
 * Default Request Mapping : /callback
 */
@Slf4j
@LineMessageHandler
public class MessagingHooksResources {
    private final static HashMap<String, String> TAIPEI_METROS = new HashMap<>();
    private final static HashMap<String, String> COMMONS = new HashMap<>();
    private final static HashMap<String, String> HOROSCOPE = new HashMap<>();

    static {
        // 地鐵資料庫
        TAIPEI_METROS.put("松山", "捷運松山站");
        TAIPEI_METROS.put("南京三民", "捷運南京三民站");
        TAIPEI_METROS.put("小巨蛋", "捷運台北小巨蛋站");

        // commons conversation
        COMMONS.put("穿搭", null);

        // Susan Miller. HOROSCOPE
        HOROSCOPE.put("摩羯", "Capricorn");
        HOROSCOPE.put("水瓶", "Aquarius");
        HOROSCOPE.put("雙魚", "Pisces");
        HOROSCOPE.put("白羊", "Aries");
        HOROSCOPE.put("金牛", "Taurus");
        HOROSCOPE.put("雙子", "Gemini");
        HOROSCOPE.put("巨蟹", "Cancer");
        HOROSCOPE.put("獅子", "Leo");
        HOROSCOPE.put("處女", "Virgo");
        HOROSCOPE.put("天秤", "Libra");
        HOROSCOPE.put("天蠍", "Scorpio");
        HOROSCOPE.put("射手", "Sagittarius");
    }


    @Inject
    private LineService lineService;
    @Inject
    private FonFoodCralwerService fonFoodCralwerService;
    @Inject
    private WearService wearService;

    /**
     * 接收文字事件
     *
     * @param event
     * @throws IOException
     */
    @EventMapping
    public void handleTextMessageEvent(MessageEvent<TextMessageContent> event)
            throws IOException, URISyntaxException {
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
     * Post-back 事件,
     * 目前只處理 wear.jp WearType Action
     *
     * @param event
     */
    @EventMapping
    public void handlePostbackEvent(PostbackEvent event) {
        String replyToken = event.getReplyToken();
        String data = event.getPostbackContent().getData();
        CarouselTemplate carouselTemplate = wearService.crawlerWearRecommend(data);
        TemplateMessage templateMessage = new TemplateMessage("WEAR", carouselTemplate);
        lineService.reply(replyToken, templateMessage);
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
            throws IOException, URISyntaxException {
        String text = content.getText();
        log.info("Got text message from {}: {}", replyToken, text);

        String userId = event.getSource().getUserId();

        if (Objects.nonNull(userId)) {
            UserProfileResponse userProfile = lineService.getUserProfileResponse(userId);
            log.info("UserProfile: {}", userProfile);

            // 美食地圖
            if (TAIPEI_METROS.containsKey(text)) {
                CarouselTemplate carouselTemplate
                        = fonFoodCralwerService.crawlerRestaurantByMetroName(TAIPEI_METROS.get(text));
                TemplateMessage templateMessage = new TemplateMessage(TAIPEI_METROS.get(text), carouselTemplate);
                lineService.reply(replyToken, templateMessage);
            }

            // 穿搭
            if (COMMONS.containsKey(text)) {
                ButtonsTemplate buttonsTemplate = wearService.crawlerWearTypes();
                TemplateMessage templateMessage = new TemplateMessage("WEAR", buttonsTemplate);
                lineService.reply(replyToken, templateMessage);
            }

            // Sim-Simi Reply, 免費 token 失效
//            Response<SimSimiResponse> simSimiResponse = simSimiService.chat("Hi~").execute();
//            lineService.reply(replyToken, Arrays.asList(
//                    new TextMessage(simSimiResponse.body().getResponse())));
        }
    }
}
