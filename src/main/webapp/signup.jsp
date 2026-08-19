<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.util.CsrfUtil" %>

<!DOCTYPE html>
<html>

<head>
    <title>Create Patient Account</title>
    <%@include file="component/allcss.jsp"%>
</head>

<body>

<%@include file="component/navbar.jsp"%>

<div class="container py-5">

    <div class="row justify-content-center">

        <div class="col-md-5">

            <div class="form-card">

                <span class="eyebrow">
                    PATIENT REGISTRATION
                </span>

                <h3 class="mt-2 mb-4">
                    Create Patient Account
                </h3>

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
                      action="user-register">

                    <input type="hidden"
                           name="_csrf"
                           value="<%=CsrfUtil.token(session)%>">

                    <div class="mb-3">

                        <label class="form-label">
                            Full Name
                        </label>

                        <input class="form-control"
                               type="text"
                               name="fullName"
                               maxlength="100"
                               required>

                    </div>

                    <div class="mb-3">

                        <label class="form-label">
                            Email Address
                        </label>

                        <input class="form-control"
                               type="email"
                               name="email"
                               maxlength="120"
                               required>

                    </div>

                    <div class="mb-3">

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

                    <div class="mb-3">

                        <label class="form-label">
                            Password
                        </label>

                        <input class="form-control"
                               type="password"
                               name="password"
                               minlength="8"
                               required>

                        <small class="text-muted">
                            Minimum 8 characters with uppercase,
                            lowercase and a number.
                        </small>

                    </div>

                    <button type="submit"
                            class="btn btn-brand w-100">

                        Create Patient Account

                    </button>

                </form>

                <div class="text-center mt-3">

                    Already registered?

                    <a href="user_login.jsp">
                        Patient Login
                    </a>

                </div>

            </div>

        </div>

    </div>

</div>

</body>
</html>