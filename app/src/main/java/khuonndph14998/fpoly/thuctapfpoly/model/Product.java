package khuonndph14998.fpoly.thuctapfpoly.model;

import java.util.HashMap;
import java.util.Map;

public class Product {
    private String name;
    private String code;
    private int quantity;
    private String note;
    private String describe;
    private String selectedItem;
    private String image;
    private int price;
    private int number;

    public Product() {

    }

    public Product(String name, String code, int quantity, String note, String describe, String selectedItem, String image, int price, int number) {
        this.name = name;
        this.code = code;
        this.quantity = quantity;
        this.note = note;
        this.describe = describe;
        this.selectedItem = selectedItem;
        this.image = image;
        this.price = price;
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getSelectedItem() {
        return selectedItem;
    }

    public void setSelectedItem(String selectedItem) {
        this.selectedItem = selectedItem;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
