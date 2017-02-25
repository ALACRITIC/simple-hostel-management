package org.remipassmoilesel.bookme.accommodations;

/**
 * Created by remipassmoilesel on 12/02/17.
 */
public enum Type {

    CHALET,

    ROOM,

    BED;

    public String getReadableName() {

        if (this == ROOM) {
            return "Room";
        }
        if (this == CHALET) {
            return "Chalet";
        }
        //
        else if (this == BED) {
            return "Bed";
        }
        //
        else return "Undefined";
    }
}
