package org.remipassmoilesel.bookme;

import org.hamcrest.Matchers;
import org.joda.time.DateTime;
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
import org.remipassmoilesel.bookme.utils.Utils;
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

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
    private DateTime beginTestPeriod;
    private DateTime endTestPeriod;

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
        for (int i = 0; i < 6; i++) {
            SharedResource resource = new SharedResource("Room " + i, 2, 5.45, "", Type.ROOM, Color.blue);
            sharedResources.add(resource);
            sharedResourceService.create(resource);
        }
        for (int i = 0; i < 3; i++) {
            SharedResource resource = new SharedResource("Bed " + i, 1, 5.45, "", Type.BED, Color.blue);
            sharedResources.add(resource);
            sharedResourceService.create(resource);
        }

        // create fake reservations
        reservationService.clearAllEntities();

        beginTestPeriod = new DateTime();
        endTestPeriod = beginTestPeriod.plusDays(30);

        // add special reservations to test against test period (TP)
        // 1: begin before TP and finish in TP
        reservationService.create(customers.get(0), sharedResources.get(0), 2, beginTestPeriod.minusDays(2).toDate(), beginTestPeriod.plusDays(2).toDate());
        // 2: begin in TP and finish after TP
        reservationService.create(customers.get(0), sharedResources.get(1), 2, beginTestPeriod.plusDays(2).toDate(), endTestPeriod.plusDays(2).toDate());
        // 3: begin before TP and finish after TP
        reservationService.create(customers.get(0), sharedResources.get(2), 2, beginTestPeriod.minusDays(2).toDate(), endTestPeriod.plusDays(2).toDate());
        // 4: begin after TP and finish before end of TP
        reservationService.create(customers.get(0), sharedResources.get(3), 2, beginTestPeriod.plusDays(2).toDate(), endTestPeriod.minusDays(2).toDate());
        // 5: same period
        reservationService.create(customers.get(0), sharedResources.get(4), 2, beginTestPeriod.toDate(), endTestPeriod.toDate());

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

    @Test
    public void getAvailableRooms() throws Exception {
        mockMvc.perform(get(Mappings.RESERVATIONS_RESOURCES_AVAILABLE_JSON_GET)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("start", beginTestPeriod.toString("dd/MM/YYYY HH:mm"))
                .param("end", endTestPeriod.toString("dd/MM/YYYY HH:mm"))
                .param("places", String.valueOf(2)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.*", hasSize(equalTo(1))));
    }

    @Test
    public void addReservation() throws Exception {

        // ask a form and a token
        Map<String, Object> validFormModel = mockMvc.perform(get(Mappings.RESERVATIONS_FORM)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", "-1"))
                .andExpect(status().isOk())
                .andExpect(model().attribute(TokenManager.DEFAULT_MODEL_TOKEN_NAME,
                        Matchers.not(Matchers.isEmptyString())))
                .andExpect(model().attribute("errorMessage",
                        Matchers.not(Matchers.isEmptyString())))
                .andReturn().getModelAndView().getModel();

        String firstname = "firstname";
        String lastname = "lastname";
        String phoneNumber = "+123456789";
        String phoneNumber2 = "+12345678910";
        String customerId = String.valueOf(customers.get(0).getId());
        String places = String.valueOf(2);
        String datebegin = beginTestPeriod.toString("dd/MM/YYYY HH:mm");
        String dateend = endTestPeriod.toString("dd/MM/YYYY HH:mm");
        String sharedResourceId = String.valueOf(sharedResources.get(1).getId());
        String reservationId = "-1";
        String comment = Utils.generateLoremIpsum(200);
        String paid = String.valueOf(false);
        String sessionTokenName = TokenManager.generateSessionTokenName(ReservationController.TOKEN_ATTR_SESSION_PREFIX);
        String formToken = String.valueOf(validFormModel.get(TokenManager.DEFAULT_MODEL_TOKEN_NAME));

        // new one
        mockMvc.perform(post(Mappings.RESERVATIONS_FORM)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .sessionAttr(sessionTokenName, formToken)
                .param("customerFirstname", firstname)
                .param("customerLastname", lastname)
                .param("customerPhonenumber", phoneNumber)
                .param("customerId", customerId)
                .param("places", places)
                .param("begin", datebegin)
                .param("end", dateend)
                .param("sharedResourceId", sharedResourceId)
                .param("reservationId", reservationId)
                .param("comment", comment)
                .param("paid", paid)
                .param("token", formToken))
                .andExpect(status().isOk())
                .andExpect(model().hasNoErrors())
                .andExpect(model().attribute("errorMessage", isEmptyString()));

        // create a reservation
        Reservation reservation = new Reservation(customers.get(0), sharedResources.get(0), 1, new Date(), new Date());
        reservationService.create(reservation);

        // update existing
        mockMvc.perform(post(Mappings.RESERVATIONS_FORM)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .sessionAttr(sessionTokenName, formToken)
                .param("customerFirstname", firstname)
                .param("customerLastname", lastname)
                .param("customerPhonenumber", phoneNumber2)
                .param("customerId", customerId)
                .param("places", places)
                .param("begin", datebegin)
                .param("end", dateend)
                .param("sharedResourceId", sharedResourceId)
                .param("reservationId", String.valueOf(reservation.getId()))
                .param("comment", Utils.generateLoremIpsum(500))
                .param("paid", "true")
                .param("token", formToken))
                .andExpect(status().isOk())
                .andExpect(model().hasNoErrors())
                .andExpect(model().attribute("errorMessage", isEmptyString()));

        assertTrue(reservationService.getById(reservation.getId()).isPaid() == true);
    }
}