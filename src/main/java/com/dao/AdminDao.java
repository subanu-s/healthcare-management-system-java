package com.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.util.PasswordUtil;

public class AdminDao {

    private final Connection conn;

    public AdminDao(Connection conn) {
        this.conn = conn;
    }

    public boolean login(
            String email,
            String password)
            throws SQLException {

        String sql =
                "SELECT id,password_hash "
                + "FROM admins "
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

                        try (PreparedStatement up =
                                     conn.prepareStatement(
                                             "UPDATE admins "
                                             + "SET password_hash=? "
                                             + "WHERE id=?"
                                     )) {

                            up.setString(
                                    1,
                                    PasswordUtil.hash(
                                            password
                                    )
                            );

                            up.setInt(
                                    2,
                                    rs.getInt("id")
                            );

                            up.executeUpdate();
                        }
                    }

                    return true;
                }
            }
        }

        return false;
    }
}