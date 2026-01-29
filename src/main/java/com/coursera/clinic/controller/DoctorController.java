package com.coursera.clinic.controller;

import com.coursera.clinic.dto.ErrorResponse;
import com.coursera.clinic.dto.SuccessResponse;
import com.coursera.clinic.entity.Doctor;
import com.coursera.clinic.service.DoctorService;
import com.coursera.clinic.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/doctors")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private TokenService tokenService;

    /**
     * Endpoint to get a doctor's availability.
     * Requires a valid JWT token.
     */
    @GetMapping("/{doctorId}/availability")
    public ResponseEntity<?> getDoctorAvailability(
            @RequestHeader("Authorization") String token,
            @PathVariable Long doctorId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        // Remove "Bearer " prefix and validate token
        if (token == null || !token.startsWith("Bearer ") || !tokenService.validateToken(token.substring(7))) {
            return new ResponseEntity<>(new ErrorResponse("Invalid or missing token"), HttpStatus.UNAUTHORIZED);
        }

        try {
            List<String> availableSlots = doctorService.getAvailableTimeSlots(doctorId, date);
            return ResponseEntity.ok(new SuccessResponse<>(availableSlots));
        } catch (RuntimeException e) {
            return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }
    
    @GetMapping
    public ResponseEntity<?> getAllDoctors() {
        List<Doctor> doctors = doctorService.findAllDoctors();
        return ResponseEntity.ok(new SuccessResponse<>(doctors));
    }
    
    // Example endpoint for Requirement #26
    @GetMapping("/search")
    public ResponseEntity<?> searchDoctorsBySpecialityAndTime(
        @RequestParam String speciality,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        
        // This is a simplified search. A real implementation would be more complex.
        // For now, it finds all doctors and then filters by speciality.
        // The time aspect is handled by checking if they have any availability on the given date.
        List<Doctor> allDoctors = doctorService.findAllDoctors();
        
        List<Doctor> filteredDoctors = allDoctors.stream()
            .filter(d -> d.getSpeciality().equalsIgnoreCase(speciality))
            .filter(d -> !doctorService.getAvailableTimeSlots(d.getId(), date).isEmpty())
            .toList();

        return ResponseEntity.ok(new SuccessResponse<>(filteredDoctors));
    }
}
