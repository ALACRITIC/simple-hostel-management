package org.remipassmoilesel.bookme;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.remipassmoilesel.bookme.configuration.CustomConfiguration;
import org.remipassmoilesel.bookme.controllers.ReservationController;
import org.remipassmoilesel.bookme.customers.Customer;
import org.remipassmoilesel.bookme.customers.CustomerService;
import org.remipassmoilesel.bookme.reservations.Reservation;
import org.remipassmoilesel.bookme.reservations.ReservationService;
import org.remipassmoilesel.bookme.sharedresources.SharedResource;
import org.remipassmoilesel.bookme.sharedresources.SharedResourceService;
import org.remipassmoilesel.bookme.sharedresources.Type;
import org.remipassmoilesel.bookme.utils.TokenManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by remipassmoilesel on 22/02/17.
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles(CustomConfiguration.TEST_PROFILE)
public class ReservationControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private SharedResourceService sharedResourceService;

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private ReservationController reservationController;

    private ArrayList<Customer> customers;
    private ArrayList<SharedResource> sharedResources;

    @Before
    public void setup() throws IOException {

        mockMvc = MockMvcBuilders.standaloneSetup(reservationController).build();

        // clear old customers
        customerService.clearAllEntities();

        // create fake customers
        customers = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            customers.add(new Customer("Jean " + i, "Paul " + i, "+0015513131" + i));
        }

        // add it
        for (Customer customer : customers) {
            customerService.create(customer);
        }

        sharedResourceService.clearAllEntities();

        sharedResources = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            SharedResource resource = new SharedResource("Bed " + i, 1, 5.45, "", Type.BED, Color.blue);
            sharedResources.add(resource);
            sharedResourceService.create(resource);
        }
        for (int i = 0; i < 3; i++) {
            SharedResource resource = new SharedResource("Room " + i, 1, 5.45, "", Type.ROOM, Color.blue);
            sharedResources.add(resource);
            sharedResourceService.create(resource);
        }

    }

    @Test
    public void deleteReservation() throws Exception {

        // create a reservation
        Reservation res = new Reservation(customers.get(0), sharedResources.get(0), 1, new Date(), new Date());
        reservationService.create(res);

        // ask a token
        Map<String, Object> validFormModel = mockMvc.perform(get(Mappings.RESERVATIONS_FORM)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", String.valueOf(res.getId())))
                .andExpect(status().isOk())
                .andExpect(model().attribute(TokenManager.DEFAULT_MODEL_TOKEN_NAME,
                        Matchers.not(Matchers.isEmptyString())))
                .andExpect(model().attribute("errorMessage",
                        Matchers.not(Matchers.isEmptyString())))
                .andReturn().getModelAndView().getModel();

        // delete reservation
        String formToken = String.valueOf(validFormModel.get(TokenManager.DEFAULT_MODEL_TOKEN_NAME));
        mockMvc.perform(get(Mappings.RESERVATIONS_DELETE)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", String.valueOf(res.getId()))
                .param("token", formToken)
                .sessionAttr(TokenManager.generateSessionTokenName(ReservationController.TOKEN_ATTR_SESSION_PREFIX),
                        formToken))
                .andExpect(status().isOk());

        // check for reservation
        assertTrue(reservationService.getById(res.getId()) == null);
    }
}