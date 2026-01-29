-- SQL Stored Procedures for Smart Clinic Management System

-- Note: The exact syntax might need minor adjustments based on your MySQL version.
-- These procedures assume the tables and columns defined in docs/schema-design.md exist.

--
-- Procedure 1: GetDailyAppointmentReportByDoctor
-- Get a report of all appointments for a specific doctor on a given date.
--
DROP PROCEDURE IF EXISTS GetDailyAppointmentReportByDoctor;
DELIMITER $$
CREATE PROCEDURE GetDailyAppointmentReportByDoctor(IN report_date DATE, IN doc_id INT)
BEGIN
    SELECT 
        d.name AS doctor_name,
        p.name AS patient_name,
        a.appointment_time
    FROM appointments a
    JOIN doctors d ON a.doctor_id = d.doctor_id
    JOIN patients p ON a.patient_id = p.patient_id
    WHERE d.doctor_id = doc_id AND DATE(a.appointment_time) = report_date
    ORDER BY a.appointment_time;
END$$
DELIMITER ;


--
-- Procedure 2: GetDoctorWithMostPatientsByMonth
-- Find the doctor who had the most appointments in a given month and year.
--
DROP PROCEDURE IF EXISTS GetDoctorWithMostPatientsByMonth;
DELIMITER $$
CREATE PROCEDURE GetDoctorWithMostPatientsByMonth(IN report_month INT, IN report_year INT)
BEGIN
    SELECT 
        d.name AS doctor_name,
        d.speciality,
        COUNT(a.appointment_id) AS appointment_count
    FROM appointments a
    JOIN doctors d ON a.doctor_id = d.doctor_id
    WHERE MONTH(a.appointment_time) = report_month AND YEAR(a.appointment_time) = report_year
    GROUP BY d.doctor_id, d.name, d.speciality
    ORDER BY appointment_count DESC
    LIMIT 1;
END$$
DELIMITER ;


--
-- Procedure 3: GetDoctorWithMostPatientsByYear
-- Find the doctor who had the most appointments in a given year.
--
DROP PROCEDURE IF EXISTS GetDoctorWithMostPatientsByYear;
DELIMITER $$
CREATE PROCEDURE GetDoctorWithMostPatientsByYear(IN report_year INT)
BEGIN
    SELECT 
        d.name AS doctor_name,
        d.speciality,
        COUNT(a.appointment_id) AS appointment_count
    FROM appointments a
    JOIN doctors d ON a.doctor_id = d.doctor_id
    WHERE YEAR(a.appointment_time) = report_year
    GROUP BY d.doctor_id, d.name, d.speciality
    ORDER BY appointment_count DESC
    LIMIT 1;
END$$
DELIMITER ;


-- =================================================================
-- Example SQL Commands for Testing (Requirement #20, #21, #22, #23)
-- =================================================================

-- 1. Show all tables in the database:
-- SHOW TABLES;

-- 2. Select a few records from the Patient table to verify data:
-- SELECT * FROM patients LIMIT 5;

-- 3. Call the stored procedures with example data:
-- CALL GetDailyAppointmentReportByDoctor('2026-02-10', 1);
-- CALL GetDoctorWithMostPatientsByMonth(2, 2026); -- (February 2026)
-- CALL GetDoctorWithMostPatientsByYear(2026);
