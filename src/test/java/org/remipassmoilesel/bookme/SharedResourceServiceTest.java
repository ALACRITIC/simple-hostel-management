package org.remipassmoilesel.bookme;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.remipassmoilesel.bookme.sharedresources.SharedResource;
import org.remipassmoilesel.bookme.sharedresources.SharedResourceService;
import org.remipassmoilesel.bookme.sharedresources.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


/**
 * Created by remipassmoilesel on 13/02/17.
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles(CustomConfiguration.TEST_PROFILE)
public class SharedResourceServiceTest {

    private final Logger logger = LoggerFactory.getLogger(SharedResourceServiceTest.class);

    @Autowired
    private SharedResourceService sharedResourceService;

    @Test
    public void test() throws IOException {

        int resourcesNumber = 10;
        ArrayList<SharedResource> resources = new ArrayList<>();
        for (int i = 0; i < resourcesNumber; i++) {
            SharedResource resource = new SharedResource("A" + i, 2, "Comment " + i, Type.ROOM);
            sharedResourceService.createResource(resource);

            resources.add(resource);
        }

        // basic equality test
        assertTrue("Equality test 1", resources.get(0).equals(resources.get(0)));
        assertFalse("Equality test 2", resources.get(0).equals(resources.get(1)));

        // retrieving test
        for (SharedResource resA : resources) {
            SharedResource resB = sharedResourceService.getById(resA.getId());
            assertTrue("Database retrieving test: " + resA + " / " + resB, resA.equals(resB));
        }

        // refresh test
        String newName = "Not a resource name";
        resources.get(0).setName(newName);
        sharedResourceService.refresh(resources.get(0));
        assertFalse("Refresh test", resources.get(0).getName().equals(newName));

    }

}
