package org.remipassmoilesel.bookme.admin;

import java.util.List;
import java.util.Objects;

public class MainMenuRow {
    public String image;
    public String title;
    public List<MainMenuItem> items;

    public MainMenuRow(String title, String image, List<MainMenuItem> items) {
        this.image = image;
        this.title = title;
        this.items = items;

        if (image == null) {
            this.image = MainMenuItem.PROTOTYPE_IMAGE;
        }
    }

    public List<MainMenuItem> getItems() {
        return items;
    }

    public void setItems(List<MainMenuItem> items) {
        this.items = items;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MainMenuRow tiledRow = (MainMenuRow) o;
        return Objects.equals(image, tiledRow.image) &&
                Objects.equals(title, tiledRow.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(image, title);
    }

}
