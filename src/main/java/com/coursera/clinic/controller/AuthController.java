package com.coursera.clinic.controller;

import com.coursera.clinic.dto.AuthResponse;
import com.coursera.clinic.service.DoctorService;
import com.coursera.clinic.repository.PatientRepository;
import com.coursera.clinic.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private PatientRepository patientRepository;
    
    @Autowired
    private TokenService tokenService;

    @PostMapping("/doctor/login")
    public ResponseEntity<AuthResponse> loginDoctor(@RequestBody Map<String, String> loginRequest) {
        String email = loginRequest.get("email");
        String password = loginRequest.get("password");
        AuthResponse response = doctorService.login(email, password);
        if (response.getToken() != null) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

    @PostMapping("/patient/login")
    public ResponseEntity<AuthResponse> loginPatient(@RequestBody Map<String, String> loginRequest) {
        String email = loginRequest.get("email");
        String password = loginRequest.get("password");
        
        Optional<com.coursera.clinic.entity.Patient> patientOpt = patientRepository.findByEmail(email);
        if (patientOpt.isPresent()) {
            com.coursera.clinic.entity.Patient patient = patientOpt.get();
            if (password.equals(patient.getPassword())) {
                String token = tokenService.generateToken(email);
                return ResponseEntity.ok(new AuthResponse(token, "Patient login successful"));
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new AuthResponse(null, "Invalid credentials"));
    }
}
