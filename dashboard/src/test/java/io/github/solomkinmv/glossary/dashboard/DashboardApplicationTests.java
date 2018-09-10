package io.github.solomkinmv.glossary.dashboard;

import com.netflix.discovery.EurekaClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class DashboardApplicationTests {
    @MockBean
    EurekaClient eurekaClient;

    @Test
    public void contextLoads() {
    }

}
