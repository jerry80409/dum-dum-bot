package tw.com.urad.service.fonfood;

import com.github.abola.crawler.CrawlerPack;
import com.linecorp.bot.model.action.PostbackAction;
import com.linecorp.bot.model.message.template.ButtonsTemplate;
import com.linecorp.bot.model.message.template.CarouselTemplate;
import org.apache.commons.logging.impl.SimpleLog;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URISyntaxException;
import java.util.stream.Collectors;

/**
 * Created by jerry on 2017/1/23.
 */
@Service
public class WearService {

    private static final String DEFAULT_USER_AGENT
            = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.95 Safari/537.36";
    private final String WEAR_URI = "http://wear.jp";

    /**
     *
     * @return
     * @throws URISyntaxException
     */
    public ButtonsTemplate crawlerWearType() throws URISyntaxException {
        CrawlerPack.setLoggerLevel(SimpleLog.LOG_LEVEL_WARN);

        Document document = CrawlerPack.start()
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
