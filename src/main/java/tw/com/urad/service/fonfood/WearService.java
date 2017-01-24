package tw.com.urad.service.fonfood;

import com.github.abola.crawler.CrawlerPack;
import com.linecorp.bot.model.action.PostbackAction;
import com.linecorp.bot.model.action.URIAction;
import com.linecorp.bot.model.message.template.ButtonsTemplate;
import com.linecorp.bot.model.message.template.CarouselColumn;
import com.linecorp.bot.model.message.template.CarouselTemplate;
import org.apache.commons.logging.impl.SimpleLog;
import org.joda.time.DateTime;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by jerry on 2017/1/23.
 */
@Service
public class WearService {
    private static final String DEFAULT_USER_AGENT
            = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.95 Safari/537.36";
    private static final Date COOKIE_EXPRESS = new DateTime().plusDays(1).toDate();
    private final String WEAR_URI = "http://wear.jp/";

    /**
     * Crawler wear Recommend
     * todo fix cookie reject
     *
     * @param switchUri
     * @return
     */
    public CarouselTemplate crawlerWearRecommend(String switchUri) {
        CrawlerPack.setLoggerLevel(SimpleLog.LOG_LEVEL_WARN);
        Elements wearRecommends = CrawlerPack.start()
                .addCookie(".wear.jp", "AF%5FObjectID", "", "/", COOKIE_EXPRESS, true)
                .setUserAgent(DEFAULT_USER_AGENT)
                .getFromHtml(WEAR_URI + switchUri)
                .select("#recommend > ul > li");

        List<CarouselColumn> carouselColumns = wearRecommends.stream()
                .map(recommend -> {
                    try {
                        return new CarouselColumn(
                                fixHttpsURI(recommend.select(".image_container > .img > a > img").attr("data-original")),
                                recommend.select(".profile > .content > .main > p").text(),
                                recommend.select(".profile > .content > .txt > p").text(),
                                Arrays.asList(new URIAction(
                                        recommend.select(".image_container > .item > .container > .cnt > .cnt_container > .brand").text(),
                                        WEAR_URI + recommend.select(".image_container > .img > a").attr("href")
                                ))
                        );
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                        return null;
                    }
                }).limit(3)
                .collect(Collectors.toList());

        return new CarouselTemplate(carouselColumns);
    }

    /**
     * Crawler wear.jp index page, Crawler wear type navigation bar
     * todo fix cookie reject
     *
     * @return
     * @throws URISyntaxException
     */
    public ButtonsTemplate crawlerWearTypes() throws URISyntaxException {
        CrawlerPack.setLoggerLevel(SimpleLog.LOG_LEVEL_WARN);

        Document document = CrawlerPack.start()
                .addCookie(".wear.jp", "AF%5FObjectID", "", "/", COOKIE_EXPRESS, true)
                .setUserAgent(DEFAULT_USER_AGENT)
                .getFromHtml(WEAR_URI);

        Elements wearTypes = document.select("#switch_sex_type > nav > ul > li");
        Elements images = document.select("#content .img > a > img");

        ButtonsTemplate buttonsTemplate = new ButtonsTemplate(
                fixHttpsURI(images.get(0).attr("src")),                      // 拿第一張圖片當 Button Template
                "WEAR",
                "wear.jp",
                wearTypes.stream()
                        .map(type -> new PostbackAction(type.text(), type.select("a").attr("href")))
                        .limit(4)
                        .collect(Collectors.toList())
        );

        return buttonsTemplate;
    }

    /**
     * 強制轉 https
     *
     * @param uri
     * @return
     * @throws URISyntaxException
     */
    public String fixHttpsURI(String uri) throws URISyntaxException {
        return UriComponentsBuilder.fromHttpUrl(uri)
                .scheme("https")
                .build().toString();
    }
}
