package com.servlet;

import java.io.IOException;
import java.sql.Connection;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import com.dao.*;
import com.db.DbConnect;
import com.entity.*;
import com.util.ValidationUtil;

@WebServlet(
    urlPatterns = {
        "/user-register",
        "/user-login",
        "/doctor-login",
        "/admin-login",
        "/logout",

        "/book-appointment",
        "/patient/cancel-appointment",

        "/request-consultation",

        "/doctor/update-appointment",
        "/doctor/save-consultation",

        "/admin/add-doctor"
    }
)
public class AppServlet
        extends HttpServlet {

    @Override
    protected void doGet(
            HttpServletRequest req,
            HttpServletResponse resp)
            throws IOException {

        if ("/logout".equals(
                req.getServletPath())) {

            HttpSession session =
                    req.getSession(false);

            if (session != null) {
                session.invalidate();
            }

            resp.sendRedirect(
                    req.getContextPath()
                    + "/index.jsp"
            );
        }
    }

    @Override
    protected void doPost(
            HttpServletRequest req,
            HttpServletResponse resp)
            throws IOException {

        String path =
                req.getServletPath();

        try (Connection conn =
                     DbConnect.getConn()) {

            if ("/user-register"
                    .equals(path)) {

                register(
                        req,
                        resp,
                        conn
                );

                return;
            }

            if ("/user-login"
                    .equals(path)) {

                userLogin(
                        req,
                        resp,
                        conn
                );

                return;
            }

            if ("/doctor-login"
                    .equals(path)) {

                doctorLogin(
                        req,
                        resp,
                        conn
                );

                return;
            }

            if ("/admin-login"
                    .equals(path)) {

                adminLogin(
                        req,
                        resp,
                        conn
                );

                return;
            }

            String role =
                    (String)
                    req.getSession()
                            .getAttribute(
                                    "AUTH_ROLE"
                            );

            if ("/book-appointment"
                    .equals(path)) {

                requireRole(
                        role,
                        "PATIENT"
                );

                bookAppointment(
                        req,
                        resp,
                        conn
                );

                return;
            }

            if ("/patient/cancel-appointment"
                    .equals(path)) {

                requireRole(
                        role,
                        "PATIENT"
                );

                cancelAppointment(
                        req,
                        resp,
                        conn
                );

                return;
            }

            if ("/request-consultation"
                    .equals(path)) {

                requireRole(
                        role,
                        "PATIENT"
                );

                requestConsultation(
                        req,
                        resp,
                        conn
                );

                return;
            }

            if ("/doctor/update-appointment"
                    .equals(path)) {

                requireRole(
                        role,
                        "DOCTOR"
                );

                updateAppointment(
                        req,
                        resp,
                        conn
                );

                return;
            }

            if ("/doctor/save-consultation"
                    .equals(path)) {

                requireRole(
                        role,
                        "DOCTOR"
                );

                updateConsultation(
                        req,
                        resp,
                        conn
                );

                return;
            }

            if ("/admin/add-doctor"
                    .equals(path)) {

                requireRole(
                        role,
                        "ADMIN"
                );

                addDoctor(
                        req,
                        resp,
                        conn
                );

                return;
            }

            resp.sendError(404);

        } catch (
                IllegalArgumentException e) {

            req.getSession()
                    .setAttribute(
                            "msg",
                            e.getMessage()
                    );

            String referer =
                    req.getHeader(
                            "Referer"
                    );

            if (referer != null) {

                resp.sendRedirect(
                        referer
                );

            } else {

                resp.sendRedirect(
                        req.getContextPath()
                        + "/index.jsp"
                );
            }

        } catch (SecurityException e) {

            resp.sendError(
                    403,
                    "You are not authorized "
                    + "to perform this action."
            );

        } catch (Exception e) {

            e.printStackTrace();

            resp.sendError(
                    500,
                    "Operation failed. "
                    + "Check server console."
            );
        }
    }

    private void register(
            HttpServletRequest req,
            HttpServletResponse resp,
            Connection conn)
            throws Exception {

        String fullName =
                ValidationUtil.required(
                        req.getParameter(
                                "fullName"
                        ),
                        100,
                        "Full name"
                );

        String email =
                ValidationUtil.email(
                        req.getParameter(
                                "email"
                        )
                );

        String phone =
                ValidationUtil.phone(
                        req.getParameter(
                                "phone"
                        ),
                        "Phone"
                );

        String password =
                req.getParameter(
                        "password"
                );

        ValidationUtil
                .strongPassword(
                        password
                );

        User user =
                new User(
                        fullName,
                        email,
                        password,
                        phone
                );

        new UserDao(conn)
                .register(user);

        req.getSession()
                .setAttribute(
                        "msg",
                        "Registration successful. "
                        + "Please login."
                );

        resp.sendRedirect(
                "signup.jsp"
        );
    }

    private void userLogin(
            HttpServletRequest req,
            HttpServletResponse resp,
            Connection conn)
            throws Exception {

        User user =
                new UserDao(conn)
                        .login(
                                ValidationUtil.email(
                                        req.getParameter(
                                                "email"
                                        )
                                ),
                                req.getParameter(
                                        "password"
                                )
                        );

        if (user == null) {

            req.getSession()
                    .setAttribute(
                            "msg",
                            "Invalid email "
                            + "or password."
                    );

            resp.sendRedirect(
                    "user_login.jsp"
            );

            return;
        }

        HttpSession session =
                createFreshSession(req);

        session.setAttribute(
                "AUTH_ROLE",
                "PATIENT"
        );

        session.setAttribute(
                "AUTH_ID",
                user.getId()
        );

        session.setAttribute(
                "userObj",
                user
        );

        resp.sendRedirect(
                "patient/dashboard.jsp"
        );
    }

    private void doctorLogin(
            HttpServletRequest req,
            HttpServletResponse resp,
            Connection conn)
            throws Exception {

        Doctor doctor =
                new DoctorDao(conn)
                        .login(
                                ValidationUtil.email(
                                        req.getParameter(
                                                "email"
                                        )
                                ),
                                req.getParameter(
                                        "password"
                                )
                        );

        if (doctor == null) {

            req.getSession()
                    .setAttribute(
                            "msg",
                            "Invalid doctor "
                            + "credentials."
                    );

            resp.sendRedirect(
                    "doctor_login.jsp"
            );

            return;
        }

        HttpSession session =
                createFreshSession(req);

        session.setAttribute(
                "AUTH_ROLE",
                "DOCTOR"
        );

        session.setAttribute(
                "AUTH_ID",
                doctor.getId()
        );

        session.setAttribute(
                "doctorObj",
                doctor
        );

        resp.sendRedirect(
                "doctor/dashboard.jsp"
        );
    }

    private void adminLogin(
            HttpServletRequest req,
            HttpServletResponse resp,
            Connection conn)
            throws Exception {

        String email =
                ValidationUtil.email(
                        req.getParameter(
                                "email"
                        )
                );

        boolean success =
                new AdminDao(conn)
                        .login(
                                email,
                                req.getParameter(
                                        "password"
                                )
                        );

        if (!success) {

            req.getSession()
                    .setAttribute(
                            "msg",
                            "Invalid admin "
                            + "credentials."
                    );

            resp.sendRedirect(
                    "admin_login.jsp"
            );

            return;
        }

        HttpSession session =
                createFreshSession(req);

        session.setAttribute(
                "AUTH_ROLE",
                "ADMIN"
        );

        session.setAttribute(
                "adminEmail",
                email
        );

        resp.sendRedirect(
                "admin/dashboard.jsp"
        );
    }

    private HttpSession
    createFreshSession(
            HttpServletRequest req) {

        HttpSession existing =
                req.getSession(false);

        if (existing != null) {
            existing.invalidate();
        }

        HttpSession session =
                req.getSession(true);

        session.setMaxInactiveInterval(
                30 * 60
        );

        return session;
    }

    private void bookAppointment(
            HttpServletRequest req,
            HttpServletResponse resp,
            Connection conn)
            throws Exception {

        User user =
                (User)
                req.getSession()
                        .getAttribute(
                                "userObj"
                        );

        Appointment a =
                new Appointment();

        a.userId =
                user.getId();

        a.doctorId =
                ValidationUtil.integer(
                        req.getParameter(
                                "doctorId"
                        ),
                        1,
                        Integer.MAX_VALUE,
                        "Doctor"
                );

        a.patientName =
                ValidationUtil.required(
                        req.getParameter(
                                "patientName"
                        ),
                        100,
                        "Patient name"
                );

        a.gender =
                ValidationUtil.oneOf(
                        req.getParameter(
                                "gender"
                        ),
                        "Gender",
                        "Female",
                        "Male",
                        "Other"
                );

        a.age =
                ValidationUtil.integer(
                        req.getParameter(
                                "age"
                        ),
                        1,
                        120,
                        "Age"
                );

        a.appointmentDate =
                ValidationUtil
                        .appointmentDate(
                                req.getParameter(
                                        "appointmentDate"
                                )
                        );

        a.timeSlot =
                ValidationUtil.oneOf(
                        req.getParameter(
                                "timeSlot"
                        ),
                        "Time slot",
                        "09:00 AM",
                        "10:00 AM",
                        "11:00 AM",
                        "12:00 PM",
                        "02:00 PM",
                        "03:00 PM",
                        "04:00 PM",
                        "05:00 PM"
                );

        a.appointmentType =
                ValidationUtil.oneOf(
                        req.getParameter(
                                "appointmentType"
                        ),
                        "Appointment type",
                        "General Consultation",
                        "Follow-up",
                        "Review",
                        "Preventive Check-up"
                );

        a.priority =
                ValidationUtil.oneOf(
                        req.getParameter(
                                "priority"
                        ),
                        "Priority",
                        "ROUTINE",
                        "URGENT",
                        "FOLLOW-UP"
                );

        a.email =
                ValidationUtil.email(
                        req.getParameter(
                                "email"
                        )
                );

        a.phone =
                ValidationUtil.phone(
                        req.getParameter(
                                "phone"
                        ),
                        "Phone"
                );

        a.symptom =
                ValidationUtil.required(
                        req.getParameter(
                                "symptom"
                        ),
                        100,
                        "Primary symptom"
                );

        a.severity =
                ValidationUtil.oneOf(
                        req.getParameter(
                                "severity"
                        ),
                        "Severity",
                        "Mild",
                        "Moderate",
                        "Severe"
                );

        a.symptomDuration =
                ValidationUtil.oneOf(
                        req.getParameter(
                                "symptomDuration"
                        ),
                        "Duration",
                        "Today",
                        "1-3 days",
                        "4-7 days",
                        "More than a week",
                        "More than a month"
                );

        a.existingConditions =
                ValidationUtil.optional(
                        req.getParameter(
                                "existingConditions"
                        ),
                        255
                );

        a.allergies =
                ValidationUtil.optional(
                        req.getParameter(
                                "allergies"
                        ),
                        255
                );

        a.currentMedication =
                ValidationUtil.optional(
                        req.getParameter(
                                "currentMedication"
                        ),
                        255
                );

        a.address =
                ValidationUtil.optional(
                        req.getParameter(
                                "address"
                        ),
                        500
                );

        a.emergencyContact =
                ValidationUtil.phone(
                        req.getParameter(
                                "emergencyContact"
                        ),
                        "Emergency contact"
                );

        a.patientNotes =
                ValidationUtil.optional(
                        req.getParameter(
                                "patientNotes"
                        ),
                        500
                );

        boolean created =
                new AppointmentDao(conn)
                        .create(a);

        if (!created) {

            throw new
            IllegalArgumentException(
                    "That doctor and time "
                    + "slot is already booked."
            );
        }

        req.getSession()
                .setAttribute(
                        "msg",
                        "Appointment booked "
                        + "successfully."
                );

        resp.sendRedirect(
                "patient/book_appointment.jsp"
        );
    }

    private void cancelAppointment(
            HttpServletRequest req,
            HttpServletResponse resp,
            Connection conn)
            throws Exception {

        User user =
                (User)
                req.getSession()
                        .getAttribute(
                                "userObj"
                        );

        int id =
                ValidationUtil.integer(
                        req.getParameter(
                                "appointmentId"
                        ),
                        1,
                        Integer.MAX_VALUE,
                        "Appointment"
                );

        String reason =
                ValidationUtil.required(
                        req.getParameter(
                                "reason"
                        ),
                        500,
                        "Cancellation reason"
                );

        new AppointmentDao(conn)
                .cancelByPatient(
                        id,
                        user.getId(),
                        reason
                );

        resp.sendRedirect(
                "book_appointment.jsp"
        );
    }

    private void requestConsultation(
            HttpServletRequest req,
            HttpServletResponse resp,
            Connection conn)
            throws Exception {

        User user =
                (User)
                req.getSession()
                        .getAttribute(
                                "userObj"
                        );

        if (req.getParameter(
                "consent") == null) {

            throw new
            IllegalArgumentException(
                    "Consent is required."
            );
        }

        Consultation c =
                new Consultation();

        c.userId =
                user.getId();

        c.doctorId =
                ValidationUtil.integer(
                        req.getParameter(
                                "doctorId"
                        ),
                        1,
                        Integer.MAX_VALUE,
                        "Doctor"
                );

        c.requestedDate =
                ValidationUtil
                        .consultationDateTime(
                                req.getParameter(
                                        "requestedDate"
                                )
                        );

        c.mode =
                ValidationUtil.oneOf(
                        req.getParameter(
                                "mode"
                        ),
                        "Consultation mode",
                        "Video",
                        "Audio",
                        "Chat"
                );

        c.symptom =
                ValidationUtil.required(
                        req.getParameter(
                                "symptom"
                        ),
                        100,
                        "Primary symptom"
                );

        c.severity =
                ValidationUtil.oneOf(
                        req.getParameter(
                                "severity"
                        ),
                        "Severity",
                        "Mild",
                        "Moderate",
                        "Severe"
                );

        c.symptomDuration =
                ValidationUtil.oneOf(
                        req.getParameter(
                                "symptomDuration"
                        ),
                        "Duration",
                        "Today",
                        "1-3 days",
                        "4-7 days",
                        "More than a week",
                        "More than a month"
                );

        c.previousConsultation =
                ValidationUtil.oneOf(
                        req.getParameter(
                                "previousConsultation"
                        ),
                        "Previous consultation",
                        "YES",
                        "NO"
                );

        c.preferredLanguage =
                ValidationUtil.oneOf(
                        req.getParameter(
                                "preferredLanguage"
                        ),
                        "Language",
                        "English",
                        "Tamil",
                        "Hindi"
                );

        c.reason =
                ValidationUtil.required(
                        req.getParameter(
                                "reason"
                        ),
                        500,
                        "Consultation reason"
                );

        c.consentGiven = true;

        new ConsultationDao(conn)
                .create(c);

        req.getSession()
                .setAttribute(
                        "msg",
                        "Online consultation "
                        + "requested."
                );

        resp.sendRedirect(
                "patient/consultations.jsp"
        );
    }

    private void updateAppointment(
            HttpServletRequest req,
            HttpServletResponse resp,
            Connection conn)
            throws Exception {

        Doctor doctor =
                (Doctor)
                req.getSession()
                        .getAttribute(
                                "doctorObj"
                        );

        int id =
                ValidationUtil.integer(
                        req.getParameter(
                                "appointmentId"
                        ),
                        1,
                        Integer.MAX_VALUE,
                        "Appointment"
                );

        String status =
                ValidationUtil.oneOf(
                        req.getParameter(
                                "status"
                        ),
                        "Status",
                        "CONFIRMED",
                        "COMPLETED",
                        "CANCELLED"
                );

        String remarks =
                ValidationUtil.optional(
                        req.getParameter(
                                "doctorRemarks"
                        ),
                        500
                );

        String cancellationReason =
                "";

        if ("CANCELLED"
                .equals(status)) {

            cancellationReason =
                    ValidationUtil.required(
                            req.getParameter(
                                    "cancellationReason"
                            ),
                            500,
                            "Cancellation reason"
                    );
        }

        new AppointmentDao(conn)
                .updateByDoctor(
                        id,
                        doctor.getId(),
                        status,
                        remarks,
                        cancellationReason
                );

        resp.sendRedirect(
                "appointments.jsp"
        );
    }

    private void updateConsultation(
            HttpServletRequest req,
            HttpServletResponse resp,
            Connection conn)
            throws Exception {

        Doctor doctor =
                (Doctor)
                req.getSession()
                        .getAttribute(
                                "doctorObj"
                        );

        int id =
                ValidationUtil.integer(
                        req.getParameter(
                                "consultationId"
                        ),
                        1,
                        Integer.MAX_VALUE,
                        "Consultation"
                );

        String status =
                ValidationUtil.oneOf(
                        req.getParameter(
                                "status"
                        ),
                        "Status",
                        "ACCEPTED",
                        "COMPLETED",
                        "DECLINED"
                );

        String diagnosis =
                ValidationUtil.optional(
                        req.getParameter(
                                "diagnosis"
                        ),
                        255
                );

        String temperature =
                ValidationUtil.optional(
                        req.getParameter(
                                "temperature"
                        ),
                        20
                );

        String bloodPressure =
                ValidationUtil.optional(
                        req.getParameter(
                                "bloodPressure"
                        ),
                        30
                );

        String pulse =
                ValidationUtil.optional(
                        req.getParameter(
                                "pulse"
                        ),
                        20
                );

        String tests =
                ValidationUtil.optional(
                        req.getParameter(
                                "recommendedTests"
                        ),
                        500
                );

        String notes =
                ValidationUtil.optional(
                        req.getParameter(
                                "doctorNotes"
                        ),
                        2000
                );

        String prescription =
                ValidationUtil.optional(
                        req.getParameter(
                                "prescription"
                        ),
                        2000
                );

        String followUp =
                ValidationUtil.optional(
                        req.getParameter(
                                "followUpDate"
                        ),
                        10
                );

        new ConsultationDao(conn)
                .updateByDoctor(
                        id,
                        doctor.getId(),
                        status,

                        req.getContextPath(),
                        req.getScheme(),
                        req.getServerName(),
                        req.getServerPort(),

                        diagnosis,
                        temperature,
                        bloodPressure,
                        pulse,
                        tests,
                        notes,
                        prescription,
                        followUp
                );

        resp.sendRedirect(
                "consultations.jsp"
        );
    }

    private void addDoctor(
            HttpServletRequest req,
            HttpServletResponse resp,
            Connection conn)
            throws Exception {

        Doctor doctor =
                new Doctor();

        doctor.setFullName(
                ValidationUtil.required(
                        req.getParameter(
                                "fullName"
                        ),
                        100,
                        "Doctor name"
                )
        );

        doctor.setQualification(
                ValidationUtil.required(
                        req.getParameter(
                                "qualification"
                        ),
                        100,
                        "Qualification"
                )
        );

        doctor.setSpecialist(
                ValidationUtil.oneOf(
                        req.getParameter(
                                "specialist"
                        ),
                        "Specialist",

                        "General Medicine",
                        "Cardiology",
                        "Dermatology",
                        "Pediatrics",
                        "Orthopedics",
                        "Gynecology",
                        "ENT"
                )
        );

        doctor.setEmail(
                ValidationUtil.email(
                        req.getParameter(
                                "email"
                        )
                )
        );

        doctor.setPhone(
                ValidationUtil.phone(
                        req.getParameter(
                                "phone"
                        ),
                        "Phone"
                )
        );

        doctor.setExperienceYears(
                ValidationUtil.integer(
                        req.getParameter(
                                "experienceYears"
                        ),
                        0,
                        60,
                        "Experience"
                )
        );

        String password =
                req.getParameter(
                        "password"
                );

        ValidationUtil
                .strongPassword(
                        password
                );

        doctor.setPassword(
                password
        );

        new DoctorDao(conn)
                .add(doctor);

        req.getSession()
                .setAttribute(
                        "msg",
                        "Doctor added successfully."
                );

        resp.sendRedirect(
                "add_doctor.jsp"
        );
    }

    private void requireRole(
            String actual,
            String required) {

        if (!required.equals(actual)) {

            throw new SecurityException(
                    "Unauthorized role"
            );
        }
    }
}