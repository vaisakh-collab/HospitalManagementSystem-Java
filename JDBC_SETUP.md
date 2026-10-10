# JDBC setup — Hospital Management System (Windows / VS Code)

## 1. Install MySQL Server
Install MySQL Server (MySQL Workbench is optional). Remember the password you set for the `root` account. Make sure the MySQL service is running.

## 2. Create the database and tables
Open MySQL Workbench, connect to your local server, open `schema.sql`, and execute the script.
It creates the `hospital` database and the tables used by the project.

If the database/tables already exist, do not blindly rerun the script: `CREATE TABLE` statements may fail because the tables already exist.

## 3. Add MySQL Connector/J
Download **MySQL Connector/J** from the official MySQL downloads page:
https://dev.mysql.com/downloads/connector/j/

Choose the **Platform Independent** ZIP, extract it, and copy the file named like
`mysql-connector-j-<version>.jar` into this project's `lib` folder.

Expected structure:
```
HospitalManagementSystem-Java/
  lib/
    mysql-connector-j-<version>.jar
  config.properties
  config.properties.example
  DatabaseConnection.java
  schema.sql
  Main.java
```

## 4. Set your database credentials
Copy `config.properties.example` and rename the copy to `config.properties`.
Edit it and set the correct password:
```
DB_URL=jdbc:mysql://localhost:3306/hospital
DB_USER=root
DB_PASSWORD=your_actual_mysql_password
```
Do not include quotation marks around the values. Keep `config.properties` private; it is ignored by Git.

## 5. Compile and run in VS Code PowerShell
Open the terminal in the project folder (the folder containing `Main.java`) and run these commands. Replace the JAR filename with the exact filename you copied into `lib`.

Compile:
```
javac -cp ".;lib/mysql-connector-j-<version>.jar" *.java
```

Test JDBC:
```
java -cp ".;lib/mysql-connector-j-<version>.jar" TestDatabaseConnection
```

Run the Swing application:
```
java -cp ".;lib/mysql-connector-j-<version>.jar" Main
```

The `-cp` option above is for Windows. On macOS/Linux, use `:` instead of `;`.

## Common errors
- `No suitable driver`: Connector/J JAR is missing from the classpath, or the JAR filename in the command is wrong.
- `Communications link failure` / connection refused: MySQL Server is not running, or the host/port is wrong. The default URL uses port 3306.
- `Access denied for user`: check `DB_USER` and `DB_PASSWORD`.
- `Unknown database 'hospital'`: execute `schema.sql` in MySQL Workbench.
- `config.properties` not found: ensure it is in the current terminal folder and was copied from `config.properties.example`.
- SQL table/column errors after connection succeeds: the database schema and the DAO SQL must match. A successful JDBC connection only confirms that the server/database login works.

Never upload `config.properties` or commit real database passwords to GitHub.
