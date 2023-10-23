package khuonndph14998.fpoly.thuctapfpoly.model;

public class InfoUser {
    private String name,address,deliveryAddress,sex;
    private int phone;

    public InfoUser() {
    }

    public InfoUser(String name, String address, String deliveryAddress, String sex, int phone) {
        this.name = name;
        this.address = address;
        this.deliveryAddress = deliveryAddress;
        this.sex = sex;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public int getPhone() {
        return phone;
    }

    public void setPhone(int phone) {
        this.phone = phone;
    }
}
