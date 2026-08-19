package com.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.entity.Consultation;

public class ConsultationDao {

    private final Connection conn;

    public ConsultationDao(Connection conn) {
        this.conn = conn;
    }

    public boolean create(
            Consultation c)
            throws SQLException {

        String sql =
                "INSERT INTO consultations("
                + "user_id,doctor_id,"
                + "requested_date,mode,"
                + "symptom,severity,"
                + "symptom_duration,"
                + "previous_consultation,"
                + "preferred_language,"
                + "consent_given,"
                + "reason,status"
                + ") VALUES("
                + "?,?,?,?,?,?,?,?,?,?,?,?"
                + ")";

        try (PreparedStatement ps =
                     conn.prepareStatement(sql)) {

            ps.setInt(
                    1,
                    c.userId
            );

            ps.setInt(
                    2,
                    c.doctorId
            );

            ps.setString(
                    3,
                    c.requestedDate
            );

            ps.setString(
                    4,
                    c.mode
            );

            ps.setString(
                    5,
                    c.symptom
            );

            ps.setString(
                    6,
                    c.severity
            );

            ps.setString(
                    7,
                    c.symptomDuration
            );

            ps.setString(
                    8,
                    c.previousConsultation
            );

            ps.setString(
                    9,
                    c.preferredLanguage
            );

            ps.setBoolean(
                    10,
                    c.consentGiven
            );

            ps.setString(
                    11,
                    c.reason
            );

            ps.setString(
                    12,
                    "REQUESTED"
            );

            return ps.executeUpdate() == 1;
        }
    }

    public List<Consultation>
    findByUser(
            int userId)
            throws SQLException {

        String sql =
                "SELECT c.*, "
                + "d.full_name doctor_name,"
                + "u.full_name patient_name,"
                + "u.phone patient_phone "
                + "FROM consultations c "
                + "JOIN doctors d "
                + "ON c.doctor_id=d.id "
                + "JOIN users u "
                + "ON c.user_id=u.id "
                + "WHERE c.user_id=? "
                + "ORDER BY "
                + "c.requested_date DESC";

        return find(sql, userId);
    }

    public List<Consultation>
    findByDoctor(
            int doctorId)
            throws SQLException {

        String sql =
                "SELECT c.*, "
                + "d.full_name doctor_name,"
                + "u.full_name patient_name,"
                + "u.phone patient_phone "
                + "FROM consultations c "
                + "JOIN doctors d "
                + "ON c.doctor_id=d.id "
                + "JOIN users u "
                + "ON c.user_id=u.id "
                + "WHERE c.doctor_id=? "
                + "ORDER BY "
                + "c.requested_date DESC";

        return find(sql, doctorId);
    }

