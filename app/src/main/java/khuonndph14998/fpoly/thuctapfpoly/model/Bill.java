package khuonndph14998.fpoly.thuctapfpoly.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Bill implements Serializable {
    private String billId;
    private int totalPay;
    private String payEr;
    private String customerAddress;
    private String time;
    private List<String> productNameList;
    private List<String> productCode;
    private List<Integer> quantityList;

    public Bill() {
    }

    public Bill(String billId, int totalPay, String payEr, String customerAddress, String time, List<String> productNameList, List<String> productCode, List<Integer> quantityList) {
        this.billId = billId;
        this.totalPay = totalPay;
        this.payEr = payEr;
        this.customerAddress = customerAddress;
        this.time = time;
        this.productNameList = productNameList;
        this.productCode = productCode;
        this.quantityList = quantityList;
    }

    public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
        this.billId = billId;
    }

    public int getTotalPay() {
        return totalPay;
    }

    public void setTotalPay(int totalPay) {
        this.totalPay = totalPay;
    }

    public String getPayEr() {
        return payEr;
    }

    public void setPayEr(String payEr) {
        this.payEr = payEr;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public List<String> getProductNameList() {
        return productNameList;
    }

    public void setProductNameList(List<String> productNameList) {
        this.productNameList = productNameList;
    }

    public List<String> getProductCode() {
        return productCode;
    }

    public void setProductCode(List<String> productCode) {
        this.productCode = productCode;
    }

    public List<Integer> getQuantityList() {
        return quantityList;
    }

    public void setQuantityList(List<Integer> quantityList) {
        this.quantityList = quantityList;
    }
}
