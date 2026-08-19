package com.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.entity.Doctor;
import com.util.PasswordUtil;

public class DoctorDao {

    private final Connection conn;

    public DoctorDao(Connection conn) {
        this.conn = conn;
    }

    public Doctor login(
            String email,
            String password)
            throws SQLException {

        String sql =
                "SELECT * FROM doctors "
                + "WHERE email=?";

        try (PreparedStatement ps =
                     conn.prepareStatement(sql)) {

            ps.setString(1, email);

            try (ResultSet rs =
                         ps.executeQuery()) {

                if (rs.next()
                        && PasswordUtil.matches(
                        password,
                        rs.getString(
                                "password_hash"
                        ))) {

                    if (PasswordUtil
                            .needsUpgrade(
                                    rs.getString(
                                            "password_hash"
                                    )
                            )) {

                        upgradePassword(
                                rs.getInt("id"),
                                password
                        );
                    }

                    return map(rs);
                }
            }
        }

        return null;
    }

    private void upgradePassword(
            int id,
            String password)
            throws SQLException {

        try (PreparedStatement ps =
                     conn.prepareStatement(
                             "UPDATE doctors "
                             + "SET password_hash=? "
                             + "WHERE id=?"
                     )) {

            ps.setString(
                    1,
                    PasswordUtil.hash(
                            password
                    )
            );

            ps.setInt(2, id);

            ps.executeUpdate();
        }
    }

    public boolean add(
            Doctor doctor)
            throws SQLException {

        String sql =
                "INSERT INTO doctors("
                + "full_name,qualification,"
                + "specialist,email,phone,"
                + "password_hash,"
                + "experience_years"
                + ") VALUES(?,?,?,?,?,?,?)";

        try (PreparedStatement ps =
                     conn.prepareStatement(sql)) {

            ps.setString(
                    1,
                    doctor.getFullName()
            );

            ps.setString(
                    2,
                    doctor.getQualification()
            );

            ps.setString(
                    3,
                    doctor.getSpecialist()
            );

            ps.setString(
                    4,
                    doctor.getEmail()
            );

            ps.setString(
                    5,
                    doctor.getPhone()
            );

            ps.setString(
                    6,
                    PasswordUtil.hash(
                            doctor.getPassword()
                    )
            );

            ps.setInt(
                    7,
                    doctor.getExperienceYears()
            );

            return ps.executeUpdate() == 1;
        }
    }

    public List<Doctor> findAll()
            throws SQLException {

        List<Doctor> doctors =
                new ArrayList<Doctor>();

        String sql =
                "SELECT * FROM doctors "
                + "ORDER BY specialist,"
                + "full_name";

        try (PreparedStatement ps =
                     conn.prepareStatement(sql);

             ResultSet rs =
                     ps.executeQuery()) {

            while (rs.next()) {
                doctors.add(
                        map(rs)
                );
            }
        }

        return doctors;
    }

    private Doctor map(
            ResultSet rs)
            throws SQLException {

        Doctor doctor =
                new Doctor();

        doctor.setId(
                rs.getInt("id")
        );

        doctor.setFullName(
                rs.getString(
                        "full_name"
                )
        );

        doctor.setQualification(
                rs.getString(
                        "qualification"
                )
        );

        doctor.setSpecialist(
                rs.getString(
                        "specialist"
                )
        );

        doctor.setEmail(
                rs.getString(
                        "email"
                )
        );

        doctor.setPhone(
                rs.getString(
                        "phone"
                )
        );

        doctor.setExperienceYears(
                rs.getInt(
                        "experience_years"
                )
        );

        return doctor;
    }
}