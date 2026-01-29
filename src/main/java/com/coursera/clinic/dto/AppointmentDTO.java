package com.coursera.clinic.dto;

import com.coursera.clinic.entity.Appointment;

import java.time.LocalDateTime;

public class AppointmentDTO {
    private Long id;
    private LocalDateTime appointmentTime;
    private String doctorName;
    private String patientName;
    private Long patientId;

    public AppointmentDTO(Appointment appointment) {
        this.id = appointment.getId();
        this.appointmentTime = appointment.getAppointmentTime();
        this.doctorName = appointment.getDoctor().getName();
        this.patientName = appointment.getPatient().getName();
        this.patientId = appointment.getPatient().getId();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public LocalDateTime getAppointmentTime() { return appointmentTime; }
    public void setAppointmentTime(LocalDateTime appointmentTime) { this.appointmentTime = appointmentTime; }
    public String getDoctorName() { return doctorName; }
    public void setDoctorName(String doctorName) { this.doctorName = doctorName; }
    public String getPatientName() { return patientName; }
    public void setPatientName(String patientName) { this.patientName = patientName; }
    public Long getPatientId() { return patientId; }
    public void setPatientId(Long patientId) { this.patientId = patientId; }
}
