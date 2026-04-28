# Educational Resources Recommender System

**Developed by:** ROTARU Diana Maria & GHERGHEL Ana-Maria  
**Course:** Methods and Techniques of Software Development (MTDL)  

---

## Project Description
In the modern educational context, students face information overload and struggle to find relevant, verified study materials. Meanwhile, educational experts lack a centralized platform to publish and manage their resources for a well-defined target audience. 

The **Educational Resources Recommender System** solves this problem by providing a desktop application that directly connects students' needs and interests with expert-validated materials under the careful supervision of an administrator. The application ensures a structured, personalized, and secure learning environment.

---

## Roles and Actors
The system implements a Role-Based Access Control (RBAC) mechanism featuring 3 types of users:

1. **Student:** The standard user. Can create a profile, set educational interests, search for materials, and receive personalized recommendations.
2. **Educational Expert:** The content creator. Initially registers as a student and receives "Expert" rights only after Admin validation. Can add (CRUD), edit, and publish educational resources (title, image, and link are mandatory).
3. **Administrator:** The platform moderator. Manages "Expert" upgrade requests and can delete accounts that violate platform rules.

---

## Core Features
* **Identity & Security:** Account creation, Login, Logout, and a secure password recovery flow.
* **Profile Management:** Upload personal data, profile picture, and select educational interests.
* **Resource Management (CRUD):** Experts can add resources and toggle their status between *Draft/Unpublished* and *Published*.
* **Search & Recommendation Engine:** Students can search for resources using specific filters. The system integrates a recommendation algorithm that returns resources whose categories match at least one of the student's saved interests.

---

## Architecture & Technologies
The application uses a **Layered Architecture**, strictly following the *Separation of Concerns* principle:
* **Presentation Layer (GUI):** Built using **JavaFX**.
* **Business Logic Layer:** Contains the core services, Business Rules validations, and the recommendation algorithm.
* **Data Access Layer (DAO Layer):** Manages SQL queries utilizing the *DAO Design Pattern* to decouple the database from the application logic. The connection is managed via the *Singleton pattern*.
* **Database Layer:** A relational database (**MySQL**) storing users, profiles, resources, and categories (tags).

---

## Database Structure (Main Entities)
* `USERS` & `PROFILES`
* `INTERESTS` & `STUDENT_INTERESTS` (Many-to-Many)
* `RESOURCES` & `CATEGORIES` (Many-to-Many)
* `EXPERT_REQUESTS`

---

## Documentation
The project was planned and modeled using the following tools and diagrams:
* **GANTT Chart & RASCI Matrix** for time and responsibility management.
* **UML Diagrams:** Use Case Diagrams, Activity Diagrams, Sequence Diagrams, Package Diagram, and Class Diagram.
