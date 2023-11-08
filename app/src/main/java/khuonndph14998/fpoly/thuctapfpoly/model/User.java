package khuonndph14998.fpoly.thuctapfpoly.model;

import java.util.Objects;

public class User {
    private String id, fullname , email ,roles;

    public User() {
    }

    public User(String id, String fullname, String email, String roles) {
        this.id = id;
        this.fullname = fullname;
        this.email = email;
        this.roles = roles;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        User other = (User) obj;
        return Objects.equals(id, other.id) &&
                Objects.equals(email, other.email) &&
                Objects.equals(fullname, other.fullname) &&
                Objects.equals(roles, other.roles);
    }

}
