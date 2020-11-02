package com.shariff.mealsordering;

public class Booking {

    private String Email,ProductId,Date,Phone,Address,Quantity,status;

    public Booking() { }

    public Booking(String email, String productId, String date, String phone, String address, String quantity, String st) {
        Email = email;
        ProductId = productId;
        Date = date;
        Phone = phone;
        Address = address;
        Quantity = quantity;
        status= st;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getProductId() {
        return ProductId;
    }

    public void setProductId(String productId) {
        ProductId = productId;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }
}
