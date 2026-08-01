package com.childcare.enrollment.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.childcare.enrollment.model.Center;
import com.childcare.enrollment.repository.CenterRepository;

@Service
public class CenterService {

    private final CenterRepository centerRepository;

    public CenterService(CenterRepository centerRepository) {
        this.centerRepository = centerRepository;
    }

    public List<Center> getAllCenters() {
        return centerRepository.findAll();
    }

    public List<Center> getActiveCenters() {
        return centerRepository.findByActiveTrue();
    }

    public Optional<Center> getCenterById(Long id) {
        return centerRepository.findById(id);
    }

    public Center saveCenter(Center center) {
        return centerRepository.save(center);
    }

    public Center updateCenter(Long id, Center updatedCenter) {
        Center existingCenter = centerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Center not found with ID: " + id));

        existingCenter.setCenterName(updatedCenter.getCenterName());
        existingCenter.setAddress(updatedCenter.getAddress());
        existingCenter.setCity(updatedCenter.getCity());
        existingCenter.setState(updatedCenter.getState());
        existingCenter.setZipCode(updatedCenter.getZipCode());
        existingCenter.setPhoneNumber(updatedCenter.getPhoneNumber());

        return centerRepository.save(existingCenter);
    }

    public void deactivateCenter(Long id) {
        Center center = centerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Center not found with ID: " + id));

        center.setActive(false);
        centerRepository.save(center);
    }

    public void reactivateCenter(Long id) {
        Center center = centerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Center not found with ID: " + id));

        center.setActive(true);
        centerRepository.save(center);
    }
}