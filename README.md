# TeamSync Backend

## Project Overview

TeamSync Backend is the backend service for the **TeamSync** corporate collaboration platform written in Spring Boot. This service handles authentication, authorization, user management, and provides RESTful APIs for interacting with the platform's features.

## Key Features

- **Authentication and Authorization**
    - Single Sign-On (SSO) integration (simulated)
    - Role-Based Access Control (RBAC) to manage different user roles (e.g., employees, team leads, administrators)
- **User Management**
    - CRUD operations for user profiles
    - Skills and expertise management
- **News Feed API**
    - Create, read, update, and delete (CRUD) operations for posts
    - Rich text formatting and file attachments
    - User tagging functionality
- **Comments and Reactions**
    - Add comments to posts
    - React to posts and comments
- **Groups and Channels**
    - Create and manage groups and channels
    - Post updates within specific groups or channels
- **Notifications**
    - Real-time notifications for mentions, comments, and other activities
- **Basic Analytics**
    - Engagement metrics such as most active users and popular posts

## Getting Started

### Prerequisites

- Java 11 or higher
- Maven
- PostgreSQL

### Installation

1. Clone the repository:
    ```bash
    git clone <backend-repo-url>
    ```
2. Navigate to the project directory:
    ```bash
    cd TeamSyncBackend
    ```
3. Build the project:
    ```bash
    mvn clean install
    ```
4. Run the application:
    ```bash
    mvn spring-boot:run
    ```

### Database Setup

This project uses PostgreSQL as its database. You can handle the database setup with the following commands:

- **Create the database**:
    ```bash
    psql -U postgres -d postgres -a -f init_database.sql
    ```
- **Delete the database**:
    ```bash
    psql -U postgres -d postgres -a -f delete_database.sql
    ```

Make sure you have PostgreSQL installed and running, and adjust the connection details in the `application.properties` file if necessary.