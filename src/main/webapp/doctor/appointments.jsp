<%@ page import="com.entity.*,com.dao.*,com.db.DbConnect,com.util.CsrfUtil,java.util.*" %>
<%@ page contentType="text/html;charset=UTF-8" %>

<%
Doctor doctor =
    (Doctor)
    session.getAttribute("doctorObj");

List<Appointment> appointments;

try (java.sql.Connection conn =
         DbConnect.getConn()) {

    appointments =
        new AppointmentDao(conn)
            .findByDoctor(
                doctor.getId()
            );
}
%>

<!DOCTYPE html>
<html>

<head>

    <title>
        Doctor Appointments | Digital Care
    </title>

    <%@include file="../component/allcss.jsp"%>

</head>

<body>

<div class="container py-4">

    <div class="d-flex justify-content-between">

        <a href="dashboard.jsp">
            &larr; Dashboard
        </a>

        <a href="../logout">
            Logout
        </a>

    </div>

    <h2 class="mt-4">
        Assigned Appointments
    </h2>

    <p class="text-muted-custom">
        Review clinical context before confirming or completing a visit.
    </p>

    <div class="row g-3 mt-1">

        <% for (Appointment a : appointments) { %>

        <div class="col-12">

            <div class="clinic-card p-4">

                <div class="row g-3">

                    <div class="col-md-3">

                        <strong>
                            <%=a.patientName%>
                        </strong>

                        <br>

                        <span class="text-muted-custom">
                            <%=a.age%> yrs
                            •
                            <%=a.gender%>
                        </span>

                        <br>

                        <small>
                            <%=a.phone%>
                        </small>

                    </div>


                    <div class="col-md-3">

                        <strong>
                            <%=a.appointmentDate%>
                            •
                            <%=a.timeSlot%>
                        </strong>

                        <br>

                        <%=a.appointmentType%>

                        <br>

                        <span
                            class="badge-status status-<%=a.status%>">

                            <%=a.status%>

                        </span>

                    </div>


                    <div class="col-md-3">

                        <strong>
                            <%=a.symptom%>
                        </strong>

                        —
                        <%=a.severity%>

                        <br>

                        <small>
                            Duration:
                            <%=a.symptomDuration%>
                        </small>

                        <br>

                        <small>
                            Conditions:
                            <%=a.existingConditions == null
                                || a.existingConditions.isEmpty()
                                ? "-"
                                : a.existingConditions%>
                        </small>

                        <br>

                        <small>
                            Allergies:
                            <%=a.allergies == null
                                || a.allergies.isEmpty()
                                ? "-"
                                : a.allergies%>
                        </small>

                    </div>


                    <div class="col-md-3">

                        <form
                            method="post"
                            action="update-appointment">

                            <input
                                type="hidden"
                                name="_csrf"
                                value="<%=CsrfUtil.token(session)%>">

                            <input
                                type="hidden"
                                name="appointmentId"
                                value="<%=a.id%>">

                            <label class="form-label">
                                Status
                            </label>

                            <select
                                class="form-select form-select-sm mb-2"
                                name="status">

                                <option>CONFIRMED</option>
                                <option>COMPLETED</option>
                                <option>CANCELLED</option>

                            </select>

                            <input
                                class="form-control form-control-sm mb-2"
                                name="doctorRemarks"
                                maxlength="500"
                                placeholder="Doctor remarks">

                            <input
                                class="form-control form-control-sm mb-2"
                                name="cancellationReason"
                                maxlength="500"
                                placeholder="Cancellation reason if cancelled">

                            <button
                                class="btn btn-sm btn-brand">

                                Update Appointment

                            </button>

                        </form>

                    </div>

                </div>

            </div>

        </div>

        <% } %>

    </div>

</div>

</body>

</html>