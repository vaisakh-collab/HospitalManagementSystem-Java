HOSPITAL MANAGEMENT SYSTEM - RUN INSTRUCTIONS

1. Install a JDK (Java 17 or newer recommended).
2. Open this folder in VS Code.
3. Open Terminal > New Terminal in VS Code.
4. Compile the Java source files:
       javac *.java
5. Run the application:
       java Main

The Swing dashboard opens without requiring a database. The module buttons are currently
UI placeholders. To use DAO/database operations, configure MySQL, run schema.sql, add the
MySQL Connector/J JAR to the project classpath, and create config.properties in this folder:

DB_USER=your_mysql_username
DB_PASSWORD=your_mysql_password

Do not commit config.properties or share your database password.
