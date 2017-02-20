package org.remipassmoilesel.bookme.admin;

import org.springframework.ui.Model;

import java.util.Arrays;
import java.util.List;

/**
 * Created by remipassmoilesel on 20/02/17.
 */
public class TiledMenu {

    private static List<TiledMenuItem> row1 = Arrays.asList(
            new TiledMenuItem("New reservation", null, null),
            new TiledMenuItem("Calendar", null, null),
            new TiledMenuItem("View reservations", null, null)
    );
    private static List<TiledMenuItem> row2 = Arrays.asList(
            new TiledMenuItem("New service", null, null),
            new TiledMenuItem("Calendar", null, null),
            new TiledMenuItem("View services", null, null)
    );
    private static List<TiledMenuItem> row4 = Arrays.asList(
            new TiledMenuItem("Search customers", null, null),
            new TiledMenuItem("View customers", null, null),
            new TiledMenuItem("Export bill", null, "assets/img/tile_dollar.svg")
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
