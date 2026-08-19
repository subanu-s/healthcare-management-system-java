<%@ page import="
com.db.DbConnect,
java.sql.*
" %>

<%@ page contentType="text/html;charset=UTF-8" %>

<%

int doctors = 0;
int patients = 0;
int appointments = 0;
int consultations = 0;

try (

Connection conn =
DbConnect.getConn();

Statement st =
conn.createStatement()

) {

ResultSet rs =
st.executeQuery(
"SELECT COUNT(*) FROM doctors"
);

if (rs.next()) {
doctors = rs.getInt(1);
}


rs =
st.executeQuery(
"SELECT COUNT(*) FROM users"
);

if (rs.next()) {
patients = rs.getInt(1);
}


rs =
st.executeQuery(
"SELECT COUNT(*) FROM appointments"
);

if (rs.next()) {
appointments = rs.getInt(1);
}


rs =
st.executeQuery(
"SELECT COUNT(*) FROM consultations"
);

if (rs.next()) {
consultations = rs.getInt(1);
}

}

%>


<!DOCTYPE html>

<html>

<head>

<title>
Admin Dashboard
</title>

<%@include file="../component/allcss.jsp"%>

</head>


<body>


<div class="dashboard-top">

<div class="container
d-flex
justify-content-between
align-items-center">

<div>

<span class="eyebrow">
ADMINISTRATION
</span>

<h2>
Clinical Operations Overview
</h2>

</div>


<a href="../logout"
class="btn btn-outline-dark">

Logout

</a>

</div>

</div>


<div class="container py-5">


<div class="row g-3">


<div class="col-md-3">

<div class="summary-card p-4">

<h2>
<%=doctors%>
</h2>

<span class="text-muted-custom">
Doctors
</span>

</div>

</div>


<div class="col-md-3">

<div class="summary-card p-4">

<h2>
<%=patients%>
</h2>

<span class="text-muted-custom">
Patients
</span>

</div>

</div>


<div class="col-md-3">

<div class="summary-card p-4">

<h2>
<%=appointments%>
</h2>

<span class="text-muted-custom">
Appointments
</span>

</div>

</div>


<div class="col-md-3">

<div class="summary-card p-4">

<h2>
<%=consultations%>
</h2>

<span class="text-muted-custom">
Consultations
</span>

</div>

</div>

</div>


<div class="clinic-card p-4 mt-4">

<h4>
Doctor Management
</h4>

<p class="text-muted-custom">

Register doctors with
qualification,
speciality,
experience and secure
login credentials.

</p>

<a href="add_doctor.jsp"
class="btn btn-brand">

Register Doctor

</a>

</div>

</div>

</body>

</html>