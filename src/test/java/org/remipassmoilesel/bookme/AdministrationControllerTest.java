package org.remipassmoilesel.bookme;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.remipassmoilesel.bookme.accommodations.Accommodation;
import org.remipassmoilesel.bookme.accommodations.AccommodationService;
import org.remipassmoilesel.bookme.configuration.CustomConfiguration;
import org.remipassmoilesel.bookme.controllers.AdministrationController;
import org.remipassmoilesel.bookme.customers.Customer;
import org.remipassmoilesel.bookme.customers.CustomerService;
import org.remipassmoilesel.bookme.reservations.Reservation;
import org.remipassmoilesel.bookme.reservations.ReservationService;
import org.remipassmoilesel.bookme.services.MerchantService;
import org.remipassmoilesel.bookme.services.MerchantServiceService;
import org.remipassmoilesel.bookme.services.MerchantServiceType;
import org.remipassmoilesel.bookme.services.MerchantServiceTypesService;
import org.remipassmoilesel.bookme.utils.testdata.TestDataFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;

import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by remipassmoilesel on 22/02/17.
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles(CustomConfiguration.TEST_PROFILE)
public class AdministrationControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private AccommodationService accommodationService;

    @Autowired
    private MerchantServiceService merchantServiceService;

    @Autowired
    private MerchantServiceTypesService merchantServiceTypesService;

    @Autowired
    private AdministrationController administrationController;

    private ArrayList<Customer> customers;
    private ArrayList<MerchantServiceType> serviceTypes;
    private ArrayList<Accommodation> accommodations;
    private ArrayList<MerchantService> services;
    private ArrayList<Reservation> reservations;

    @Before
    public void setup() throws Exception {

        mockMvc = MockMvcBuilders.standaloneSetup(administrationController).build();

        // clear old customers
        customerService.clearAllEntities();
        merchantServiceService.clearAllEntities();
        reservationService.clearAllEntities();
        merchantServiceTypesService.clearAllEntities();
        accommodationService.clearAllEntities();

        // create fake customers
        customers = TestDataFactory.createCustomers(20, customerService);

        // create fake accommodations
        accommodations = TestDataFactory.createAccommodations(10, accommodationService);

        // create fake reservations
        reservations = TestDataFactory.createReservations(30, accommodations,
                customers, new DateTime(), reservationService);

        // create fake service types
        serviceTypes = TestDataFactory.createServiceTypes(merchantServiceTypesService);

        // create fake service
        services = TestDataFactory.createServices(30, customers, serviceTypes,
                new DateTime(), merchantServiceService);

    }

    @Test
    public void testGetForm() throws Exception {
        mockMvc.perform(get(Mappings.ADMINISTRATION_ROOT))
                .andExpect(status().isOk());
    }

    @Test
    public void testExportReservations() throws Exception {

        MockHttpServletResponse response = mockMvc.perform(get(Mappings.ADMINISTRATION_EXPORT_RESERVATIONS_CSV)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("begin", new DateTime().minusDays(1).toString("dd/MM/YYYY"))
                .param("end", new DateTime().plusMonths(2).toString("dd/MM/YYYY"))
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/csv"))
                .andReturn().getResponse();

        assertTrue(response.getContentAsString().length() > 3800);
        //TODO: check value returned
    }

    @Test
    public void testExportServices() throws Exception {

        MockHttpServletResponse response = mockMvc.perform(get(Mappings.ADMINISTRATION_EXPORT_SERVICES_CSV)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("begin", new DateTime().minusDays(1).toString("dd/MM/YYYY"))
                .param("end", new DateTime().plusMonths(2).toString("dd/MM/YYYY"))
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/csv"))
                .andReturn().getResponse();

        assertTrue(response.getContentAsString().length() > 24000);

        //TODO: check value returned
    }

}
