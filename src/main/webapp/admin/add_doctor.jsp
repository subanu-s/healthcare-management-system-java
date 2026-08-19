<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.util.CsrfUtil" %>

<%
if (session.getAttribute("adminEmail") == null) {
    response.sendRedirect("../admin_login.jsp");
    return;
}
%>

<!DOCTYPE html>
<html>

<head>
    <title>Add Doctor | Digital Care</title>
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
        DOCTOR MANAGEMENT
    </span>

    <h2 class="mt-2">
        Register Doctor
    </h2>

    <p class="text-muted-custom">
        Create a secure doctor account and assign
        the appropriate clinical speciality.
    </p>

    <%
    String msg =
        (String) session.getAttribute("msg");

    if (msg != null) {
    %>

    <div class="alert alert-clinic">
        <%=msg%>
    </div>

    <%
        session.removeAttribute("msg");
    }
    %>

    <div class="form-card mt-4">

        <form method="post"
              action="add-doctor">

            <input type="hidden"
                   name="_csrf"
                   value="<%=CsrfUtil.token(session)%>">

            <div class="form-section-title">
                Professional Information
            </div>

            <div class="row g-3">

                <div class="col-md-6">

                    <label class="form-label">
                        Doctor Full Name
                    </label>

                    <input class="form-control"
                           type="text"
                           name="fullName"
                           maxlength="100"
                           placeholder="e.g. Priya Raman"
                           required>

                </div>

                <div class="col-md-6">

                    <label class="form-label">
                        Qualification
                    </label>

                    <input class="form-control"
                           type="text"
                           name="qualification"
                           maxlength="100"
                           placeholder="e.g. MBBS, MD"
                           required>

                </div>

                <div class="col-md-6">

                    <label class="form-label">
                        Clinical Speciality
                    </label>

                    <select class="form-select"
                            name="specialist"
                            required>

                        <option value="" selected disabled>
                            Select Speciality
                        </option>

                        <option value="General Medicine">
                            General Medicine
                        </option>

                        <option value="Cardiology">
                            Cardiology
                        </option>

                        <option value="Dermatology">
                            Dermatology
                        </option>

                        <option value="Pediatrics">
                            Pediatrics
                        </option>

                        <option value="Orthopedics">
                            Orthopedics
                        </option>

                        <option value="Gynecology">
                            Gynecology
                        </option>

                        <option value="ENT">
                            ENT
                        </option>

                    </select>

                </div>

                <div class="col-md-6">

                    <label class="form-label">
                        Experience (Years)
                    </label>

                    <input class="form-control"
                           type="number"
                           name="experienceYears"
                           min="0"
                           max="60"
                           placeholder="e.g. 5"
                           required>

                </div>

            </div>


            <div class="form-section-title mt-4">
                Contact & Login Information
            </div>

            <div class="row g-3">

                <div class="col-md-6">

                    <label class="form-label">
                        Email Address
                    </label>

                    <input class="form-control"
                           type="email"
                           name="email"
                           maxlength="120"
                           placeholder="doctor@example.com"
                           required>

                </div>

                <div class="col-md-6">

                    <label class="form-label">
                        Mobile Number
                    </label>

                    <input class="form-control"
                           type="tel"
                           name="phone"
                           maxlength="10"
                           pattern="[6-9][0-9]{9}"
                           placeholder="10-digit mobile number"
                           required>

                </div>

                <div class="col-md-6">

                    <label class="form-label">
                        Temporary Password
                    </label>

                    <input class="form-control"
                           type="password"
                           name="password"
                           minlength="8"
                           autocomplete="new-password"
                           required>

                    <small class="text-muted">
                        Minimum 8 characters with uppercase,
                        lowercase and a number.
                    </small>

                </div>

            </div>

            <button type="submit"
                    class="btn btn-brand mt-4">

                Register Doctor

            </button>

        </form>

    </div>

</div>

</body>
</html>