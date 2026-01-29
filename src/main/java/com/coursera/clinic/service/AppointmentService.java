package com.coursera.clinic.service;

import com.coursera.clinic.dto.AppointmentDTO;
import com.coursera.clinic.entity.Appointment;
import com.coursera.clinic.entity.Doctor;
import com.coursera.clinic.entity.Patient;
import com.coursera.clinic.repository.AppointmentRepository;
import com.coursera.clinic.repository.DoctorRepository;
import com.coursera.clinic.repository.PatientRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private PatientRepository patientRepository;

    /**
     * Books an appointment and saves it to the repository.
     */
    public Appointment bookAppointment(Long doctorId, Long patientId, LocalDateTime appointmentTime) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        // Basic validation: Check if the slot is actually available
        boolean isSlotAvailable = doctor.getAvailableTimes().stream()
            .anyMatch(time -> time.equals(appointmentTime.toLocalTime().toString()));

        if (!isSlotAvailable) {
            throw new RuntimeException("The requested time slot is not in the doctor's availability.");
        }

        Appointment newAppointment = new Appointment();
        newAppointment.setDoctor(doctor);
        newAppointment.setPatient(patient);
        newAppointment.setAppointmentTime(appointmentTime);

        return appointmentRepository.save(newAppointment);
    }

    /**
     * Retrieves all appointments for a specific doctor on a given date and maps them to DTOs.
     * @Transactional is important here to keep the session open for lazy loading.
     */
    @Transactional
    public List<AppointmentDTO> getAppointmentsForDoctorByDate(Long doctorId, LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);
        List<Appointment> appointments = appointmentRepository.findByDoctorIdAndDate(doctorId, startOfDay, endOfDay);
        return appointments.stream()
                .map(AppointmentDTO::new)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves all appointments for a specific patient and maps them to DTOs.
     */
    @Transactional
    public List<AppointmentDTO> getAppointmentsForPatient(Long patientId) {
        List<Appointment> appointments = appointmentRepository.findByPatientId(patientId);
        return appointments.stream()
                .map(AppointmentDTO::new)
                .collect(Collectors.toList());
    }
}
