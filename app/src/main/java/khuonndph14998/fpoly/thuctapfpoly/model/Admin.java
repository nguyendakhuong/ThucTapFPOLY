package khuonndph14998.fpoly.thuctapfpoly.model;

public class Admin {
    private String id , email ;

    public Admin() {
    }

    public Admin(String id, String email) {
        this.id = id;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
