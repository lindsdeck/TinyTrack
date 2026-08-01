package com.childcare.enrollment.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "centers")
public class Center {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Center name is required.")
    @Size(max = 100, message = "Center name cannot exceed 100 characters.")
    @Column(name = "center_name", nullable = false, length = 100)
    private String centerName;

    @Size(max = 150, message = "Address cannot exceed 150 characters.")
    @Column(length = 150)
    private String address;

    @Size(max = 75, message = "City cannot exceed 75 characters.")
    @Column(length = 75)
    private String city;

    @Size(max = 2, message = "State must use the two-letter abbreviation.")
    @Column(length = 2)
    private String state;

    @Size(max = 10, message = "ZIP code cannot exceed 10 characters.")
    @Column(name = "zip_code", length = 10)
    private String zipCode;

    @Size(max = 20, message = "Phone number cannot exceed 20 characters.")
    @Column(name = "phone_number", length = 20)
    private String phoneNumber;

    @Column(nullable = false)
    private boolean active = true;

    public Center() {
    }

    public Center(
            String centerName,
            String address,
            String city,
            String state,
            String zipCode,
            String phoneNumber) {

        this.centerName = centerName;
        this.address = address;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
        this.phoneNumber = phoneNumber;
        this.active = true;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCenterName() {
        return centerName;
    }

    public void setCenterName(String centerName) {
        this.centerName = centerName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}