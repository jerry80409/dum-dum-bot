package tw.com.urad.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import retrofit2.Response;
import tw.com.urad.Entities.simsimi.SimSimiResponse;
import tw.com.urad.service.simsimi.SimSimiService;

import javax.inject.Inject;

/**
 * Created by jerry on 2017/1/5.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class SimSimServiceTest {

    @Inject
    private SimSimiService simSimService;

    public SimSimServiceTest() {

    }

    /**
     * Assert Response Code is 200
     */
    @Test
    public void assertSimSimiHttpEndPointIsSuccess() throws Exception {
        Response<SimSimiResponse> response = simSimService.chat("HI").execute();
        Assert.assertEquals(200, response.code());
        System.out.println(response.body());
    }
}
