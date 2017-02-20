package org.remipassmoilesel.bookme.admin;

import java.util.Objects;

public class TiledMenuItem {


    public static final String PROTOTYPE_IMAGE = "assets/img/tile_prototype.svg";

    private String href;
    private String name;
    private String image;

    public TiledMenuItem(String name, String href, String image) {
        this.href = href;
        this.name = name;
        this.image = image;
        if (image == null) {
            this.image = PROTOTYPE_IMAGE;
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
