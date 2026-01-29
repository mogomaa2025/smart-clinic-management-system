package com.coursera.clinic.controller;

import com.coursera.clinic.dto.ErrorResponse;
import com.coursera.clinic.dto.SuccessResponse;
import com.coursera.clinic.entity.Appointment;
import com.coursera.clinic.entity.Patient;
import com.coursera.clinic.repository.PatientRepository;
import com.coursera.clinic.service.AppointmentService;
import com.coursera.clinic.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;
    
    @Autowired
    private TokenService tokenService;
    
    @Autowired
    private PatientRepository patientRepository;

    // Endpoint for a patient to see their own appointments (Requirement #25)
    @GetMapping("/my-appointments")
    public ResponseEntity<?> getMyAppointments(@RequestHeader("Authorization") String token) {
        if (token == null || !token.startsWith("Bearer ") || !tokenService.validateToken(token.substring(7))) {
            return new ResponseEntity<>(new ErrorResponse("Invalid or missing token"), HttpStatus.UNAUTHORIZED);
        }

        String email = tokenService.getEmailFromToken(token.substring(7));
        Optional<Patient> patientOpt = patientRepository.findByEmail(email);

        if (patientOpt.isEmpty()) {
            return new ResponseEntity<>(new ErrorResponse("Patient not found for the given token"), HttpStatus.NOT_FOUND);
        }

        Patient patient = patientOpt.get();
        List<Appointment> appointments = appointmentService.getAppointmentsForPatient(patient.getId());
        return ResponseEntity.ok(new SuccessResponse<>(appointments));
    }

    // Endpoint for a doctor to see their appointments (for the frontend portal)
    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<?> getDoctorAppointments(@RequestHeader("Authorization") String token, @PathVariable Long doctorId) {
         if (token == null || !token.startsWith("Bearer ") || !tokenService.validateToken(token.substring(7))) {
            return new ResponseEntity<>(new ErrorResponse("Invalid or missing token"), HttpStatus.UNAUTHORIZED);
        }
        
        List<Appointment> appointments = appointmentService.getAppointmentsForDoctorByDate(doctorId, java.time.LocalDate.now());
        return ResponseEntity.ok(new SuccessResponse<>(appointments));
    }
}
