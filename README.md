# Hospital Management System

A Java desktop application for managing core hospital workflows,
including patients, doctors, appointments, consultations, prescriptions,
and billing. The project uses **Java Swing** for the graphical user
interface, **JDBC** for database connectivity, and **MySQL** for
persistent storage.

## Features and Scope

The system is designed around the following entities:

-   **Patients** --- patient details and records.
-   **Doctors** --- doctor details and specializations.
-   **Appointments** --- appointment scheduling and status tracking.
-   **Consultations** --- consultation dates, diagnoses, and notes.
-   **Prescriptions** --- medication details, dosage, frequency,
    duration, and instructions.
-   **Billing** --- consultation-related bills and payment status.

The project is under development, so some planned functionality may not
yet be implemented.


## Design Documentation

The following documents explain the project's structure and design.

| Document | Description |
|---|---|
| [CLASS_DESIGN.md](CLASS_DESIGN.md) | The application's classes, attributes, methods, and relationships. |
| [DATABASE_DESIGN.md](DATABASE_DESIGN.md) | The database tables, primary keys, foreign keys, and relationships. |
| [DAO_DETAILS.md](DAO_DETAILS.md) | The Data Access Object (DAO) layer and its database operations. |



## Technology Stack

-   **Java** --- application logic and object-oriented design.
-   **Java Swing** --- desktop graphical user interface.
-   **JDBC** --- communication between Java and MySQL.
-   **MySQL Server** --- persistent data storage.
-   **MySQL Connector/J** --- the JDBC driver.
-   **Visual Studio Code** --- one supported development environment.

This project uses a manually configured JDBC driver and does not use
Maven.

## Requirements

To build and run the project locally, you need:

1.  A Java JDK.
2.  MySQL Server.
3.  MySQL Workbench or another MySQL client (recommended).
4.  MySQL Connector/J.
5.  A Java-capable IDE or editor. The instructions below use Visual
    Studio Code.

## Running the Project

### 1. Get the source code

Clone the repository:

``` bash
git clone https://github.com/vaisakh-collab/HospitalManagementSystem-Java.git
```

Open the resulting project directory in your IDE or editor. Replace
`<repository-url>` with the repository's clone URL.

### 2. Set up the database

Start MySQL Server and open MySQL Workbench or another MySQL client.

Run the SQL statements in [`schema.sql`](schema.sql) to create the
`hospital` database and its tables. Check that the script completes
successfully before running the application.

### 3. Add MySQL Connector/J

Download MySQL Connector/J from the [official MySQL download
page](https://dev.mysql.com/downloads/connector/j/).

Place the Connector/J JAR in the project's `lib/` directory. For
example:

``` text
lib/
└── mysql-connector-j-<version>.jar
```

The JAR is not included in the repository, so it must be downloaded
separately.

Make sure your IDE includes this JAR on the Java classpath. In Visual
Studio Code, check the Java project's referenced libraries and any
classpath settings in `.vscode/settings.json`.

### 4. Configure the database connection

Create a local `config.properties` file at the location expected by
`DatabaseConnection.java`. This file is excluded from version control
because it contains environment-specific credentials.

For example, if the connection class expects the following property
names:

``` properties
db.url=jdbc:mysql://localhost:3306/hospital
db.username=root
db.password=YOUR_MYSQL_PASSWORD
```

Replace the example password with the password for your local MySQL
account. The property names and file location must match what
`DatabaseConnection.java` actually reads.

Do not publish real database credentials.

### 5. Run the application

Open the Java entry-point file containing the `main()` method and run it
from your IDE. In Visual Studio Code, use the Java **Run** action.

Before testing database operations, confirm that:

-   MySQL Server is running.
-   The `hospital` database and its tables exist.
-   `config.properties` is in the expected location and contains valid
    credentials.
-   MySQL Connector/J is on the Java classpath.

## Application Architecture

The intended separation of responsibilities is:

``` text
Swing GUI
   |
   v
Application / Business Logic
   |
   v
DAO Classes
   |
   v
DatabaseConnection
   |
   v
JDBC Driver
   |
   v
MySQL Database
```

-   **GUI:** presents the application's screens and handles user
    interaction.
-   **Application / business logic:** coordinates application
    operations.
-   **DAO classes:** perform database operations such as `INSERT`,
    `SELECT`, `UPDATE`, and `DELETE`.
-   **DatabaseConnection:** provides JDBC connections using local
    configuration.
-   **MySQL:** stores application data persistently.

Refer to the design documents above for more detail on the intended
class structure, database schema, and DAO responsibilities.

## Troubleshooting

| Problem | What to check |
|---|---|
| JDBC driver not found | Confirm that the Connector/J JAR is in `lib/` and included on the Java classpath. |
| MySQL access denied | Check the username and password in `config.properties`. |
| Connection refused | Confirm that MySQL Server is running and that the connection URL uses the correct host and port. |
| Unknown database | Run `schema.sql` and verify that the database is named `hospital`. |
| Table does not exist | Check whether all table-creation statements in `schema.sql` executed successfully. |
| Configuration file not found | Check the file location and how `DatabaseConnection.java` loads it. |

