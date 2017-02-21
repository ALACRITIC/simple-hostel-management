package org.remipassmoilesel.bookme.sharedresources;

/**
 * Created by remipassmoilesel on 12/02/17.
 */
public enum Type {

    ROOM,

    BED,

    CAR;

    public String getReadableName() {

        if (this == ROOM) {
            return "Room";
        }
        //
        else if (this == BED) {
            return "Bed";
        }
        //
        else return "Undefined";
    }
}
