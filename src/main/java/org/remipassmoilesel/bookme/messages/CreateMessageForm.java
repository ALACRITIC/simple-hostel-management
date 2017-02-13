package org.remipassmoilesel.bookme.messages;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

/**
 * Created by remipassmoilesel on 30/01/17.
 */
public class CreateMessageForm {

    @NotNull
    @Size(min = 2, max = 3000)
    private String message;

    @NotNull
    private Long token;

    public CreateMessageForm() {

    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getToken() {
        return token;
    }

    public void setToken(Long token) {
        this.token = token;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CreateMessageForm that = (CreateMessageForm) o;
        return Objects.equals(message, that.message) &&
                Objects.equals(token, that.token);
    }

    @Override
    public int hashCode() {
        return Objects.hash(message, token);
    }

    @Override
    public String toString() {
        return "CreateMessageForm{" +
                "message='" + message + '\'' +
                ", token=" + token +
                '}';
    }
}
