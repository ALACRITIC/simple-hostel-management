package org.remipassmoilesel.bookme.admin;

import org.remipassmoilesel.bookme.Mappings;
import org.springframework.ui.Model;

import java.util.Arrays;
import java.util.List;

/**
 * Created by remipassmoilesel on 20/02/17.
 */
public class TiledMenu {

    private static Mappings.MappingMap map = Mappings.getMap();

    private static List<TiledMenuItem> row1 = Arrays.asList(
            new TiledMenuItem("New reservation", Mappings.RESERVATIONS_FORM, null),
            new TiledMenuItem("Calendar", Mappings.RESERVATIONS_CALENDAR, null),
            new TiledMenuItem("View reservations", Mappings.RESERVATIONS_LASTS, null)
    );
    private static List<TiledMenuItem> row2 = Arrays.asList(
            new TiledMenuItem("New service", Mappings.SERVICES_FORM, null),
            new TiledMenuItem("Calendar", Mappings.SERVICES_CALENDAR, null),
            new TiledMenuItem("View services", Mappings.SERVICES_SHOW_LASTS, null)
    );
    private static List<TiledMenuItem> row4 = Arrays.asList(
            new TiledMenuItem("Search customers", Mappings.CUSTOMERS_SEARCH, null),
            new TiledMenuItem("Next checkouts", Mappings.RESERVATIONS_NEXT_CHECKOUTS, null),
            new TiledMenuItem("Export bill", Mappings.ADMINISTRATION_BILL_FORM, "assets/img/tile_dollar.svg")
    );

    private static List<TiledMenuRow> rows = Arrays.asList(
            new TiledMenuRow("Reservations", null, row1),
            new TiledMenuRow("Services", null, row2),
            new TiledMenuRow("Customers", null, row4)
    );

    public static void includeMenus(Model model) {
        model.addAttribute("menuRows", rows);
    }

}
