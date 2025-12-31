# Database Schema

## Overview

The application uses **SQLite** database for data persistence. The database is automatically created on first run.

## Database File

- **Location**: `student_app.db` (project root)
- **Type**: SQLite 3
- **Connection**: JDBC (sqlite-jdbc-3.45.1.0.jar)

## Tables

### 1. students

Stores student information.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| student_id | TEXT | PRIMARY KEY | Unique student identifier |
| name | TEXT | NOT NULL | Student's full name |
| age | INTEGER | NOT NULL | Student's age (1-150) |
| course | TEXT | NOT NULL | Course name |
| email | TEXT | NULL | Student's email address |

**Indexes**: None (SQLite auto-indexes PRIMARY KEY)

**Sample Data**:
```sql
INSERT INTO students VALUES ('S001', 'John Doe', 20, 'Computer Science', 'john@example.com');
```

### 2. attendance

Stores attendance records.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | INTEGER | PRIMARY KEY AUTOINCREMENT | Unique record ID |
| student_id | TEXT | NOT NULL, FOREIGN KEY | References students(student_id) |
| date | TEXT | NOT NULL | Attendance date (YYYY-MM-DD) |
| status | TEXT | NOT NULL | PRESENT or ABSENT |

**Foreign Keys**:
- `student_id` → `students(student_id)`

**Indexes**: None

**Sample Data**:
```sql
INSERT INTO attendance (student_id, date, status) 
VALUES ('S001', '2024-01-15', 'PRESENT');
```

### 3. payments

Stores payment records.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | INTEGER | PRIMARY KEY AUTOINCREMENT | Unique payment ID |
| student_id | TEXT | NOT NULL, FOREIGN KEY | References students(student_id) |
| amount | REAL | NOT NULL | Payment amount (> 0) |
| date | TEXT | NOT NULL | Payment date (YYYY-MM-DD) |
| description | TEXT | NULL | Payment description |

**Foreign Keys**:
- `student_id` → `students(student_id)`

**Indexes**: None

**Sample Data**:
```sql
INSERT INTO payments (student_id, amount, date, description) 
VALUES ('S001', 1000.00, '2024-01-10', 'Tuition Fee');
```

## Relationships

```
students (1) ────< (many) attendance
students (1) ────< (many) payments
```

- One student can have many attendance records
- One student can have many payment records
- Cascade delete: Not implemented (manual cleanup required)

## Database Initialization

The database is initialized in `DatabaseConnection.initializeTables()`:

```java
CREATE TABLE IF NOT EXISTS students (...)
CREATE TABLE IF NOT EXISTS attendance (...)
CREATE TABLE IF NOT EXISTS payments (...)
```

## Data Types

- **TEXT**: Used for strings (student_id, name, course, email, date, status, description)
- **INTEGER**: Used for numeric IDs and age
- **REAL**: Used for decimal amounts

## Constraints

1. **Primary Keys**: All tables have primary keys
2. **Foreign Keys**: Attendance and payments reference students
3. **NOT NULL**: Required fields are enforced
4. **AUTOINCREMENT**: Attendance and payment IDs auto-increment

## Query Examples

### Get all students
```sql
SELECT * FROM students;
```

### Get student with attendance
```sql
SELECT s.*, a.date, a.status 
FROM students s 
LEFT JOIN attendance a ON s.student_id = a.student_id;
```

### Calculate total payments
```sql
SELECT student_id, SUM(amount) as total_paid 
FROM payments 
GROUP BY student_id;
```

### Calculate attendance rate
```sql
SELECT 
    student_id,
    COUNT(*) as total,
    SUM(CASE WHEN status = 'PRESENT' THEN 1 ELSE 0 END) as present,
    (SUM(CASE WHEN status = 'PRESENT' THEN 1 ELSE 0 END) * 100.0 / COUNT(*)) as rate
FROM attendance
GROUP BY student_id;
```

## Backup and Restore

### Backup
```bash
cp student_app.db student_app_backup.db
```

### Restore
```bash
cp student_app_backup.db student_app.db
```

## Maintenance

- Database file grows with data
- No automatic cleanup of old records
- Manual backup recommended before major operations
- Foreign key constraints ensure data integrity

