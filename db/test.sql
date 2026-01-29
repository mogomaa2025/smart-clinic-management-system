CREATE DATABASE smart_clinic_db;

USE smart_clinic_db;


#21
USE smart_clinic_db;
SHOW PROCEDURE STATUS WHERE Db = 'smart_clinic_db';


-- add
 -- Insert a doctor for login
INSERT INTO doctors (name, email, password, speciality) VALUES ('Dr. Jane Doe', 'doctor.jane@clinic.com',
     'password123', 'Cardiology');
     
      -- Insert a patient for login
INSERT INTO patients (name, email, password, phone_number) VALUES ('John Smith', 'patient.john@clinic.com',
     'password123', '555-1234');
     
     
-- 18

SET @doctor_id = (SELECT doctor_id FROM doctors WHERE email = 'doctor.jane@clinic.com');
SET @patient_id = (SELECT patient_id FROM patients WHERE email = 'patient.john@clinic.com');
    
INSERT INTO appointments (doctor_id, patient_id, appointment_time) VALUES
(@doctor_id, @patient_id, '2026-01-29 10:00:00'), -- موعد اليوم الساعة 10 صباحاً
(@doctor_id, @patient_id, '2026-01-29 11:30:00'), -- موعد اليوم الساعة 11:30 صباحاً
(@doctor_id, @patient_id, '2026-01-30 09:00:00'); -- موعد الغد الساعة 9 صباحاً


-- 19

SHOW TABLES;

-- 20
SELECT * FROM patients LIMIT 5;

-- 21
CALL GetDailyAppointmentReportByDoctor('2026-01-29', 1);

-- 22

 CALL GetDoctorWithMostPatientsByMonth(1, 2026);
 
 -- 23
 
  CALL GetDoctorWithMostPatientsByYear(2026);