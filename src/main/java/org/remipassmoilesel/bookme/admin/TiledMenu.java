package org.remipassmoilesel.bookme.admin;

import org.remipassmoilesel.bookme.Mappings;
import org.springframework.ui.Model;

import java.util.Arrays;
import java.util.List;

/**
 * Created by remipassmoilesel on 20/02/17.
 */
public class TiledMenu {

    private static List<TiledMenuItem> row_reservation = Arrays.asList(
            new TiledMenuItem("New reservation", Mappings.RESERVATIONS_FORM, "assets/img/tile_new-reservation.svg"),
            new TiledMenuItem("Calendar", Mappings.RESERVATIONS_CALENDAR, "assets/img/tile_reservation-calendar.svg"),
            new TiledMenuItem("Last reservations", Mappings.RESERVATIONS_LASTS, "assets/img/tile_reservation-lasts.svg")
    );
    private static List<TiledMenuItem> row_service = Arrays.asList(
            new TiledMenuItem("New service", Mappings.SERVICES_FORM, null),
            new TiledMenuItem("Calendar", Mappings.SERVICES_CALENDAR, null),
            new TiledMenuItem("View services", Mappings.SERVICES_SHOW_LASTS, null)
    );
    private static List<TiledMenuItem> row_customer = Arrays.asList(
            new TiledMenuItem("Search customers", Mappings.CUSTOMERS_SEARCH, null),
            new TiledMenuItem("Next checkouts", Mappings.RESERVATIONS_NEXT_CHECKOUTS, null),
            new TiledMenuItem("Export bill", Mappings.ADMINISTRATION_BILL_FORM, "assets/img/tile_dollar.svg")
    );
    private static List<TiledMenuItem> row_messages = Arrays.asList(
            new TiledMenuItem("New message", Mappings.MESSAGES_FORM, null),
            new TiledMenuItem("View all", Mappings.MESSAGES_SHOW_ALL, null)
    );

    private static List<TiledMenuRow> row_names = Arrays.asList(
            new TiledMenuRow("Reservations", null, row_reservation),
            new TiledMenuRow("Services", null, row_service),
            new TiledMenuRow("Customers", null, row_customer),
            new TiledMenuRow("Messages", null, row_messages)
    );

    public static void includeMenus(Model model) {
        model.addAttribute("menuRows", row_names);
    }

}