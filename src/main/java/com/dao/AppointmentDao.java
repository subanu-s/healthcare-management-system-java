package com.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.entity.Appointment;

public class AppointmentDao {

    private final Connection conn;

    public AppointmentDao(Connection conn) {
        this.conn = conn;
    }

    public boolean slotAvailable(
            int doctorId,
            String date,
            String slot)
            throws SQLException {

        String sql =
                "SELECT COUNT(*) "
                + "FROM appointments "
                + "WHERE doctor_id=? "
                + "AND appointment_date=? "
                + "AND time_slot=? "
                + "AND status IN "
                + "('PENDING','CONFIRMED')";

        try (PreparedStatement ps =
                     conn.prepareStatement(sql)) {

            ps.setInt(1, doctorId);
            ps.setString(2, date);
            ps.setString(3, slot);

            try (ResultSet rs =
                         ps.executeQuery()) {

                rs.next();

                return rs.getInt(1) == 0;
            }
        }
    }

    public boolean create(
            Appointment a)
            throws SQLException {

        if (!slotAvailable(
                a.doctorId,
                a.appointmentDate,
                a.timeSlot)) {

            return false;
        }

        String sql =
                "INSERT INTO appointments("
                + "user_id,doctor_id,"
                + "patient_name,gender,age,"
                + "appointment_date,time_slot,"
                + "appointment_type,priority,"
                + "email,phone,symptom,"
                + "severity,symptom_duration,"
                + "existing_conditions,"
                + "allergies,current_medication,"
                + "address,emergency_contact,"
                + "patient_notes,status"
                + ") VALUES("
                + "?,?,?,?,?,?,?,?,?,?,?,?,"
                + "?,?,?,?,?,?,?,?,?"
                + ")";

        try (PreparedStatement ps =
                     conn.prepareStatement(sql)) {

            int i = 1;

            ps.setInt(i++, a.userId);
            ps.setInt(i++, a.doctorId);

            ps.setString(
                    i++,
                    a.patientName
            );

            ps.setString(
                    i++,
                    a.gender
            );

            ps.setInt(
                    i++,
                    a.age
            );

            ps.setString(
                    i++,
                    a.appointmentDate
            );

            ps.setString(
                    i++,
                    a.timeSlot
            );

            ps.setString(
                    i++,
                    a.appointmentType
            );

            ps.setString(
                    i++,
                    a.priority
            );

            ps.setString(
                    i++,
                    a.email
            );

            ps.setString(
                    i++,
                    a.phone
            );

            ps.setString(
                    i++,
                    a.symptom
            );

            ps.setString(
                    i++,
                    a.severity
            );

            ps.setString(
                    i++,
                    a.symptomDuration
            );

            ps.setString(
                    i++,
                    a.existingConditions
            );

            ps.setString(
                    i++,
                    a.allergies
            );

            ps.setString(
                    i++,
                    a.currentMedication
            );

            ps.setString(
                    i++,
                    a.address
            );

            ps.setString(
                    i++,
                    a.emergencyContact
            );

            ps.setString(
                    i++,
                    a.patientNotes
            );

            ps.setString(
                    i,
                    "PENDING"
            );

            return ps.executeUpdate() == 1;
        }
    }

    public List<Appointment> findByUser(
            int userId)
            throws SQLException {

        String sql =
                "SELECT a.*, "
                + "d.full_name doctor_name,"
                + "d.specialist "
                + "FROM appointments a "
                + "JOIN doctors d "
                + "ON a.doctor_id=d.id "
                + "WHERE a.user_id=? "
                + "ORDER BY "
                + "a.appointment_date DESC,"
                + "a.time_slot";

        return find(sql, userId);
    }

    public List<Appointment>
    findByDoctor(
            int doctorId)
            throws SQLException {

        String sql =
                "SELECT a.*, "
                + "d.full_name doctor_name,"
                + "d.specialist "
                + "FROM appointments a "
                + "JOIN doctors d "
                + "ON a.doctor_id=d.id "
                + "WHERE a.doctor_id=? "
                + "ORDER BY "
                + "a.appointment_date DESC,"
                + "a.time_slot";

        return find(sql, doctorId);
    }

    private List<Appointment> find(
            String sql,
            int id)
            throws SQLException {

        List<Appointment> list =
                new ArrayList<Appointment>();

        try (PreparedStatement ps =
                     conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs =
                         ps.executeQuery()) {

                while (rs.next()) {
                    list.add(map(rs));
                }
            }
        }

        return list;
    }

    public boolean updateByDoctor(
            int appointmentId,
            int doctorId,
            String status,
            String remarks,
            String cancellationReason)
            throws SQLException {

        String sql =
                "UPDATE appointments "
                + "SET status=?,"
                + "doctor_remarks=?,"
                + "cancellation_reason=? "
                + "WHERE id=? "
                + "AND doctor_id=?";

        try (PreparedStatement ps =
                     conn.prepareStatement(sql)) {

            ps.setString(1, status);
            ps.setString(2, remarks);
            ps.setString(
                    3,
                    cancellationReason
            );

            ps.setInt(
                    4,
                    appointmentId
            );

            ps.setInt(
                    5,
                    doctorId
            );

            return ps.executeUpdate() == 1;
        }
    }

    public boolean cancelByPatient(
            int appointmentId,
            int userId,
            String reason)
            throws SQLException {

        String sql =
                "UPDATE appointments "
                + "SET status='CANCELLED',"
                + "cancellation_reason=? "
                + "WHERE id=? "
                + "AND user_id=? "
                + "AND status='PENDING'";

        try (PreparedStatement ps =
                     conn.prepareStatement(sql)) {

            ps.setString(1, reason);

            ps.setInt(
                    2,
                    appointmentId
            );

            ps.setInt(
                    3,
                    userId
            );

            return ps.executeUpdate() == 1;
        }
    }

    private Appointment map(
            ResultSet rs)
            throws SQLException {

        Appointment a =
                new Appointment();

        a.id =
                rs.getInt("id");

        a.userId =
                rs.getInt("user_id");

        a.doctorId =
                rs.getInt("doctor_id");

        a.patientName =
                rs.getString(
                        "patient_name"
                );

        a.gender =
                rs.getString("gender");

        a.age =
                rs.getInt("age");

        a.appointmentDate =
                rs.getString(
                        "appointment_date"
                );

        a.timeSlot =
                rs.getString(
                        "time_slot"
                );

        a.appointmentType =
                rs.getString(
                        "appointment_type"
                );

        a.priority =
                rs.getString(
                        "priority"
                );

        a.email =
                rs.getString("email");

        a.phone =
                rs.getString("phone");

        a.symptom =
                rs.getString(
                        "symptom"
                );

        a.severity =
                rs.getString(
                        "severity"
                );

        a.symptomDuration =
                rs.getString(
                        "symptom_duration"
                );

        a.existingConditions =
                rs.getString(
                        "existing_conditions"
                );

        a.allergies =
                rs.getString(
                        "allergies"
                );

        a.currentMedication =
                rs.getString(
                        "current_medication"
                );

        a.address =
                rs.getString(
                        "address"
                );

        a.emergencyContact =
                rs.getString(
                        "emergency_contact"
                );

        a.patientNotes =
                rs.getString(
                        "patient_notes"
                );

        a.doctorRemarks =
                rs.getString(
                        "doctor_remarks"
                );

        a.cancellationReason =
                rs.getString(
                        "cancellation_reason"
                );

        a.status =
                rs.getString("status");

        a.doctorName =
                rs.getString(
                        "doctor_name"
                );

        a.specialist =
                rs.getString(
                        "specialist"
                );

        return a;
    }
}