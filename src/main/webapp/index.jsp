<%@ page contentType="text/html;charset=UTF-8" %>

<!DOCTYPE html>
<html>

<head>

<title>
Digital Care Hospital
</title>

<%@include file="component/allcss.jsp"%>

</head>

<body>

<%@include file="component/navbar.jsp"%>


<section class="hero">

<div class="container">

<div class="row align-items-center g-5">


<div class="col-lg-7">

<span class="eyebrow">
DIGITAL CARE HOSPITAL
</span>

<h1>
Healthcare appointments
made simple and connected.
</h1>

<p>

Choose the right clinical
department, schedule a doctor
appointment or request an
online consultation through
our patient portal.

</p>


<div class="d-flex flex-wrap gap-2 mt-4">

<a href="signup.jsp"
class="btn btn-brand btn-lg">

Book as New Patient

</a>

<a href="user_login.jsp"
class="btn btn-outline-dark btn-lg">

Existing Patient

</a>

</div>

</div>


<div class="col-lg-5">

<div class="search-card">

<h4>
Find the right care
</h4>

<p class="text-muted-custom">
Choose a department based
on your healthcare need.
</p>


<label class="form-label">
Speciality
</label>

<select class="form-select mb-3">

<option>
General Medicine
</option>

<option>
Cardiology
</option>

<option>
Dermatology
</option>

<option>
Pediatrics
</option>

<option>
Orthopedics
</option>

<option>
Gynecology
</option>

<option>
ENT
</option>

</select>


<label class="form-label">
Care Type
</label>

<select class="form-select mb-3">

<option>
In-person Appointment
</option>

<option>
Online Consultation
</option>

</select>


<a href="user_login.jsp"
class="btn btn-brand w-100">

Continue to Booking

</a>


<div class="small text-muted mt-3">

For emergency medical
conditions, contact your
nearest emergency service.
This portal is for scheduled care.

</div>

</div>

</div>

</div>

</div>

</section>


<section
class="section"
id="specialities">

<div class="container">

<span class="eyebrow">
CLINICAL SPECIALITIES
</span>

<h2 class="section-title mt-2">
Choose care by speciality
</h2>


<div class="row g-3 mt-3">


<div class="col-md-4">

<div class="speciality-card">

<div class="speciality-icon">
GM
</div>

<h5>
General Medicine
</h5>

<p class="text-muted-custom">

Fever, cough, infections,
routine health concerns
and general medical review.

</p>

</div>

</div>


<div class="col-md-4">

<div class="speciality-card">

<div class="speciality-icon">
CA
</div>

<h5>
Cardiology
</h5>

<p class="text-muted-custom">

Heart health,
blood pressure monitoring,
chest-related concerns
and follow-up reviews.

</p>

</div>

</div>


<div class="col-md-4">

<div class="speciality-card">

<div class="speciality-icon">
DE
</div>

<h5>
Dermatology
</h5>

<p class="text-muted-custom">

Skin, hair,
allergy and other
dermatology-related concerns.

</p>

</div>

</div>


<div class="col-md-4">

<div class="speciality-card">

<div class="speciality-icon">
PE
</div>

<h5>
Pediatrics
</h5>

<p class="text-muted-custom">

Child healthcare,
general illness
and developmental review.

</p>

</div>

</div>


<div class="col-md-4">

<div class="speciality-card">

<div class="speciality-icon">
OR
</div>

<h5>
Orthopedics
</h5>

<p class="text-muted-custom">

Bone, joint,
muscle and mobility-related
medical concerns.

</p>

</div>

</div>


<div class="col-md-4">

<div class="speciality-card">

<div class="speciality-icon">
ENT
</div>

<h5>
ENT
</h5>

<p class="text-muted-custom">

Ear, nose,
throat and related
consultation services.

</p>

</div>

</div>

</div>

</div>

</section>


<section
class="section bg-white"
id="services">

<div class="container">

<div class="row g-4">


<div class="col-md-4">

<div class="clinic-card p-4 h-100">

<h5>
Structured Appointment
</h5>

<p class="text-muted-custom">

Select your doctor,
date and available slot,
then provide symptoms,
severity and medical history.

</p>

</div>

</div>


<div class="col-md-4">

<div class="clinic-card p-4 h-100">

<h5>
Online Consultation
</h5>

<p class="text-muted-custom">

Request scheduled video,
audio or chat consultation
and receive a protected
consultation room link.

</p>

</div>

</div>


<div class="col-md-4">

<div class="clinic-card p-4 h-100">

<h5>
Clinical Follow-up
</h5>

<p class="text-muted-custom">

Doctors can record
diagnosis, vitals,
recommended tests,
prescription and follow-up.

</p>

</div>

</div>

</div>

</div>

</section>


<footer class="footer">

<div class="container">

<div class="d-flex
justify-content-between
flex-wrap gap-2">

<strong>
DIGITAL CARE
</strong>

<span>
Healthcare Management Portal
</span>

</div>

</div>

</footer>


</body>

</html>