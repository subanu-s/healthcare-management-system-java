<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.entity.*" %>
<%@ page import="com.dao.*" %>
<%@ page import="com.db.DbConnect" %>
<%@ page import="com.util.CsrfUtil" %>
<%@ page import="java.util.*" %>

<%
User user = (User) session.getAttribute("userObj");

if (user == null) {
    response.sendRedirect("../user_login.jsp");
    return;
}

List<Doctor> doctors;
List<Consultation> consultations;

try (java.sql.Connection conn = DbConnect.getConn()) {

    doctors = new DoctorDao(conn).findAll();

    consultations = new ConsultationDao(conn)
            .findByUser(user.getId());
}
%>

<!DOCTYPE html>
<html>

<head>
    <meta charset="UTF-8">

    <title>Online Consultation | Digital Care</title>

    <%@include file="../component/allcss.jsp"%>
</head>

<body>

<div class="container py-4">

    <div class="d-flex justify-content-between align-items-center mb-4">

        <a href="dashboard.jsp">
            &larr; Dashboard
        </a>

        <a href="../logout">
            Logout
        </a>

    </div>


    <span class="eyebrow">
        TELEHEALTH
    </span>

    <h2 class="mt-2">
        Online Consultation
    </h2>

    <p class="text-muted-custom">
        Request a secure online consultation with one
        of the available doctors.
    </p>


    <%
    String msg = (String) session.getAttribute("msg");

    if (msg != null) {
    %>

    <div class="alert alert-clinic">
        <%=msg%>
    </div>

    <%
        session.removeAttribute("msg");
    }
    %>


    <!-- ========================= -->
    <!-- CONSULTATION REQUEST FORM -->
    <!-- ========================= -->

    <div class="form-card mt-4 mb-5">

        <div class="form-section-title">
            Consultation Request
        </div>

        <form method="post"
              action="../request-consultation">

            <!-- CSRF PROTECTION -->

            <input type="hidden"
                   name="_csrf"
                   value="<%=CsrfUtil.token(session)%>">


            <div class="row g-3">


                <!-- DOCTOR -->

                <div class="col-md-6">

                    <label class="form-label">
                        Select Doctor
                    </label>

                    <select class="form-select"
                            name="doctorId"
                            required>

                        <option value=""
                                selected
                                disabled>
                            Choose Doctor
                        </option>

                        <% for (Doctor doctor : doctors) { %>

                        <option value="<%=doctor.getId()%>">

                            <%=doctor.getFullName()%>
                            -
                            <%=doctor.getSpecialist()%>

                        </option>

                        <% } %>

                    </select>

                </div>


                <!-- DATE -->

                <div class="col-md-6">

                    <label class="form-label">
                        Preferred Date & Time
                    </label>

                    <input class="form-control"
                           type="datetime-local"
                           name="requestedDate"
                           required>

                </div>


                <!-- MODE -->

                <div class="col-md-4">

                    <label class="form-label">
                        Consultation Mode
                    </label>

                    <select class="form-select"
                            name="mode"
                            required>

                        <option value="Video">
                            Video Consultation
                        </option>

                        <option value="Audio">
                            Audio Consultation
                        </option>

                        <option value="Chat">
                            Chat Consultation
                        </option>

                    </select>

                </div>


                <!-- SYMPTOM -->

                <div class="col-md-4">

                    <label class="form-label">
                        Primary Symptom
                    </label>

                    <select class="form-select"
                            name="symptom"
                            required>

                        <option value="Fever">Fever</option>
                        <option value="Cough">Cough</option>
                        <option value="Headache">Headache</option>
                        <option value="Chest Pain">Chest Pain</option>
                        <option value="Skin Problem">Skin Problem</option>
                        <option value="Stomach Pain">Stomach Pain</option>
                        <option value="Joint Pain">Joint Pain</option>
                        <option value="Diabetes Review">
                            Diabetes Review
                        </option>
                        <option value="Blood Pressure Review">
                            Blood Pressure Review
                        </option>
                        <option value="General">
                            General Consultation
                        </option>

                    </select>

                </div>


                <!-- SEVERITY -->

                <div class="col-md-4">

                    <label class="form-label">
                        Severity
                    </label>

                    <select class="form-select"
                            name="severity"
                            required>

                        <option value="Mild">
                            Mild
                        </option>

                        <option value="Moderate">
                            Moderate
                        </option>

                        <option value="Severe">
                            Severe
                        </option>

                    </select>

                </div>


                <!-- DURATION -->

                <div class="col-md-4">

                    <label class="form-label">
                        Symptom Duration
                    </label>

                    <select class="form-select"
                            name="symptomDuration"
                            required>

                        <option value="Today">
                            Today
                        </option>

                        <option value="1-3 days">
                            1-3 days
                        </option>

                        <option value="4-7 days">
                            4-7 days
                        </option>

                        <option value="More than a week">
                            More than a week
                        </option>

                        <option value="More than a month">
                            More than a month
                        </option>

                    </select>

                </div>


                <!-- PREVIOUS CONSULTATION -->

                <div class="col-md-4">

                    <label class="form-label">
                        Previous Consultation?
                    </label>

                    <select class="form-select"
                            name="previousConsultation">

                        <option value="NO">
                            No
                        </option>

                        <option value="YES">
                            Yes
                        </option>

                    </select>

                </div>


                <!-- LANGUAGE -->

                <div class="col-md-4">

                    <label class="form-label">
                        Preferred Language
                    </label>

                    <select class="form-select"
                            name="preferredLanguage">

                        <option value="English">
                            English
                        </option>

                        <option value="Tamil">
                            Tamil
                        </option>

                        <option value="Hindi">
                            Hindi
                        </option>

                    </select>

                </div>


                <!-- REASON -->

                <div class="col-12">

                    <label class="form-label">
                        Clinical Concern / Reason
                    </label>

                    <textarea class="form-control"
                              name="reason"
                              rows="4"
                              maxlength="500"
                              placeholder="Briefly describe your symptoms or reason for consultation"
                              required></textarea>

                </div>


                <!-- CONSENT -->

                <div class="col-12">

                    <div class="form-check">

                        <input class="form-check-input"
                               type="checkbox"
                               name="consent"
                               value="YES"
                               id="consultationConsent"
                               required>

                        <label class="form-check-label"
                               for="consultationConsent">

                            I consent to sharing the clinical
                            information entered here with the
                            selected doctor for this consultation.

                        </label>

                    </div>

                </div>

            </div>


            <button type="submit"
                    class="btn btn-brand mt-4">

                Request Consultation

            </button>

        </form>

    </div>


    <!-- ==================== -->
    <!-- CONSULTATION HISTORY -->
    <!-- ==================== -->

    <div class="d-flex justify-content-between align-items-center mb-3">

        <div>

            <span class="eyebrow">
                MY CONSULTATIONS
            </span>

            <h3 class="mt-1 mb-0">
                Consultation History
            </h3>

        </div>

    </div>


    <% if (consultations == null || consultations.isEmpty()) { %>

    <div class="form-card text-center py-5">

        <h5>No consultations yet</h5>

        <p class="text-muted-custom mb-0">
            Your online consultation requests will appear here.
        </p>

    </div>

    <% } else { %>


    <div class="table-responsive table-wrap">

        <table class="table align-middle">

            <thead>

            <tr>

                <th>
                    Schedule
                </th>

                <th>
                    Doctor
                </th>

                <th>
                    Clinical Concern
                </th>

                <th>
                    Status
                </th>

                <th>
                    Consultation
                </th>

                <th>
                    Clinical Summary
                </th>

            </tr>

            </thead>


            <tbody>

            <% for (Consultation c : consultations) { %>

            <tr>

                <!-- DATE -->

                <td>

                    <strong>
                        <%=c.requestedDate%>
                    </strong>

                    <br>

                    <small class="text-muted">
                        <%=c.mode%>
                    </small>

                </td>


                <!-- DOCTOR -->

                <td>

                    <strong>
                        <%=c.doctorName == null
                            ? "-"
                            : c.doctorName%>
                    </strong>

                </td>


                <!-- SYMPTOM -->

                <td>

                    <strong>
                        <%=c.symptom == null
                            ? "-"
                            : c.symptom%>
                    </strong>

                    <br>

                    <small>

                        Severity:
                        <%=c.severity == null
                            ? "-"
                            : c.severity%>

                    </small>

                    <br>

                    <small>

                        <%=c.reason == null
                            ? ""
                            : c.reason%>

                    </small>

                </td>


                <!-- STATUS -->

                <td>

                    <span class="badge-status status-<%=c.status%>">

                        <%=c.status%>

                    </span>

                </td>


                <!-- MEETING -->

                <td>

                    <%
                    if (c.meetingLink != null
                            && !c.meetingLink.trim().isEmpty()) {
                    %>

                    <a class="btn btn-sm btn-brand"
                       target="_blank"
                       rel="noopener noreferrer"
                       href="<%=c.meetingLink%>">

                        Open Consultation Room

                    </a>

                    <%

                    } else {

                    %>

                    <span class="text-muted">
                        Available after doctor accepts
                    </span>

                    <% } %>

                </td>


                <!-- CLINICAL SUMMARY -->

                <td>

                    <strong>
                        Diagnosis:
                    </strong>

                    <%=c.diagnosis == null
                        || c.diagnosis.trim().isEmpty()
                        ? "-"
                        : c.diagnosis%>

                    <br>


                    <strong>
                        Doctor Notes:
                    </strong>

                    <%=c.doctorNotes == null
                        || c.doctorNotes.trim().isEmpty()
                        ? "-"
                        : c.doctorNotes%>

                    <br>


                    <strong>
                        Prescription:
                    </strong>

                    <%=c.prescription == null
                        || c.prescription.trim().isEmpty()
                        ? "-"
                        : c.prescription%>

                    <br>


                    <strong>
                        Follow-up:
                    </strong>

                    <%=c.followUpDate == null
                        || c.followUpDate.trim().isEmpty()
                        ? "-"
                        : c.followUpDate%>

                </td>

            </tr>

            <% } %>

            </tbody>

        </table>

    </div>

    <% } %>

</div>

</body>
</html>