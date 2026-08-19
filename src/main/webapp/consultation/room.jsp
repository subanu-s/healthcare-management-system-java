<%@ page import="
com.entity.Consultation,
com.dao.ConsultationDao,
com.db.DbConnect
" %>

<%@ page contentType="text/html;charset=UTF-8" %>

<%

String token =
request.getParameter("token");

Consultation consultation =
null;


if (token != null) {

try (
java.sql.Connection conn =
DbConnect.getConn()
) {

consultation =
new ConsultationDao(conn)
.findByToken(token);

}

}


String role =
(String)
session.getAttribute(
"AUTH_ROLE"
);


Integer authId =
(Integer)
session.getAttribute(
"AUTH_ID"
);


boolean allowed =
consultation != null

&& (

(
"PATIENT".equals(role)
&& authId != null
&& authId.intValue()
== consultation.userId
)

||

(
"DOCTOR".equals(role)
&& authId != null
&& authId.intValue()
== consultation.doctorId
)

);


if (!allowed) {

response.sendError(
403,
"You are not authorized "
+ "for this consultation."
);

return;

}

%>


<!DOCTYPE html>

<html>

<head>

<title>
Protected Consultation Room
</title>

<%@include file="../component/allcss.jsp"%>

</head>


<body>


<div class="room-box">

<span class="eyebrow">

PROTECTED CONSULTATION ROOM

</span>


<h2 class="mt-2">

<%=consultation.patientName%>

&amp;

Dr.
<%=consultation.doctorName%>

</h2>


<p class="text-muted-custom">

Mode:
<%=consultation.mode%>

<br>

Scheduled:
<%=consultation.requestedDate%>

</p>


<div class="alert alert-clinic">

Your consultation room
has been created successfully.

This portfolio version
generates and protects
a unique consultation URL.

</div>


<p>
<strong>
Secure Room Token
</strong>
</p>


<div class="room-token">

<%=consultation.meetingToken%>

</div>


<a
class="btn btn-brand mt-4"

href="../<%=

"PATIENT".equals(role)

? "patient/consultations.jsp"

: "doctor/consultations.jsp"

%>">

Return to Consultation

</a>

</div>

</body>

</html>