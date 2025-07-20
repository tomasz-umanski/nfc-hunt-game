# NFC Hunt Game

## Overview

The project aims to create a web app where users can discover location connected to octet and participate in NFC hunt game. 

## Table of Contents

1. [Features](#features)
2. [Technology Stack](#technology-stack)
3. [Database Design and Structure](#database-design-and-structure)
4. [Installation](#installation)
5. [Usage](#usage)

## Features

1. **User Registration and Authentication**
    - Users can create accounts and log in securely.
    - Authentication mechanisms ensure user data security.
    - Authentication is token-based, utilizing access tokens and refresh tokens.
    - Users can log out, revoking its tokens.
2. **User profile**
    - Users can see their unlocked locations with details.  
    - Users have the option to delete their account if needed.
3. **NFC tags**
    - Users can scan NFC tags which will unlock locations in their profile.
    - Users must share their location in order to confirm their location.
    - Each scan registers timestamp which will allow to confirm the honesty of the participants
4. **Responsive Design:**
    - Application is created with mobile first approach, making it easy to navigate on various devices.

## Technology Stack

- **Frontend:** React, TypeScript, Vite, Tailwind
- **Backend:** Java, Maven, Spring Boot
- **Database:** PostgreSQL
- **Deployment:** Docker

## Database Design and Structure

1. **Entity-Relationship Diagram (ERD):**
    - The `data-structure.png` file in the database directory provides a visual representation of the database schema.
    - [View ERD](./database/data-structure.png)

2. **Database Schema:**
    - The database schema is initialized automatically at the start of the application using Hibernate, ensuring seamless table creation and management.
    - By default, some basic data is preloaded into the database to support the application's initial functionality.

## Installation
Follow these steps to get the project up and running:
1. **Clone the Repository**
2. **Docker Setup:**
    - Ensure Docker and Docker Compose are installed on your system. In the project directory in the docker folder, you'll find Docker configuration files.
3. **Database Setup:**
    - Detailed instruction of how to set up database is provided in [View README](./database/README.md)
4. **Backend Setup:**
    - Detailed instruction of how to set up backend is provided in [View README](./backend/README.md)
5. **Frontend Setup:**
    - Detailed instruction of how to set up backend is provided in [View README](./frontend/README.md)
6. **Access the Application:**
    - After all components are up and running, you can access the application through your web browser.
