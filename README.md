Workshop 1 – Java Monthly Planner App

This project is a simple Java Swing application that generates a monthly planner UI, loads events from a CSV file, and displays them in a calendar grid.
It demonstrates fundamental skills in Java programming, data handling, GUI development, and exception management.

 Features

Monthly Calendar UI
Generates a full calendar grid for a selected month using GregorianCalendar.

Event Loading from CSV
Reads events from a CSV file via Scanner and SimpleDateFormat, mapping dates to event descriptions.

Exception-Safe File Handling
Implements proper try/catch structures to handle I/O and parsing errors cleanly.

Dynamic Event Display
Each event is added under its corresponding date cell inside the planner.

Organized Package Structure

gui for Swing components

datastore for CSV access & parsing

workshop1 for the main entry point

 How to Run

Clone the Repository

git clone https://github.com/<your-username>/<your-workshop-repo>.git


Open in IntelliJ IDEA

Go to File → Open…

Select the project directory

Locate the Main Class
The entry point is:

workshop1.Main


Run the Application

Right-click Main.java → Run 'Main'

The monthly planner window will open

Events from your CSV file will load automatically into the calendar

 CSV Format Example

Your events should be stored in a file such as:

8-23-2022,CSC 360 - Lecture 1
8-25-2022,CSC 360 - Lecture 2
8-30-2022,CSC 360 - Lecture 3


The format must be:

M-d-yyyy,Event Description

 Why This Project Matters



1. Real GUI Development

Uses Java Swing components, layouts, and event handling — core concepts behind desktop app interfaces.

2. Clean Data Handling

Shows how to read, parse, and validate data from external files, preparing you for larger data-driven projects.

3. Robust Exception Management

Teaches the importance of catching and handling errors safely when dealing with user input and file I/O.

4. Practical Use of Java Dates

Working with Calendar, GregorianCalendar, and SimpleDateFormat develops real-world date/time handling experience.

5. Modular Project Structure

Separating GUI and datastore logic reinforces clean architecture practices commonly required in industry.
