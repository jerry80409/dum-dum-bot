package tw.com.urad.service.fonfood;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.inject.Inject;

/**
 * Created by jerry on 2017/1/20.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class FonFoodCralwerServiceTest {

    @Inject
    private FonFoodCrawlerService fonFoodCralwerService;

    // TODO: 2017/1/20  assert test
    @Test
    public void test() {
        fonFoodCralwerService.crawlerRestaurantByMetroName("捷運台北小巨蛋站");
    }
}
