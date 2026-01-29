package com.coursera.clinic.repository;

import com.coursera.clinic.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

    /**
     * Finds a patient by their email address.
     * This is a derived query method.
     */
    Optional<Patient> findByEmail(String email);

    /**
     * Finds a patient by their email address or phone number.
     * This uses a custom @Query annotation.
     */
    @Query("SELECT p FROM Patient p WHERE p.email = :emailOrPhone OR p.phoneNumber = :emailOrPhone")
    Optional<Patient> findByEmailOrPhoneNumber(@Param("emailOrPhone") String emailOrPhone);
    
    // Example of a derived query for the same purpose
    // Optional<Patient> findByEmailOrPhoneNumber(String email, String phoneNumber);
}
