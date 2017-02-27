package org.remipassmoilesel.bookme.menu;

import org.remipassmoilesel.bookme.Mappings;
import org.springframework.ui.Model;

import java.util.Arrays;
import java.util.List;

/**
 * Created by remipassmoilesel on 20/02/17.
 */
public class MainMenu {

    private static List<MainMenuItem> row_reservation = Arrays.asList(
            new MainMenuItem("New reservation", Mappings.RESERVATIONS_FORM, "/assets/img/tile_new-reservation.svg"),
            new MainMenuItem("Calendar", Mappings.RESERVATIONS_CALENDAR, "/assets/img/tile_reservation-calendar.svg"),
            new MainMenuItem("Last reservations", Mappings.RESERVATIONS_LASTS, "/assets/img/tile_reservation-lasts.svg")
    );
    private static List<MainMenuItem> row_service = Arrays.asList(
            new MainMenuItem("Bill a service", Mappings.SERVICES_FORM, "/assets/img/tile_new-service.svg"),
            new MainMenuItem("Scheduled services", Mappings.SERVICES_CALENDAR, "/assets/img/tile_service-calendar.svg"),
            new MainMenuItem("Last services", Mappings.SERVICES_SHOW_LASTS, "/assets/img/tile_reservation-lasts.svg")
    );
    private static List<MainMenuItem> row_customer = Arrays.asList(
            new MainMenuItem("Search customers", Mappings.CUSTOMERS_SEARCH, "/assets/img/tile_search-customer.svg"),
            new MainMenuItem("Next checkouts", Mappings.RESERVATIONS_NEXT_CHECKOUTS, "/assets/img/tile_next-checkouts.svg"),
            new MainMenuItem("Bill a customer", Mappings.CUSTOMERS_BILL_FORM, "/assets/img/tile_bill.svg")
    );

    private static List<MainMenuItem> row_messages = Arrays.asList(
            new MainMenuItem("New message", Mappings.MESSAGES_FORM, "/assets/img/tile_message-new.svg"),
            new MainMenuItem("View all", Mappings.MESSAGES_SHOW_ALL, "/assets/img/tile_reservation-lasts.svg")
    );

    private static List<MainMenuItem> row_accommodations = Arrays.asList(
            new MainMenuItem("New accommodation", Mappings.MESSAGES_FORM, "/assets/img/tile_new-resource.svg"),
            new MainMenuItem("Accommodations calendar", Mappings.ACCOMMODATIONS_CALENDAR, "/assets/img/tile_resource-calendar.svg"),
            new MainMenuItem("View all accommodations", Mappings.ACCOMMODATIONS_SHOW_ALL, "/assets/img/tile_reservation-lasts.svg")
    );

    private static List<MainMenuRow> row_names = Arrays.asList(
            new MainMenuRow("Reservations", null, row_reservation),
            new MainMenuRow("Services", null, row_service),
            new MainMenuRow("Customers", null, row_customer),
            new MainMenuRow("Accommodations", null, row_accommodations),
            new MainMenuRow("Messages", null, row_messages)
    );

    public static void includeMenus(Model model) {
        model.addAttribute("menuRows", row_names);
    }

}
