package org.remipassmoilesel.bookme.sharedresources;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

/**
 * Created by remipassmoilesel on 30/01/17.
 */
public class CreateRoomForm {

    @NotNull
    @Size(min = 2, max = 50)
    private String roomName;

    @NotNull
    private int places;

    @NotNull
    @Size(max = 1000)
    private String roomComment;

    @NotNull
    private Long token;

    public CreateRoomForm() {

    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getRoomComment() {
        return roomComment;
    }

    public void setRoomComment(String roomComment) {
        this.roomComment = roomComment;
    }

    public Long getToken() {
        return token;
    }

    public void setToken(Long token) {
        this.token = token;
    }

    public int getPlaces() {
        return places;
    }

    public void setPlaces(int places) {
        this.places = places;
    }


    @Override
    public String toString() {
        return "CreateRoomForm{" +
                "roomName='" + roomName + '\'' +
                ", places=" + places +
                ", roomComment='" + roomComment + '\'' +
                ", token=" + token +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CreateRoomForm that = (CreateRoomForm) o;
        return places == that.places &&
                Objects.equals(roomName, that.roomName) &&
                Objects.equals(roomComment, that.roomComment) &&
                Objects.equals(token, that.token);
    }

    @Override
    public int hashCode() {
        return Objects.hash(roomName, places, roomComment, token);
    }


}
