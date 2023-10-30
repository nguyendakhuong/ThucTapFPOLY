package khuonndph14998.fpoly.thuctapfpoly.model;

public class Discount {
    private String dc_name,dc_code,dc_time,dc_note;
    private int dc_quantity,dc_price;

    public Discount() {
    }

    public Discount(String dc_name, String dc_code, String dc_time, String dc_note, int dc_quantity, int dc_price) {
        this.dc_name = dc_name;
        this.dc_code = dc_code;
        this.dc_time = dc_time;
        this.dc_note = dc_note;
        this.dc_quantity = dc_quantity;
        this.dc_price = dc_price;
    }

    public String getDc_name() {
        return dc_name;
    }

    public void setDc_name(String dc_name) {
        this.dc_name = dc_name;
    }

    public String getDc_code() {
        return dc_code;
    }

    public void setDc_code(String dc_code) {
        this.dc_code = dc_code;
    }

    public String getDc_time() {
        return dc_time;
    }

    public void setDc_time(String dc_time) {
        this.dc_time = dc_time;
    }

    public String getDc_note() {
        return dc_note;
    }

    public void setDc_note(String dc_note) {
        this.dc_note = dc_note;
    }

    public int getDc_quantity() {
        return dc_quantity;
    }

    public void setDc_quantity(int dc_quantity) {
        this.dc_quantity = dc_quantity;
    }

    public int getDc_price() {
        return dc_price;
    }

    public void setDc_price(int dc_price) {
        this.dc_price = dc_price;
    }
}

