<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.util.CsrfUtil" %>

<!DOCTYPE html>
<html>

<head>
    <title>Patient Login | Digital Care</title>
    <%@include file="component/allcss.jsp"%>
</head>

<body>

<%@include file="component/navbar.jsp"%>

<div class="container py-5">

    <div class="row justify-content-center">

        <div class="col-md-5">

            <div class="form-card">

                <span class="eyebrow">
                    PATIENT PORTAL
                </span>

                <h3 class="mt-2 mb-2">
                    Patient Login
                </h3>

                <p class="text-muted-custom mb-4">
                    Sign in to manage appointments
                    and online consultations.
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

                <form method="post"
                      action="user-login">

                    <input type="hidden"
                           name="_csrf"
                           value="<%=CsrfUtil.token(session)%>">

                    <div class="mb-3">

                        <label class="form-label">
                            Email Address
                        </label>

                        <input class="form-control"
                               type="email"
                               name="email"
                               maxlength="120"
                               autocomplete="email"
                               required>

                    </div>

                    <div class="mb-3">

                        <label class="form-label">
                            Password
                        </label>

                        <input class="form-control"
                               type="password"
                               name="password"
                               autocomplete="current-password"
                               required>

                    </div>

                    <button type="submit"
                            class="btn btn-brand w-100">

                        Patient Login

                    </button>

                </form>

                <div class="text-center mt-3">

                    New patient?

                    <a href="signup.jsp">
                        Create an account
                    </a>

                </div>

            </div>

        </div>

    </div>

</div>

</body>
</html>