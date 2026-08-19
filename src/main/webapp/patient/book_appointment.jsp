<%@ page import="com.entity.*,com.dao.*,com.db.DbConnect,com.util.CsrfUtil,java.util.*" %>
<%@ page contentType="text/html;charset=UTF-8" %>

<%
User user = (User) session.getAttribute("userObj");

List<Doctor> doctors;
List<Appointment> appointments;

try (java.sql.Connection conn = DbConnect.getConn()) {
    doctors = new DoctorDao(conn).findAll();
    appointments = new AppointmentDao(conn).findByUser(user.getId());
}
%>

<!DOCTYPE html>
<html>

<head>
    <title>Appointments | Digital Care</title>
    <%@include file="../component/allcss.jsp"%>
</head>

<body>

<div class="container py-4">

    <div class="d-flex justify-content-between align-items-center">
        <a href="dashboard.jsp">&larr; Dashboard</a>
        <a href="../logout">Logout</a>
    </div>

    <h2 class="mt-4">Book an Appointment</h2>

    <p class="text-muted-custom">
        Choose a doctor and provide relevant clinical information
        before confirming your appointment request.
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

    <div class="form-card mb-5">

        <form method="post"
              action="../book-appointment">

            <input type="hidden"
                   name="_csrf"
                   value="<%=CsrfUtil.token(session)%>">

            <div class="form-section-title">
                Doctor & Schedule
            </div>

            <div class="row g-3">

                <div class="col-md-6">

                    <label class="form-label">
                        Speciality
                    </label>

                    <select id="specialityFilter"
                            class="form-select">

                        <option value="">
                            All Specialities
                        </option>

                        <option>General Medicine</option>
                        <option>Cardiology</option>
                        <option>Dermatology</option>
                        <option>Pediatrics</option>
                        <option>Orthopedics</option>
                        <option>Gynecology</option>
                        <option>ENT</option>

                    </select>

                </div>

                <div class="col-md-6">

                    <label class="form-label">
                        Doctor
                    </label>

                    <select id="doctorSelect"
                            class="form-select"
                            name="doctorId"
                            required>

                        <% for (Doctor doctor : doctors) { %>

                        <option
                            value="<%=doctor.getId()%>"
                            data-speciality="<%=doctor.getSpecialist()%>">

                            <%=doctor.getFullName()%>
                            —
                            <%=doctor.getSpecialist()%>

                        </option>

                        <% } %>

                    </select>

                </div>

                <div class="col-md-4">

                    <label class="form-label">
                        Appointment Date
                    </label>

                    <input
                        class="form-control"
                        type="date"
                        name="appointmentDate"
                        min="<%=java.time.LocalDate.now()%>"
                        required>

                </div>

                <div class="col-md-4">

                    <label class="form-label">
                        Time Slot
                    </label>

                    <select class="form-select"
                            name="timeSlot"
                            required>

                        <option>09:00 AM</option>
                        <option>10:00 AM</option>
                        <option>11:00 AM</option>
                        <option>12:00 PM</option>
                        <option>02:00 PM</option>
                        <option>03:00 PM</option>
                        <option>04:00 PM</option>
                        <option>05:00 PM</option>

                    </select>

                </div>

                <div class="col-md-4">

                    <label class="form-label">
                        Appointment Type
                    </label>

                    <select class="form-select"
                            name="appointmentType">

                        <option>General Consultation</option>
                        <option>Follow-up</option>
                        <option>Review</option>
                        <option>Preventive Check-up</option>

                    </select>

                </div>

                <div class="col-md-4">

                    <label class="form-label">
                        Priority
                    </label>

                    <select class="form-select"
                            name="priority">

                        <option>ROUTINE</option>
                        <option>URGENT</option>
                        <option>FOLLOW-UP</option>

                    </select>

                </div>

            </div>


            <div class="form-section-title mt-4">
                Patient & Clinical Details
            </div>

            <div class="row g-3">

                <div class="col-md-6">

                    <label class="form-label">
                        Patient Name
                    </label>

                    <input
                        class="form-control"
                        name="patientName"
                        value="<%=user.getFullName()%>"
                        required>

                </div>

                <div class="col-md-3">

                    <label class="form-label">
                        Gender
                    </label>

                    <select class="form-select"
                            name="gender">

                        <option>Female</option>
                        <option>Male</option>
                        <option>Other</option>

                    </select>

                </div>

                <div class="col-md-3">

                    <label class="form-label">
                        Age
                    </label>

                    <input
                        class="form-control"
                        type="number"
                        name="age"
                        min="1"
                        max="120"
                        required>

                </div>

                <div class="col-md-4">

                    <label class="form-label">
                        Primary Symptom
                    </label>

                    <select class="form-select"
                            name="symptom">

                        <option>Fever</option>
                        <option>Cough</option>
                        <option>Headache</option>
                        <option>Chest Pain</option>
                        <option>Skin Problem</option>
                        <option>Stomach Pain</option>
                        <option>Joint Pain</option>
                        <option>Diabetes Review</option>
                        <option>Blood Pressure Review</option>
                        <option>General</option>

                    </select>

                </div>

                <div class="col-md-4">

                    <label class="form-label">
                        Severity
                    </label>

                    <select class="form-select"
                            name="severity">

                        <option>Mild</option>
                        <option>Moderate</option>
                        <option>Severe</option>

                    </select>

                </div>

                <div class="col-md-4">

                    <label class="form-label">
                        Symptom Duration
                    </label>

                    <select class="form-select"
                            name="symptomDuration">

                        <option>Today</option>
                        <option>1-3 days</option>
                        <option>4-7 days</option>
                        <option>More than a week</option>
                        <option>More than a month</option>

                    </select>

                </div>

                <div class="col-md-6">

                    <label class="form-label">
                        Existing Conditions
                    </label>

                    <input
                        class="form-control"
                        name="existingConditions"
                        placeholder="e.g. Diabetes, hypertension">

                </div>

                <div class="col-md-6">

                    <label class="form-label">
                        Allergies
                    </label>

                    <input
                        class="form-control"
                        name="allergies"
                        placeholder="Drug or food allergies, or None">

                </div>

                <div class="col-md-6">

                    <label class="form-label">
                        Current Medication
                    </label>

                    <input
                        class="form-control"
                        name="currentMedication">

                </div>

                <div class="col-md-6">

                    <label class="form-label">
                        Emergency Contact
                    </label>

                    <input
                        class="form-control"
                        name="emergencyContact"
                        maxlength="10"
                        pattern="[6-9][0-9]{9}"
                        required>

                </div>

                <div class="col-md-6">

                    <label class="form-label">
                        Email
                    </label>

                    <input
                        class="form-control"
                        type="email"
                        name="email"
                        value="<%=user.getEmail()%>"
                        required>

                </div>

                <div class="col-md-6">

                    <label class="form-label">
                        Mobile Number
                    </label>

                    <input
                        class="form-control"
                        name="phone"
                        value="<%=user.getPhone() == null ? "" : user.getPhone()%>"
                        maxlength="10"
                        required>

                </div>

                <div class="col-12">

                    <label class="form-label">
                        Address
                    </label>

                    <input
                        class="form-control"
                        name="address">

                </div>

                <div class="col-12">

                    <label class="form-label">
                        Additional Notes
                    </label>

                    <textarea
                        class="form-control"
                        name="patientNotes"
                        rows="3"
                        maxlength="500"></textarea>

                </div>

            </div>

            <button class="btn btn-brand mt-4">
                Confirm Appointment Request
            </button>

        </form>

    </div>


    <h3>Appointment History</h3>

    <div class="table-responsive table-wrap">

        <table class="table align-middle">

            <thead>

            <tr>
                <th>Date / Slot</th>
                <th>Doctor</th>
                <th>Clinical Context</th>
                <th>Status</th>
                <th>Action</th>
            </tr>

            </thead>

            <tbody>

            <% for (Appointment a : appointments) { %>

            <tr>

                <td>
                    <strong><%=a.appointmentDate%></strong>
                    <br>
                    <small><%=a.timeSlot%></small>
                </td>

                <td>
                    <%=a.doctorName%>
                    <br>
                    <small class="text-muted">
                        <%=a.specialist%>
                    </small>
                </td>

                <td>
                    <%=a.symptom%>
                    —
                    <%=a.severity%>

                    <br>

                    <small>
                        <%=a.appointmentType%>
                    </small>
                </td>

                <td>

                    <span class="badge-status status-<%=a.status%>">
                        <%=a.status%>
                    </span>

                    <%
                    if (a.doctorRemarks != null
                            && !a.doctorRemarks.isEmpty()) {
                    %>

                    <br>

                    <small>
                        <%=a.doctorRemarks%>
                    </small>

                    <% } %>

                </td>

                <td>

                    <% if ("PENDING".equals(a.status)) { %>

                    <form
                        method="post"
                        action="cancel-appointment"
                        class="d-flex gap-1">

                        <input type="hidden"
                               name="_csrf"
                               value="<%=CsrfUtil.token(session)%>">

                        <input type="hidden"
                               name="appointmentId"
                               value="<%=a.id%>">

                        <input
                            class="form-control form-control-sm"
                            name="reason"
                            placeholder="Cancellation reason"
                            required>

                        <button class="btn btn-sm btn-outline-danger">
                            Cancel
                        </button>

                    </form>

                    <% } else { %>

                    -

                    <% } %>

                </td>

            </tr>

            <% } %>

            </tbody>

        </table>

    </div>

</div>


<script>

document
    .getElementById("specialityFilter")
    .addEventListener(
        "change",
        function() {

            const value =
                this.value;

            const select =
                document.getElementById(
                    "doctorSelect"
                );

            Array
                .from(select.options)
                .forEach(
                    option => {

                        option.hidden =
                            value
                            &&
                            option.dataset.speciality
                            !== value;
                    }
                );

            const first =
                Array
                    .from(select.options)
                    .find(
                        option =>
                            !option.hidden
                    );

            if (first) {
                select.value =
                    first.value;
            }
        }
    );

</script>

</body>

</html>