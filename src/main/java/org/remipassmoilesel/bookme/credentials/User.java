package org.remipassmoilesel.bookme.credentials;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Objects;

/**
 * Created by remipassmoilesel on 15/02/17.
 */
@DatabaseTable(tableName = "USERS")
public class User {

    public static final String ID_FIELD_NAME = "ID";
    public static final String USERNAME_FIELD_NAME = "USERNAME";
    public static final String PASSWORD_FIELD_NAME = "PASSWORD";
    public static final String ROLE_FIELD_NAME = "ROLE";

    @DatabaseField(generatedId = true, columnName = ID_FIELD_NAME)
    private long id;

    @DatabaseField(unique = true, columnName = USERNAME_FIELD_NAME)
    private String username;

    @DatabaseField(columnName = PASSWORD_FIELD_NAME)
    private String password;

    @DatabaseField(columnName = ROLE_FIELD_NAME)
    private Role role;

    public User() {

    }

    public User(String username, String hashedPassword, Role role) {
        this.username = username;
        this.password = hashedPassword;
        this.role = role;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "Credential{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User that = (User) o;
        return id == that.id &&
                Objects.equals(username, that.username) &&
                Objects.equals(password, that.password) &&
                Objects.equals(role, that.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, password, role);
    }
}
