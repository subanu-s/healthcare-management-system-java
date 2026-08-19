package com.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.entity.User;
import com.util.PasswordUtil;

public class UserDao {

    private final Connection conn;

    public UserDao(Connection conn) {
        this.conn = conn;
    }

    public boolean register(User user)
            throws SQLException {

        String sql =
                "INSERT INTO users "
                + "(full_name,email,password_hash,phone) "
                + "VALUES(?,?,?,?)";

        try (PreparedStatement ps =
                     conn.prepareStatement(sql)) {

            ps.setString(1,
                    user.getFullName());

            ps.setString(2,
                    user.getEmail());

            ps.setString(3,
                    PasswordUtil.hash(
                            user.getPassword()
                    ));

            ps.setString(4,
                    user.getPhone());

            return ps.executeUpdate() == 1;
        }
    }

    public User login(
            String email,
            String password)
            throws SQLException {

        String sql =
                "SELECT id,full_name,email,"
                + "phone,password_hash "
                + "FROM users WHERE email=?";

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

                    User user =
                            new User();

                    user.setId(
                            rs.getInt("id")
                    );

                    user.setFullName(
                            rs.getString(
                                    "full_name"
                            )
                    );

                    user.setEmail(
                            rs.getString(
                                    "email"
                            )
                    );

                    user.setPhone(
                            rs.getString(
                                    "phone"
                            )
                    );

                    return user;
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
                             "UPDATE users "
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
}