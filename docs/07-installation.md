# Installation Guide

## System Requirements

- **Operating System**: Windows, Linux, or macOS
- **Java Version**: JDK 8 or higher
- **RAM**: Minimum 512MB
- **Disk Space**: 50MB for application and database

## Installation Steps

### Method 1: Using Pre-compiled Files

1. **Download the Project**
   ```bash
   git clone <repository-url>
   cd StudentInformationApplication
   ```

2. **Verify Java Installation**
   ```bash
   java -version
   javac -version
   ```

3. **Check Dependencies**
   Ensure the following JAR files are in the `lib/` folder:
   - sqlite-jdbc-3.45.1.0.jar
   - slf4j-api-2.0.9.jar
   - slf4j-simple-2.0.9.jar

4. **Run the Application**
   
   **Windows:**
   ```bash
   run.bat
   ```
   
   **Linux/macOS:**
   ```bash
   java -cp "out:lib/*" StudentInfoApp
   ```

### Method 2: Compile from Source

1. **Navigate to Project Directory**
   ```bash
   cd StudentInformationApplication/src
   ```

2. **Compile Java Files**
   ```bash
   javac -cp "../lib/*" -d ../out **/*.java
   ```

3. **Run Application**
   ```bash
   cd ..
   java -cp "out:lib/*" StudentInfoApp
   ```

### Method 3: Using IDE (IntelliJ IDEA)

1. **Open Project**
   - File → Open → Select project folder

2. **Configure SDK**
   - File → Project Structure → Project
   - Set Project SDK to JDK 8+

3. **Add Libraries**
   - File → Project Structure → Libraries
   - Add JARs from `lib/` folder

4. **Set Output Directory**
   - File → Project Structure → Modules
   - Set Compile output to `out/`

5. **Run Configuration**
   - Run → Edit Configurations
   - Add Application configuration
   - Main class: `StudentInfoApp`
   - Classpath: Include `lib/*`

## Database Setup

The application automatically creates the SQLite database (`student_app.db`) on first run. No manual setup required.

### Database Location

- **Default**: `student_app.db` in project root
- **Custom**: Modify `DB_URL` in `DatabaseConnection.java`

## Configuration

### Credentials Setup

1. Edit `src/credentials.txt`
2. First line: username
3. Second line: password
4. Save file

Example:
```
admin
password123
```

### Database Configuration

To change database location, edit `dao/DatabaseConnection.java`:

```java
private static final String DB_URL = "jdbc:sqlite:your_database.db";
```

## Docker Installation (Optional)

1. **Build Docker Image**
   ```bash
   docker build -t student-info-app .
   ```

2. **Run Container**
   ```bash
   docker run -it student-info-app
   ```

## Troubleshooting

### Issue: "SQLite JDBC driver not found"

**Solution**: Ensure `sqlite-jdbc-3.45.1.0.jar` is in the `lib/` folder and included in classpath.

### Issue: "Database connection failed"

**Solution**: 
- Check file permissions
- Verify database file location
- Ensure SQLite JDBC driver is loaded

### Issue: "Credentials file not found"

**Solution**: 
- Create `src/credentials.txt`
- Add username and password (one per line)

### Issue: "ClassNotFoundException"

**Solution**: 
- Verify all JARs are in `lib/` folder
- Check classpath includes `lib/*`
- Recompile project

## Verification

After installation, verify:

1. ✅ Application launches without errors
2. ✅ Login screen appears
3. ✅ Can login with credentials
4. ✅ Main window displays correctly
5. ✅ Database file created (`student_app.db`)

## Uninstallation

1. Delete project folder
2. Delete database file (if you want to remove data)
3. No system files are modified

## Support

For issues, check:
- [Developer Guide](05-developer-guide.md)
- [Testing Guide](08-testing.md)
- Project README.md

