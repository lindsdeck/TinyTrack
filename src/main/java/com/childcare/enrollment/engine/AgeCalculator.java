package com.childcare.enrollment.engine;

import java.time.LocalDate;
import java.time.Period;

import org.springframework.stereotype.Component;

@Component
public class AgeCalculator {

    public int calculateAgeInMonths(
            LocalDate dateOfBirth,
            LocalDate selectedDate) {

        if (dateOfBirth == null) {
            throw new IllegalArgumentException(
                    "Date of birth is required."
            );
        }

        if (selectedDate == null) {
            throw new IllegalArgumentException(
                    "Projection date is required."
            );
        }

        if (selectedDate.isBefore(dateOfBirth)) {
            throw new IllegalArgumentException(
                    "Projection date cannot be before date of birth."
            );
        }

        Period age = Period.between(
                dateOfBirth,
                selectedDate
        );

        return age.getYears() * 12 + age.getMonths();
    }
}