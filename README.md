# FSC Student Enrollment Manager

## Overview
The **FSC Student Enrollment Manager** is a professional, polished JavaFX application designed for **Farmingdale State College (FSC)**.  
It allows administrative users to efficiently manage student enrollment records with a modern interface, functional features, and enhanced user experience.

---

## Technologies Used
- Java 21
- JavaFX 20
- Maven
- SceneBuilder (FXML)
- Git

---

## Required Features

### ✅ Core Functionalities
- **Login & Signup System**  
  - Secure login and signup pages.
  - Credentials saved using Java Preferences for session persistence.
- **Student Management**
  - Add, edit, delete student records.
  - Validate fields (First Name, Last Name, Email, Department, Password) with **advanced Regex patterns**.
  - **Major** selection implemented via **Enum** dropdown (CSC, CPIS, BUSINESS, ENGLISH, MATH, CHEMISTRY, BIOLOGY).
- **User Interface**
  - Add/Edit/Delete buttons disabled unless appropriate actions can be performed.
  - Menu items gray out when no item is selected.
  - "Add" button only enabled when valid form data is entered.
  - Dynamic **Status Bar** centered at the bottom providing user feedback (e.g., "Form Cleared!", "Record Added!").
- **Data Import/Export**
  - **Import** student data from CSV file.
  - **Export** current table data to CSV file.
- **Thread Safety**
  - `UserSession` class redesigned using **double-checked locking** for safe concurrent access.

---

## Extra Credit / Enhancements

### 🌟 Bonus Features Added
- **Confirmation Dialog Before Deleting**  
  - Prompt the user before deleting a record for safety.
- **Sortable TableView Columns**  
  - Allow sorting by clicking column headers (e.g., sort by First Name, Major, Email).
- **Green-Styled Custom Scrollbar**  
  - Modernized the scrollbars to match FSC's branding.
- **Export Major Report to "PDF" (Text File)**  
  - Generate a report summarizing student counts per major (CSC, CPIS, ENGLISH, etc.) and save it to a file.
  - [📄 View Major Report (PDF)](resources/reports/majorReport.pdf)
- **New Menu Tab: Import/Export**
  - Moved "Import CSV", "Export CSV", and "Export PDF Report" into a clean "Import/Export" menu.
- **Dark Mode / Light Mode Toggle**
  - Fully separate darkTheme.css and lightTheme.css styled professionally.

---

## Interface Preview

### 🖼 Login Screen
![Login Screen](images/login.png)

### 🖼 Main Interface
![Main Application Interface](images/interface.png)

---

## How to Run

1. Clone the repository:
   ```bash
   git clone <your-repo-link>
   ```

2. Open the project in IntelliJ IDEA (or your preferred Java IDE).

3. Ensure JDK 21+ and JavaFX libraries are configured properly.

4. Build and Run the project.

---

## Project Structure

```
src/
 ├── model/            # Person.java, etc.
 ├── service/          # UserSession.java, MyLogger.java
 ├── viewmodel/        # Controllers (Login, Signup, SplashScreen, Main)
 └── resources/
      ├── css/         # interface.css, login.css, darkTheme.css, etc.
      ├── images/      # fsclogo.png, login.png, interface.png
      └── view/        # login.fxml, signUp.fxml, about.fxml, etc.
```

---

## Author
**Justin Derenthal**  
[LinkedIn Profile](https://www.linkedin.com/in/jderenthalcs/)

---
