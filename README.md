# 🎯 QuizMaster - Online Quiz Management System

A full-stack online quiz management and assessment platform built with **Spring Boot 3**, **React**, and **TiDB Cloud (MySQL Serverless)**.

---

## 🌐 Live Demo
👉 **[https://online-quiz-system-backend-1.onrender.com](https://online-quiz-system-backend-1.onrender.com)**

---

## 🔑 Demo Login Credentials

| Role | Email | Password | Permissions |
| :--- | :--- | :--- | :--- |
| **Admin** | `admin@quiz.com` | `admin123` | Create quizzes, manage questions, view student analytics & full attempt records |
| **Student** | `student@quiz.com` | `student123` | Take quizzes, view instant score reports with explanations, check leaderboard |

> 💡 *You can also click **Sign Up** to create a brand new account!*

---

## 🚀 Key Features

- **User Authentication**: Secure JWT-based authentication with BCrypt password hashing.
- **Role-Based Access Control**: Separate, tailored dashboards for Students and Administrators.
- **Multi-Category Quizzes**: Pre-loaded quizzes across Java, Python, Web Development, SQL, C++, and Data Science.
- **Timed Quiz Engine**: Real-time timer countdown, question navigation, and automatic submission.
- **Instant Evaluation & Detailed Review**: Immediate percentage calculation, pass/fail status, and per-question answer reviews with explanations.
- **Live Leaderboard**: Real-time rankings displaying top performers by total score and average accuracy.
- **Admin Management Portal**: Create new quizzes, edit existing categories, manage questions, and track user performance analytics.
- **Cloud Database Integration**: Hosted on TiDB Cloud Serverless MySQL over encrypted TLS.

---

## 🛠️ Tech Stack

- **Backend**: Java 17, Spring Boot 3, Spring Security, Spring Data JPA, Hibernate, JJWT
- **Frontend**: React 18, Vite, React Router 6, Axios, Lucide Icons, CSS3
- **Database**: TiDB Cloud (Serverless MySQL)
- **Deployment**: Render (Docker containerized)

---

## 💻 Local Setup & Development

### Prerequisites
- Java 17+
- Maven 3.8+
- Node.js 18+ (for frontend)
- MySQL 8.0+ or Docker

### Running the Backend
```bash
cd backend
mvn spring-boot:run
```
The backend starts on `http://localhost:8080`.

### Running the Frontend
```bash
cd frontend
npm install
npm run dev
```
The frontend starts on `http://localhost:5173`.
