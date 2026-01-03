# Attendance and Finance Panel Improvements

## Overview

Refactor both AttendancePanel and FinancePanel to follow the same consolidated pattern used in StudentManagementPanel, with real-time search, Observer integration, and consistent Facade usage.

## Phase 1: AttendancePanel Consolidation

### Changes to [AttendancePanel.java](StudentInformationApplication/src/view/AttendancePanel.java)

**Remove:**

- Three separate sub-tabs (Mark, View, Rate)
- Direct AttendanceController usage

**New Layout:**

```
+----------------------------------------------------------+
| Search: [________] [Clear] Date: [____] [Refresh]        |
+----------------------------------------------------------+
| ID | Student ID | Date | Status                           |
|----|------------|------|---------|                        |
| (rows with PRESENT=green, ABSENT=red coloring)           |
+----------------------------------------------------------+
| [Mark Attendance] [Edit] [Delete] | Rate: XX% | Count: N |
+----------------------------------------------------------+
```

**Features:**

- Real-time filtering (by student ID or name)
- Status-based row coloring (PRESENT=green, ABSENT=red)
- Modal dialog for marking attendance
- Observer pattern for auto-refresh
- Quick stats panel (attendance rate, present/absent count)
- Date filter option

### New File: `view/AttendanceFormDialog.java`

Modal dialog for marking attendance:

```java
public class AttendanceFormDialog extends JDialog {
    private JComboBox<String> studentIdCombo; // Dropdown of all students
    private JTextField dateField;
    private JComboBox<String> statusCombo; // PRESENT/ABSENT
}
```

## Phase 2: FinancePanel Consolidation

### Changes to [FinancePanel.java](StudentInformationApplication/src/view/FinancePanel.java)

**Remove:**

- Three separate sub-tabs (Add Payment, View, Balance)
- Direct PaymentController usage

**New Layout:**

```
+----------------------------------------------------------+
| Search: [________] [Clear] Date Range: [__] to [__]      |
+----------------------------------------------------------+
| ID | Student ID | Amount | Date | Description            |
|----|------------|--------|------|------------------------|
| (rows with positive amounts in green)                     |
+----------------------------------------------------------+
| [Add Payment] [Edit] [Delete]    | Total: $XXX.XX        |
+----------------------------------------------------------+
```

**Features:**

- Real-time filtering (by student ID, description)
- Amount coloring
- Modal dialog for adding/editing payments
- Observer pattern for auto-refresh
- Running total display
- Date range filter

### New File: `view/PaymentFormDialog.java`

Modal dialog for adding payments:

```java
public class PaymentFormDialog extends JDialog {
    private JComboBox<String> studentIdCombo;
    private JTextField amountField;
    private JTextField dateField;
    private JTextField descriptionField;
}
```

## Phase 3: Observer Pattern Integration

### Update [StudentDataManager.java](StudentInformationApplication/src/observer/StudentDataManager.java)

Add notification methods for attendance and payment changes:

```java
public void notifyAttendanceChanged() {
    notifyObservers("ATTENDANCE");
}

public void notifyPaymentChanged() {
    notifyObservers("PAYMENT");
}
```

### Update [StudentManagementFacade.java](StudentInformationApplication/src/facade/StudentManagementFacade.java)

Add Observer notifications to attendance and payment operations.

## Phase 4: Consistency Updates

### Use Facade Only

Both panels will use only `StudentManagementFacade` (remove direct controller usage).

### Row Coloring

- Attendance: PRESENT (light green), ABSENT (light red)
- Finance: Payment amounts in green

## Implementation Order

1. Create AttendanceFormDialog.java
2. Refactor AttendancePanel.java (unified view)
3. Create PaymentFormDialog.java
4. Refactor FinancePanel.java (unified view)
5. Update StudentDataManager.java (new notification methods)
6. Update StudentManagementFacade.java (trigger notifications)

## UI Mockups

**Consolidated Attendance Panel:**

```
+--------------------------------------------------------------------+
| Search: [________________] [Clear]  Filter Date: [____]  [Refresh] |
+--------------------------------------------------------------------+
| ID  | Student ID | Student Name | Date       | Status              |
|-----|------------|--------------|------------|---------------------|
| 1   | STU001     | John Smith   | 2024-01-15 | PRESENT (green)     |
| 2   | STU002     | Jane Doe     | 2024-01-15 | ABSENT  (red)       |
+--------------------------------------------------------------------+
| [Mark Attendance]  [Edit Selected]  [Delete]  |  Today: 5P / 2A    |
+--------------------------------------------------------------------+
```

**Consolidated Finance Panel:**

```
+--------------------------------------------------------------------+
| Search: [________________] [Clear]  From: [____] To: [__] [Refresh]|
+--------------------------------------------------------------------+
| ID  | Student ID | Student Name | Amount  | Date       | Desc     |
|-----|------------|--------------|---------|------------|----------|
| 1   | STU001     | John Smith   | $500.00 | 2024-01-15 | Tuition  |
| 2   | STU002     | Jane Doe     | $250.00 | 2024-01-16 | Lab Fee  |
+--------------------------------------------------------------------+
| [Add Payment]  [Edit Selected]  [Delete]        |  Total: $750.00  |
+--------------------------------------------------------------------+
```