    private List<Consultation> find(
            String sql,
            int id)
            throws SQLException {

        List<Consultation> list =
                new ArrayList<Consultation>();

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

    public Consultation findByToken(
            String token)
            throws SQLException {

        String sql =
                "SELECT c.*, "
                + "d.full_name doctor_name,"
                + "u.full_name patient_name,"
                + "u.phone patient_phone "
                + "FROM consultations c "
                + "JOIN doctors d "
                + "ON c.doctor_id=d.id "
                + "JOIN users u "
                + "ON c.user_id=u.id "
                + "WHERE c.meeting_token=?";

        try (PreparedStatement ps =
                     conn.prepareStatement(sql)) {

            ps.setString(1, token);

            try (ResultSet rs =
                         ps.executeQuery()) {

                if (rs.next()) {
                    return map(rs);
                }
            }
        }

        return null;
    }

    public boolean updateByDoctor(
            int consultationId,
            int doctorId,
            String status,
            String contextPath,
            String scheme,
            String host,
            int port,
            String diagnosis,
            String temperature,
            String bloodPressure,
            String pulse,
            String recommendedTests,
            String doctorNotes,
            String prescription,
            String followUpDate)
            throws SQLException {

        String token = null;
        String meetingLink = null;

        if ("ACCEPTED".equals(status)
                || "COMPLETED".equals(status)) {

            token =
                    currentToken(
                            consultationId,
                            doctorId
                    );

            if (token == null
                    || token.isEmpty()) {

                token =
                        UUID.randomUUID()
                                .toString();
            }

            meetingLink =
                    scheme + "://"
                    + host
                    + ((port == 80
                    || port == 443)
                    ? ""
                    : ":" + port)
                    + contextPath
                    + "/consultation/"
                    + "room.jsp?token="
                    + token;
        }

        String sql =
                "UPDATE consultations SET "
                + "status=?,"
                + "meeting_token="
                + "COALESCE(?,meeting_token),"
                + "meeting_link="
                + "COALESCE(?,meeting_link),"
                + "diagnosis=?,"
                + "temperature=?,"
                + "blood_pressure=?,"
                + "pulse=?,"
                + "recommended_tests=?,"
                + "doctor_notes=?,"
                + "prescription=?,"
                + "follow_up_date=? "
                + "WHERE id=? "
                + "AND doctor_id=?";

        try (PreparedStatement ps =
                     conn.prepareStatement(sql)) {

            ps.setString(1, status);
            ps.setString(2, token);
            ps.setString(3, meetingLink);
            ps.setString(4, diagnosis);
            ps.setString(5, temperature);
            ps.setString(6, bloodPressure);
            ps.setString(7, pulse);
            ps.setString(
                    8,
                    recommendedTests
            );
            ps.setString(
                    9,
                    doctorNotes
            );
            ps.setString(
                    10,
                    prescription
            );

            if (followUpDate == null
                    || followUpDate
                    .trim()
                    .isEmpty()) {

                ps.setNull(
                        11,
                        Types.DATE
                );

            } else {

                ps.setString(
                        11,
                        followUpDate
                );
            }

            ps.setInt(
                    12,
                    consultationId
            );

            ps.setInt(
                    13,
                    doctorId
            );

            return ps.executeUpdate() == 1;
        }
    }

    private String currentToken(
            int consultationId,
            int doctorId)
            throws SQLException {

        String sql =
                "SELECT meeting_token "
                + "FROM consultations "
                + "WHERE id=? "
                + "AND doctor_id=?";

        try (PreparedStatement ps =
                     conn.prepareStatement(sql)) {

            ps.setInt(
                    1,
                    consultationId
            );

            ps.setInt(
                    2,
                    doctorId
            );

            try (ResultSet rs =
                         ps.executeQuery()) {

                if (rs.next()) {
                    return rs.getString(1);
                }
            }
        }

        return null;
    }

    private Consultation map(
            ResultSet rs)
            throws SQLException {

        Consultation c =
                new Consultation();

        c.id =
                rs.getInt("id");

        c.userId =
                rs.getInt("user_id");

        c.doctorId =
                rs.getInt("doctor_id");

        c.requestedDate =
                rs.getString(
                        "requested_date"
                );

        c.mode =
                rs.getString("mode");

        c.symptom =
                rs.getString(
                        "symptom"
                );

        c.severity =
                rs.getString(
                        "severity"
                );

        c.symptomDuration =
                rs.getString(
                        "symptom_duration"
                );

        c.previousConsultation =
                rs.getString(
                        "previous_consultation"
                );

        c.preferredLanguage =
                rs.getString(
                        "preferred_language"
                );

        c.consentGiven =
                rs.getBoolean(
                        "consent_given"
                );

        c.reason =
                rs.getString("reason");

        c.status =
                rs.getString("status");

        c.meetingToken =
                rs.getString(
                        "meeting_token"
                );

        c.meetingLink =
                rs.getString(
                        "meeting_link"
                );

        c.diagnosis =
                rs.getString(
                        "diagnosis"
                );

        c.temperature =
                rs.getString(
                        "temperature"
                );

        c.bloodPressure =
                rs.getString(
                        "blood_pressure"
                );

        c.pulse =
                rs.getString("pulse");

        c.recommendedTests =
                rs.getString(
                        "recommended_tests"
                );

        c.doctorNotes =
                rs.getString(
                        "doctor_notes"
                );

        c.prescription =
                rs.getString(
                        "prescription"
                );

        c.followUpDate =
                rs.getString(
                        "follow_up_date"
                );

        c.doctorName =
                rs.getString(
                        "doctor_name"
                );

        c.patientName =
                rs.getString(
                        "patient_name"
                );

        c.patientPhone =
                rs.getString(
                        "patient_phone"
                );

        return c;
    }
}