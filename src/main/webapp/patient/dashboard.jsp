<%@ page import="com.entity.User" %>

<%@ page contentType="text/html;charset=UTF-8" %>

<%

User user =
(User) session.getAttribute("userObj");

%>

<!DOCTYPE html>

<html>

<head>

<title>
Patient Dashboard
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
PATIENT PORTAL
</span>

<h2 class="mb-0">

Welcome,
<%=user.getFullName()%>

</h2>

<p class="text-muted-custom mb-0">

Manage appointments
and online consultations.

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

<h4 class="mt-2">
Appointments
</h4>

<p class="text-muted-custom">

Book a doctor based on
speciality, choose a time slot,
provide clinical information
and track appointment status.

</p>

<a href="book_appointment.jsp"
class="btn btn-brand">

Manage Appointments

</a>

</div>

</div>


<div class="col-md-6">

<div class="dashboard-card">

<span class="num">
02
</span>

<h4 class="mt-2">
Online Consultation
</h4>

<p class="text-muted-custom">

Request remote consultation,
access generated meeting links,
doctor notes, prescriptions
and follow-up information.

</p>

<a href="consultations.jsp"
class="btn btn-brand">

Online Consultation

</a>

</div>

</div>

</div>


<div class="clinic-card p-4 mt-4">

<h5>
Patient Account
</h5>

<hr>

<div class="row">

<div class="col-md-6">

<small class="text-muted">
Registered Name
</small>

<p class="fw-semibold">

<%=user.getFullName()%>

</p>

</div>


<div class="col-md-6">

<small class="text-muted">
Registered Email
</small>

<p class="fw-semibold">

<%=user.getEmail()%>

</p>

</div>

</div>

</div>

</div>

</body>

</html>