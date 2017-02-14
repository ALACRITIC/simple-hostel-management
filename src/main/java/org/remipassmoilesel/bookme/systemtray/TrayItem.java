package org.remipassmoilesel.bookme.systemtray;

import java.util.Objects;

/**
 * Created by remipassmoilesel on 14/02/17.
 */
public class TrayItem {

    private String name;
    private String mapping;

    public TrayItem(String name, String mapping) {
        this.name = name;
        this.mapping = mapping;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMapping() {
        return mapping;
    }

    public void setMapping(String mapping) {
        this.mapping = mapping;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TrayItem trayItem = (TrayItem) o;
        return Objects.equals(name, trayItem.name) &&
                Objects.equals(mapping, trayItem.mapping);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, mapping);
    }

    @Override
    public String toString() {
        return "TrayItem{" +
                "name='" + name + '\'' +
                ", url='" + mapping + '\'' +
                '}';
    }
}
