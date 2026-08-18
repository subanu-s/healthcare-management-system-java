# Digital Care - Healthcare Management System

Java/JSP/Servlet/JDBC/MySQL portfolio project reconstructed and polished from the original academic Digital Care project.

## Features
- Patient registration/login
- Doctor login
- Admin login and doctor creation
- Appointment booking and doctor status updates
- Online consultation request management
- Doctor meeting link, consultation notes and prescription
- SHA-256 password hashing for this academic project and PreparedStatements

## Online consultation scope
The project manages an online consultation workflow. It does not embed a video engine. A doctor can attach a meeting link (Google Meet/Teams/etc.), then record notes and prescription.

## Run
1. MySQL: execute `database/schema.sql`.
2. Copy `src/main/resources/db.properties.example` to `db.properties` and set your local MySQL password.
3. Run `mvn clean package`.
4. Deploy the WAR to Tomcat 9.
5. Open `http://localhost:8080/healthcare-management-system/`.

Default admin: `admin@digitalcare.com` / `Admin@123`
