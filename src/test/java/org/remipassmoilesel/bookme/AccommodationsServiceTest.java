package org.remipassmoilesel.bookme;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.remipassmoilesel.bookme.accommodations.Accommodation;
import org.remipassmoilesel.bookme.accommodations.AccommodationService;
import org.remipassmoilesel.bookme.configuration.SpringConfiguration;
import org.remipassmoilesel.bookme.utils.testdata.TestDataFactory;
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
@ActiveProfiles(SpringConfiguration.TEST_PROFILE)
public class AccommodationsServiceTest {

    private final Logger logger = LoggerFactory.getLogger(AccommodationsServiceTest.class);

    @Autowired
    private AccommodationService accommodationService;

    @Test
    public void test() throws IOException {

        accommodationService.clearAllEntities();
        ArrayList<Accommodation> accommodations = TestDataFactory.createAccommodations(10, accommodationService);

        // basic equality test
        assertTrue("Equality test 1", accommodations.get(0).equals(accommodations.get(0)));
        assertFalse("Equality test 2", accommodations.get(0).equals(accommodations.get(1)));

        // retrieving test
        for (Accommodation resA : accommodations) {
            Accommodation resB = accommodationService.getById(resA.getId());
            assertTrue("Database retrieving test: " + resA + " / " + resB, resA.equals(resB));
        }

        // refresh test
        String newName = "Not an accommodation name";
        accommodations.get(0).setName(newName);
        accommodationService.refresh(accommodations.get(0));
        assertFalse("Refresh test", accommodations.get(0).getName().equals(newName));

        // mark all as deleted
        for (Accommodation res : accommodations) {
            accommodationService.markAsDeleted(res);
            assertTrue("Delete resource test", res.isDeleted());
        }

        assertTrue("Delete resources test", accommodationService.getAll().size() == 0);
        assertTrue("Delete resources test", accommodationService.getAll(-1l, -1l, true).size() == 10);

    }

}
