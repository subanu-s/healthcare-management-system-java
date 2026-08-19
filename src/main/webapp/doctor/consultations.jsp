<%@ page import="com.entity.*,com.dao.*,com.db.DbConnect,com.util.CsrfUtil,java.util.*" %>
<%@ page contentType="text/html;charset=UTF-8" %>

<%
Doctor doctor =
    (Doctor)
    session.getAttribute("doctorObj");

List<Consultation> consultations;

try (java.sql.Connection conn =
         DbConnect.getConn()) {

    consultations =
        new ConsultationDao(conn)
            .findByDoctor(
                doctor.getId()
            );
}
%>

<!DOCTYPE html>
<html>

<head>

    <title>
        Clinical Consultations | Digital Care
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
        Online Consultation Requests
    </h2>

    <p class="text-muted-custom">
        Accept consultations, record clinical observations
        and provide prescription and follow-up details.
    </p>

    <div class="row g-4 mt-1">

        <% for (Consultation c : consultations) { %>

        <div class="col-12">

            <div class="clinic-card p-4">

                <div class="row g-4">

                    <div class="col-lg-4">

                        <span
                            class="badge-status status-<%=c.status%>">

                            <%=c.status%>

                        </span>

                        <h4 class="mt-3">
                            <%=c.patientName%>
                        </h4>

                        <p class="mb-1">

                            <strong>
                                <%=c.symptom%>
                            </strong>

                            —
                            <%=c.severity%>

                        </p>

                        <p class="text-muted-custom">
                            <%=c.reason%>
                        </p>

                        <small>

                            <%=c.requestedDate%>
                            •
                            <%=c.mode%>
                            •
                            <%=c.preferredLanguage%>

                        </small>

                    </div>


                    <div class="col-lg-8">

                        <form
                            method="post"
                            action="save-consultation">

                            <input
                                type="hidden"
                                name="_csrf"
                                value="<%=CsrfUtil.token(session)%>">

                            <input
                                type="hidden"
                                name="consultationId"
                                value="<%=c.id%>">

                            <div class="row g-2">

                                <div class="col-md-3">

                                    <label class="form-label">
                                        Status
                                    </label>

                                    <select
                                        class="form-select"
                                        name="status">

                                        <option>ACCEPTED</option>
                                        <option>COMPLETED</option>
                                        <option>DECLINED</option>

                                    </select>

                                </div>

                                <div class="col-md-9">

                                    <label class="form-label">
                                        Diagnosis
                                    </label>

                                    <input
                                        class="form-control"
                                        name="diagnosis"
                                        maxlength="255"
                                        value="<%=c.diagnosis == null
                                            ? ""
                                            : c.diagnosis%>">

                                </div>

                                <div class="col-md-4">

                                    <label class="form-label">
                                        Temperature
                                    </label>

                                    <input
                                        class="form-control"
                                        name="temperature"
                                        maxlength="20"
                                        placeholder="e.g. 98.6 F">

                                </div>

                                <div class="col-md-4">

                                    <label class="form-label">
                                        Blood Pressure
                                    </label>

                                    <input
                                        class="form-control"
                                        name="bloodPressure"
                                        maxlength="30"
                                        placeholder="e.g. 120/80">

                                </div>

                                <div class="col-md-4">

                                    <label class="form-label">
                                        Pulse
                                    </label>

                                    <input
                                        class="form-control"
                                        name="pulse"
                                        maxlength="20"
                                        placeholder="e.g. 78 bpm">

                                </div>

                                <div class="col-12">

                                    <label class="form-label">
                                        Recommended Tests
                                    </label>

                                    <input
                                        class="form-control"
                                        name="recommendedTests"
                                        maxlength="500">

                                </div>

                                <div class="col-md-6">

                                    <label class="form-label">
                                        Clinical Notes
                                    </label>

                                    <textarea
                                        class="form-control"
                                        rows="3"
                                        name="doctorNotes"><%=c.doctorNotes == null
                                        ? ""
                                        : c.doctorNotes%></textarea>

                                </div>

                                <div class="col-md-6">

                                    <label class="form-label">
                                        Prescription / Dosage Instructions
                                    </label>

                                    <textarea
                                        class="form-control"
                                        rows="3"
                                        name="prescription"><%=c.prescription == null
                                        ? ""
                                        : c.prescription%></textarea>

                                </div>

                                <div class="col-md-4">

                                    <label class="form-label">
                                        Follow-up Date
                                    </label>

                                    <input
                                        class="form-control"
                                        type="date"
                                        name="followUpDate">

                                </div>

                                <div class="col-md-8 d-flex align-items-end">

                                    <button
                                        class="btn btn-brand">

                                        Save Clinical Update

                                    </button>

                                </div>

                            </div>

                        </form>


                        <%
                        if (c.meetingLink != null
                                && !c.meetingLink.isEmpty()) {
                        %>

                        <div class="mt-3">

                            <strong>
                                Generated consultation room:
                            </strong>

                            <br>

                            <a href="<%=c.meetingLink%>">
                                <%=c.meetingLink%>
                            </a>

                        </div>

                        <% } %>

                    </div>

                </div>

            </div>

        </div>

        <% } %>

    </div>

</div>

</body>

</html>