package com.coursera.clinic.controller;

import com.coursera.clinic.dto.ErrorResponse;
import com.coursera.clinic.dto.SuccessResponse;
import com.coursera.clinic.entity.Doctor;
import com.coursera.clinic.entity.Patient;
import com.coursera.clinic.entity.Prescription;
import com.coursera.clinic.repository.DoctorRepository;
import com.coursera.clinic.repository.PatientRepository;
import com.coursera.clinic.repository.PrescriptionRepository;
import com.coursera.clinic.service.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/prescriptions")
public class PrescriptionController {

    @Autowired
    private PrescriptionRepository prescriptionRepository;
    
    @Autowired
    private DoctorRepository doctorRepository;
    
    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private TokenService tokenService;

    /**
     * Endpoint to create a new prescription.
     * The request body should contain doctorId, patientId, and details.
     * Requires a valid doctor's JWT token.
     */
    @PostMapping
    public ResponseEntity<?> createPrescription(
            @RequestHeader("Authorization") String token,
            @Valid @RequestBody PrescriptionRequest request) {

        if (token == null || !token.startsWith("Bearer ") || !tokenService.validateToken(token.substring(7))) {
            return new ResponseEntity<>(new ErrorResponse("Invalid or missing token for doctor"), HttpStatus.UNAUTHORIZED);
        }
        
        // In a real app, you'd verify the token belongs to the doctorId in the request
        
        try {
            Doctor doctor = doctorRepository.findById(request.getDoctorId()).orElseThrow(() -> new RuntimeException("Doctor not found"));
            Patient patient = patientRepository.findById(request.getPatientId()).orElseThrow(() -> new RuntimeException("Patient not found"));

            Prescription prescription = new Prescription();
            prescription.setDoctor(doctor);
            prescription.setPatient(patient);
            prescription.setDetails(request.getDetails());
            
            prescriptionRepository.save(prescription);

            return ResponseEntity.status(HttpStatus.CREATED).body(new SuccessResponse<>("Prescription created successfully."));

        } catch (RuntimeException e) {
            return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }
    
    // Simple DTO for the request body
    static class PrescriptionRequest {
        private Long doctorId;
        private Long patientId;
        private String details;

        public Long getDoctorId() { return doctorId; }
        public void setDoctorId(Long doctorId) { this.doctorId = doctorId; }
        public Long getPatientId() { return patientId; }
        public void setPatientId(Long patientId) { this.patientId = patientId; }
        public String getDetails() { return details; }
        public void setDetails(String details) { this.details = details; }
    }
}
