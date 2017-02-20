package org.remipassmoilesel.bookme.admin;

import java.util.List;
import java.util.Objects;

public class TiledMenuRow {
    public String image;
    public String title;
    public List<TiledMenuItem> items;

    public TiledMenuRow(String title, String image, List<TiledMenuItem> items) {
        this.image = image;
        this.title = title;
        this.items = items;

        if (image == null) {
            this.image = TiledMenuItem.PROTOTYPE_IMAGE;
        }
    }

    public List<TiledMenuItem> getItems() {
        return items;
    }

    public void setItems(List<TiledMenuItem> items) {
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
        TiledMenuRow tiledRow = (TiledMenuRow) o;
        return Objects.equals(image, tiledRow.image) &&
                Objects.equals(title, tiledRow.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(image, title);
    }

}
