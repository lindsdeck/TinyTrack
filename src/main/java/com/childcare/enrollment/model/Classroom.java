package com.childcare.enrollment.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "classrooms")
public class Classroom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Classroom name is required.")
    @Size(max = 75, message = "Classroom name cannot exceed 75 characters.")
    @Column(name = "classroom_name", nullable = false, length = 75)
    private String classroomName;

    @NotNull(message = "Minimum age is required.")
    @Min(value = 0, message = "Minimum age cannot be negative.")
    @Column(name = "minimum_age_months", nullable = false)
    private Integer minimumAgeMonths;

    @NotNull(message = "Maximum age is required.")
    @Min(value = 0, message = "Maximum age cannot be negative.")
    @Column(name = "maximum_age_months", nullable = false)
    private Integer maximumAgeMonths;

    @NotNull(message = "Licensed capacity is required.")
    @Min(value = 1, message = "Licensed capacity must be at least 1.")
    @Column(name = "licensed_capacity", nullable = false)
    private Integer licensedCapacity;

    @Column(nullable = false)
    private boolean active = true;

    @ManyToOne
    @JoinColumn(name = "center_id", nullable = false)
    private Center center;

    public Classroom() {
    }

    public Classroom(
            String classroomName,
            Integer minimumAgeMonths,
            Integer maximumAgeMonths,
            Integer licensedCapacity,
            Center center) {

        this.classroomName = classroomName;
        this.minimumAgeMonths = minimumAgeMonths;
        this.maximumAgeMonths = maximumAgeMonths;
        this.licensedCapacity = licensedCapacity;
        this.center = center;
        this.active = true;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClassroomName() {
        return classroomName;
    }

    public void setClassroomName(String classroomName) {
        this.classroomName = classroomName;
    }

    public Integer getMinimumAgeMonths() {
        return minimumAgeMonths;
    }

    public void setMinimumAgeMonths(Integer minimumAgeMonths) {
        this.minimumAgeMonths = minimumAgeMonths;
    }

    public Integer getMaximumAgeMonths() {
        return maximumAgeMonths;
    }

    public void setMaximumAgeMonths(Integer maximumAgeMonths) {
        this.maximumAgeMonths = maximumAgeMonths;
    }

    public Integer getLicensedCapacity() {
        return licensedCapacity;
    }

    public void setLicensedCapacity(Integer licensedCapacity) {
        this.licensedCapacity = licensedCapacity;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Center getCenter() {
        return center;
    }

    public void setCenter(Center center) {
        this.center = center;
    }
}