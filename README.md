# BTO Management System

## Project Overview
The Build-To-Order (BTO) Management System is a Java-based application designed to manage the entire BTO public housing application process. It serves as a centralized platform for applicants, HDB officers, and HDB managers to interact with BTO housing projects. The system supports various functionalities such as project management, application processing, flat booking, and inquiry management.

## Key Features

### For All Users
- Secure login via Singpass account (NRIC + password)
- Password change functionality
- Project viewing (filtered by eligibility)
- Filtering projects by location, flat types, etc.

### For Applicants
- View available BTO projects based on eligibility
- Apply for housing (with eligibility checks)
- Track application status
- Book flats (after successful application)
- Request application withdrawal
- Submit and manage inquiries about projects

### For HDB Officers
- Register to handle specific projects
- Check registration status
- View assigned project details
- Process flat bookings for successful applicants
- Update flat availability information
- Generate booking receipts
- Respond to inquiries
- Applicants capabilities (can also apply for housing)

### For HDB Managers
- Create, edit, and delete BTO project listings
- Toggle project visibility
- View all projects (regardless of visibility)
- Filter and view self-created projects
- Manage officer registrations (approve/reject)
- Manage applicant applications (approve/reject)
- Process withdrawal requests
- Generate applicant reports with various filters
- Respond to inquiries for all projects

## System Architecture

### Packages
- `Main` - Core system functionality
- `Main.BTO` - BTO project related classes
- `Main.Enums` - System enumerations
- `Main.Enquiries` - Inquiry management
- `Main.Personnel` - User management
- `Main.Manager_control` - Manager-specific functions
- `Main.interfaces` - System interfaces
- `AppInterface` - User interface implementations
- `data` - CSV data files

### Key Classes
- `BTOManagementSystem` - Main system class
- `User` - Base class for all users
- `Applicant` - Represents housing applicants
- `HDBOfficer` - Represents HDB officers
- `HDBManager` - Represents HDB managers
- `BTOProject` - Represents a BTO housing project
- `BTOApplication` - Represents an application for a BTO flat
- `Flat` - Represents an individual flat unit
- `FlatList` - Manages collections of flats
- `Registration` - Represents officer registration requests
- `ProjectDatabase` - Singleton database for all projects
- `EnquiryList` - Manages project inquiries

### Design Patterns
- **Singleton**: Used for ProjectDatabase
- **Factory Method**: For creating different user types
- **Command Pattern**: For application processing
- **Observer Pattern**: For status updates notifications
- **Interface-based Programming**: For flexible implementation

## Getting Started

### Prerequisites
- Java Development Kit (JDK) 8 or higher
- Command line or your preferred Java IDE

### Installation
1. Clone the repository:
   ```
   git clone <repository-url>
   ```
2. Navigate to the project directory:
   ```
   cd bto-management-system
   ```
3. Compile the project:
   ```
   javac -d bin Main/BTOManagementSystemApp.java
   ```
4. Run the application:
   ```
   java -cp bin Main.BTOManagementSystemApp
   ```

### Sample Data
The system includes sample data for testing:
- `data/ApplicantList.csv` - Sample applicant data
- `data/OfficerList.csv` - Sample officer data
- `data/ManagerList.csv` - Sample manager data
- `data/ProjectList.csv` - Sample project data
- `data/EnquiryList.csv` - Sample inquiry data

Default user credentials can be found in the respective CSV files, with the default password being "password".

## System Flow

### Login Process
1. User enters NRIC and password
2. System validates credentials
3. User is directed to appropriate interface based on role

### Application Process
1. Applicant views eligible projects
2. Applicant submits application for a project
3. HDB Manager reviews and approves/rejects application
4. If approved, applicant receives "Successful" status
5. HDB Officer processes flat booking
6. Receipt is generated for the booking

### Officer Registration Process
1. Officer views available projects
2. Officer registers to handle a project
3. Manager reviews and approves/rejects registration
4. If approved, officer is assigned to the project

## OOP Principles Implemented

The system implements various Object-Oriented Programming principles:

- **Encapsulation**: Private fields with getters/setters
- **Inheritance**: Hierarchical user structure
- **Polymorphism**: Interface implementations
- **Abstraction**: Abstract classes and interfaces

### SOLID Principles:
- **Single Responsibility**: Classes have specific focused responsibilities
- **Open/Closed**: System can be extended without modification
- **Liskov Substitution**: Subtypes can substitute base types
- **Interface Segregation**: Specific interfaces for different roles
- **Dependency Inversion**: Dependencies on abstractions not concretions

## Contributors
- [Khant Naing Lin]
- [Chia Jia Yuun]
- [Ivan Wong Jia Ming]
- [Joey Tang See Ying]
- [Amaravel Babu Arran]

## License
This project is developed for educational purposes as part of SC2002: Object-Oriented Design & Programming course at Nanyang Technological University.
