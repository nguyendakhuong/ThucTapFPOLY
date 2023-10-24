package khuonndph14998.fpoly.thuctapfpoly.model;

public class PayProduct {
    private String name;
    private String image;
    private String category;
    private int price;
    private int number;

    public PayProduct() {
    }

    public PayProduct(String name, String image, String category, int price, int number) {
        this.name = name;
        this.image = image;
        this.category = category;
        this.price = price;
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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
