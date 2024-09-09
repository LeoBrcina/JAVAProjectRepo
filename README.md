Java Movie Management Application

Java Movie Management Application is a project built for managing movie-related entities such as movies, actors, directors, and genres. The application demonstrates advanced object-oriented principles in Java, interacting with an SQL database, and handling data in a structured and user-friendly manner.

Table of Contents

Project Description
Features
Installation
Usage
Technologies Used
Project Structure
License

Project Description

The application provides a graphical user interface (GUI) that allows users to manage various entities like Movies, Actors, Directors, and Genres. It uses a Microsoft SQL Server database to store information and implements CRUD (Create, Read, Update, Delete) operations via a repository pattern. The project follows best object-oriented practices and employs advanced Java features such as Swing for GUI, RSS parsing for importing data, and handling roles and permissions (Administrator/User).

Key features include:

Role-based access control: Administrator and User roles.
RSS Feed Parsing: Automatically updates movie data from external RSS feeds.
Swing-based GUI: Implements the MVC (Model-View-Controller) architecture.
Database interaction: All data is managed through SQL Server, with initialization and deletion scripts.
CRUD operations: Users can perform full CRUD operations on movies, actors, directors, etc.
File management: Handles local storage of images and related resources, with consistent management across entities.

Features

Login and Registration: Users can register or log in. Administrators can initialize the database and upload new data via RSS feeds.
Role-Based System: Admins have the ability to reset the database, upload data, and manage users, while regular users can view and modify data.
CRUD Operations: Full CRUD capabilities for entities such as Movies, Actors, Directors, and Genres.
RSS Parsing: Admins can load movie data from external RSS feeds into the database. Images are saved locally with their relative paths stored in the database.
File Management: Ensures consistent handling of images and resources related to movies, actors, and directors.
Drag and Drop: Drag and drop functionality for linking actors and directors to movies.
Advanced Swing GUI: Uses JTabbedPane for organizing different sections of the application, JTable for displaying data, and AbstractTableModel for efficiently handling data.
XML Serialization: Implements JAXB to serialize and deserialize entity data as XML.

Installation

Prerequisites:

Java Development Kit (JDK) installed (version 8 or higher).
Microsoft SQL Server installed and running.
Maven installed for managing project dependencies.
Setup Instructions:
Clone the repository:

bash
git clone https://github.com/yourusername/JavaMovieManager.git

Open the project in your favorite Java IDE (e.g., IntelliJ IDEA or Eclipse).

Import Maven dependencies (if not automatically imported):

bash
mvn install

Set up the SQL database:

Execute the provided DDL script to create the necessary tables.
Execute the initialization script to set up the admin user and other required data.

Configure the database connection:

Update the database connection settings in the configuration file (application.properties or equivalent) to point to your local SQL Server instance.

Run the application.

Usage

Login as Administrator:

After launching the application, log in using the admin credentials initialized from the database script.

As an administrator, you can:

Upload new data via RSS feeds.
Reset the database and manage users.

Manage Movie Data:

Perform CRUD operations on movies, actors, directors, and genres.
Use drag and drop to link actors or directors to movies.
Upload and manage images for the different entities.

Regular User:

Log in as a regular user to view and modify movies, actors, directors, and genres.
Users can also modify their profile or settings.

Technologies Used

Java: Core language for the project.
Swing: For GUI components and MVC architecture.
Maven: Dependency management.
Microsoft SQL Server: For database management.
RSS Feeds: For pulling in movie data from external sources.
JAXB: For XML serialization and deserialization.
JDBC (Java Database Connectivity): For interacting with the SQL Server database.

Project Structure

bash
JavaMovieManager/
│
├── /src                    # Source code files
│   ├── /main                # Main application code
│   └── /test                # Test cases
├── /resources               # Resource files like images, properties, and scripts
├── /db                      # Database setup and initialization scripts
└── pom.xml                  # Maven configuration file
License
This project is developed for educational purposes as part of the Java programming course. No official license applies.
