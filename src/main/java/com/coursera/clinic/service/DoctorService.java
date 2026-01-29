package com.coursera.clinic.service;

import com.coursera.clinic.dto.AuthResponse;
import com.coursera.clinic.entity.Appointment;
import com.coursera.clinic.entity.Doctor;
import com.coursera.clinic.repository.AppointmentRepository;
import com.coursera.clinic.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DoctorService {

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;
    
    @Autowired
    private TokenService tokenService;

    /**
     * Authenticates a doctor and returns a structured response with a JWT token.
     */
    public AuthResponse login(String email, String password) {
        Optional<Doctor> doctorOpt = doctorRepository.findByEmail(email);
        if (doctorOpt.isPresent()) {
            Doctor doctor = doctorOpt.get();
            // In a real app, you'd use a password encoder like BCrypt
            // For this project, we do a simple string comparison
            if (password.equals(doctor.getPassword())) {
                String token = tokenService.generateToken(email);
                return new AuthResponse(token, "Doctor login successful");
            } else {
                return new AuthResponse(null, "Invalid credentials");
            }
        }
        return new AuthResponse(null, "Doctor not found");
    }
    
    /**
     * Returns a list of available time slots for a doctor on a given date.
     * This is a simple implementation that assumes predefined slots.
     */
    public List<String> getAvailableTimeSlots(Long doctorId, LocalDate date) {
        // Ensure the doctor exists
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        // Get all appointments for the doctor on the given date
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);
        List<Appointment> appointments = appointmentRepository.findByDoctorIdAndDate(doctorId, startOfDay, endOfDay);
        
        // Extract the times of existing appointments
        List<String> bookedTimes = appointments.stream()
                .map(appointment -> appointment.getAppointmentTime().toLocalTime().toString())
                .collect(Collectors.toList());

        // Filter the doctor's predefined available times to exclude booked slots
        // This assumes doctor.getAvailableTimes() returns strings like "09:00", "09:30", etc.
        return doctor.getAvailableTimes().stream()
                .filter(availableTime -> !bookedTimes.contains(availableTime))
                .collect(Collectors.toList());
    }
    
    public List<Doctor> findAllDoctors() {
        return doctorRepository.findAll();
    }
    
    public Optional<Doctor> findDoctorById(Long id) {
        return doctorRepository.findById(id);
    }
    
    public Doctor saveDoctor(Doctor doctor) {
        // Here you would hash the password before saving
        // For simplicity, we are saving it as plain text
        return doctorRepository.save(doctor);
    }
}
