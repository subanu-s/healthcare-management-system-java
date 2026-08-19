<%@ page import="com.entity.Doctor" %>

<%@ page contentType="text/html;charset=UTF-8" %>

<%

Doctor doctor =
(Doctor) session.getAttribute(
"doctorObj"
);

%>

<!DOCTYPE html>

<html>

<head>

<title>
Doctor Dashboard
</title>

<%@include file="../component/allcss.jsp"%>

</head>


<body class="dashboard-shell">


<div class="dashboard-top">

<div class="container
d-flex
justify-content-between
align-items-center">

<div>

<span class="eyebrow">
CLINICAL PORTAL
</span>

<h2 class="mb-0">

Dr.
<%=doctor.getFullName()%>

</h2>

<p class="text-muted-custom mb-0">

<%=doctor.getSpecialist()%>
•
<%=doctor.getQualification()%>

</p>

</div>


<a href="../logout"
class="btn btn-outline-dark">

Logout

</a>

</div>

</div>


<div class="container py-5">

<div class="row g-4">


<div class="col-md-6">

<div class="dashboard-card">

<span class="num">
01
</span>

<h4>
Patient Appointments
</h4>

<p class="text-muted-custom">

Review patient symptoms,
medical context,
appointment priority,
date and time slot.

</p>

<a href="appointments.jsp"
class="btn btn-brand">

Review Appointments

</a>

</div>

</div>


<div class="col-md-6">

<div class="dashboard-card">

<span class="num">
02
</span>

<h4>
Online Consultations
</h4>

<p class="text-muted-custom">

Accept consultation requests,
generate protected room links,
record vitals, diagnosis,
prescription and follow-up.

</p>

<a href="consultations.jsp"
class="btn btn-brand">

Manage Consultations

</a>

</div>

</div>

</div>

</div>

</body>

</html>