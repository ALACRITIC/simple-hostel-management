package org.remipassmoilesel.bookme.admin;

import org.springframework.ui.Model;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Created by remipassmoilesel on 20/02/17.
 */
public class TiledMenu {

    public static void includeMenus(Model model) {
        model.addAttribute("menus", menus);
    }

    static List<TiledMenuItem> menus = Arrays.asList(
            new TiledMenuItem("Dashboard", null, null),
            new TiledMenuItem("New reservation", null, null),
            new TiledMenuItem("View reservations", null, null),
            new TiledMenuItem("New service", null, null),
            new TiledMenuItem("View services", null, null),
            new TiledMenuItem("Export a bill", null, "assets/img/tile_dollar.svg")

    );

    public static class TiledMenuItem {
        private String href;
        private String name;
        private String image;

        public TiledMenuItem(String name, String href, String image) {
            this.href = href;
            this.name = name;
            this.image = image;
            if (image == null) {
                this.image = "assets/img/tile_prototype.svg";
            }
        }

        public String getHref() {
            return href;
        }

        public void setHref(String href) {
            this.href = href;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            TiledMenuItem that = (TiledMenuItem) o;
            return Objects.equals(href, that.href) &&
                    Objects.equals(name, that.name) &&
                    Objects.equals(image, that.image);
        }

        @Override
        public int hashCode() {
            return Objects.hash(href, name, image);
        }

        @Override
        public String toString() {
            return "TiledMenuItem{" +
                    "href='" + href + '\'' +
                    ", name='" + name + '\'' +
                    ", image='" + image + '\'' +
                    '}';
        }
    }


}
