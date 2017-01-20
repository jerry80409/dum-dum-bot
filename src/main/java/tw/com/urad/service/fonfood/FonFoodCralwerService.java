package tw.com.urad.service.fonfood;

import com.github.abola.crawler.CrawlerPack;
import com.linecorp.bot.model.action.URIAction;
import com.linecorp.bot.model.message.template.CarouselColumn;
import com.linecorp.bot.model.message.template.CarouselTemplate;
import org.apache.commons.logging.impl.SimpleLog;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by jerry on 2017/1/20.
 */
@Service
public class FonFoodCralwerService {

    private static final String DEFAULT_USER_AGENT
            = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.95 Safari/537.36";
    private static final String FONFOOD_URI = "http://www.fonfood.com/";

    public CarouselTemplate crawlerRestaurantByMetroName(String metroName) {
        CrawlerPack.setLoggerLevel(SimpleLog.LOG_LEVEL_ERROR);
        Elements restaurants = CrawlerPack.start()
                .setUserAgent(DEFAULT_USER_AGENT)
                .getFromHtml(FONFOOD_URI + metroName)
                .select("#tab_content_loc > .storeListItem");

        List<CarouselColumn> carouselColumns = restaurants.stream()
                .filter(element -> !element
                        .select(".imgArea > img").attr("src")                           // filter no image
                        .contains("no_image.jpg"))
                .map(element -> new CarouselColumn(
                        element.select(".bodyBlock > .imgArea > a > img").attr("src"),  // img URL
                        element.select(".titleBlock > div > h2 > a").text(),            // Title
                        element.select(".bodyBlock > .infoArea > p").get(0).text(),     // description
                        Arrays.asList(new URIAction(                                    // action button
                                element.select(".infoArea > p > a").text(),
                                element.select(".infoArea > p > a").attr("href")
                        ))
                )).limit(3)
                .collect(Collectors.toList());

        return new CarouselTemplate(carouselColumns);
    }
}
