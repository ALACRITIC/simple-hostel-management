package org.remipassmoilesel.bookme;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.remipassmoilesel.bookme.configuration.SpringConfiguration;
import org.remipassmoilesel.bookme.utils.TokenManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.IOException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by remipassmoilesel on 24/02/17.
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles(SpringConfiguration.TEST_PROFILE)
public class TokenManagerTest {

    public static final String TEST_MAPPING = Mappings.TEST_ROOT + "token";

    private MockMvc mockMvc;

    @Autowired
    private TokenManagerTestController tokenManagerTestController;

    @Before
    public void setup() throws IOException {
        mockMvc = MockMvcBuilders.standaloneSetup(tokenManagerTestController).build();
    }

    @Test
    public void test() throws Exception {
        mockMvc.perform(get(TEST_MAPPING))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists(TokenManager.DEFAULT_MODEL_TOKEN_NAME));
    }

}
