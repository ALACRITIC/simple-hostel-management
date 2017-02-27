package org.remipassmoilesel.bookme;

import org.hamcrest.Matchers;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.remipassmoilesel.bookme.accommodations.Accommodation;
import org.remipassmoilesel.bookme.accommodations.AccommodationService;
import org.remipassmoilesel.bookme.configuration.SpringConfiguration;
import org.remipassmoilesel.bookme.controllers.ReservationController;
import org.remipassmoilesel.bookme.customers.Customer;
import org.remipassmoilesel.bookme.customers.CustomerService;
import org.remipassmoilesel.bookme.reservations.Reservation;
import org.remipassmoilesel.bookme.reservations.ReservationService;
import org.remipassmoilesel.bookme.utils.TokenManager;
import org.remipassmoilesel.bookme.utils.Utils;
import org.remipassmoilesel.bookme.utils.testdata.TestDataFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

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
@ActiveProfiles(SpringConfiguration.TEST_PROFILE)
public class ReservationControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private AccommodationService accommodationsService;

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private ReservationController reservationController;

    private ArrayList<Customer> customers;
    private ArrayList<Accommodation> accommodations;
    private DateTime beginTestPeriod;
    private DateTime endTestPeriod;

    @Before
    public void setup() throws IOException {

        mockMvc = MockMvcBuilders.standaloneSetup(reservationController).build();

        // clear entities
        customerService.clearAllEntities();
        accommodationsService.clearAllEntities();
        reservationService.clearAllEntities();

        // create fake customers
        customers = TestDataFactory.createCustomers(10, customerService);
        accommodations = TestDataFactory.createAccommodations(10, accommodationsService);

        // create fake reservations
        beginTestPeriod = new DateTime();
        endTestPeriod = beginTestPeriod.plusDays(30);

        TestDataFactory.createReservationsForIntervalTest(reservationService, customers,
                accommodations, beginTestPeriod, endTestPeriod);
    }

    @Test
    public void deleteReservation() throws Exception {

        // create a reservation
        Reservation res = new Reservation(customers.get(0), accommodations.get(0), 1, new Date(), new Date());
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
        mockMvc.perform(get(Mappings.RESERVATIONS_ACCOMMODATIONS_AVAILABLE_JSON_GET)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("start", beginTestPeriod.toString("dd/MM/YYYY HH:mm"))
                .param("end", endTestPeriod.toString("dd/MM/YYYY HH:mm"))
                .param("places", String.valueOf(2)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.*", hasSize(equalTo(5))));
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
        String accomId = String.valueOf(accommodations.get(1).getId());
        String reservationId = "-1";
        String comment = Utils.generateLoremIpsum(200);
        String paid = String.valueOf(false);
        String totalPrice = "48.5";
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
                .param("accommodationId", accomId)
                .param("reservationId", reservationId)
                .param("comment", comment)
                .param("paid", paid)
                .param("totalPrice", totalPrice)
                .param("token", formToken))
                .andExpect(status().isOk())
                .andExpect(model().hasNoErrors())
                .andExpect(model().attribute("errorMessage", isEmptyString()));

        // create a reservation
        Reservation reservation = new Reservation(customers.get(0), accommodations.get(0), 1, new Date(), new Date());
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
                .param("accommodationId", accomId)
                .param("reservationId", String.valueOf(reservation.getId()))
                .param("comment", Utils.generateLoremIpsum(500))
                .param("paid", "true")
                .param("totalPrice", totalPrice)
                .param("token", formToken))
                .andExpect(status().isOk())
                .andExpect(model().hasNoErrors())
                .andExpect(model().attribute("errorMessage", isEmptyString()));

        assertTrue(reservationService.getById(reservation.getId()).isPaid() == true);
    }
}