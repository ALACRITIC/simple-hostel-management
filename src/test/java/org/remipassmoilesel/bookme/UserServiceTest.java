package org.remipassmoilesel.bookme;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.remipassmoilesel.bookme.configuration.CustomConfiguration;
import org.remipassmoilesel.bookme.credentials.Role;
import org.remipassmoilesel.bookme.credentials.User;
import org.remipassmoilesel.bookme.credentials.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertTrue;

/**
 * Created by remipassmoilesel on 15/02/17.
 */

@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles(CustomConfiguration.TEST_PROFILE)
public class UserServiceTest {

    private final Logger logger = LoggerFactory.getLogger(CustomerServiceTest.class);

    @Autowired
    private UserService userService;

    @Test
    public void test() throws IOException {

        userService.clearAllEntities();

        // create fake users
        int userNumber = 10;
        List<String> usernames = new ArrayList<>();
        List<String> passwords = new ArrayList<>();
        for (int i = 0; i < userNumber; i++) {
            usernames.add("PaulBedel" + i);
            passwords.add("PaulBedel//" + i);
            userService.create(usernames.get(i), passwords.get(i), Role.ADMIN);
        }

        // try adding double
        try {
            userService.create(usernames.get(0), passwords.get(0), Role.ADMIN);
            assertTrue("Adding double user test", false);
        } catch (Exception e) {
            logger.error("Error while adding user", e);
        }


        for (int i = 0; i < userNumber; i++) {
            String username = usernames.get(i).toLowerCase();
            String pass = passwords.get(i);

            // retrieve it
            User cred = userService.getByUsername(username); // /!\ in lower case
            assertTrue("User retrieving test", cred != null);

            // test credentials
            User valid = userService.testCredentials(username, pass);
            assertTrue("User retrieving test", valid != null);

            User invalid = userService.testCredentials(username, pass + pass);
            assertTrue("User retrieving test", invalid == null);
        }


    }

}
