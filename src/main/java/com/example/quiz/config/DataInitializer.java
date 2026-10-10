package com.example.quiz.config;

import com.example.quiz.model.*;
import com.example.quiz.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner init(UserRepository users,
                          QuizRepository quizzes,
                          QuestionRepository questions,
                          CategoryRepository categories,
                          QuizAttemptRepository attempts,
                          AttemptAnswerRepository attemptAnswers,
                          PasswordEncoder passwordEncoder) {
        return args -> {
            // Seed Categories
            seedCategory(categories, "Java", "Core Java, OOP principles, and JVM fundamentals", "code");
            seedCategory(categories, "Python", "Python language fundamentals, data structures, and scripting", "terminal");
            seedCategory(categories, "Web Development", "HTML, CSS, JavaScript, React, and frontend development", "globe");
            seedCategory(categories, "Database Management", "SQL queries, relational database design, and indexing", "database");
            seedCategory(categories, "C & C++", "Systems programming, pointers, memory management, and OOP", "cpu");
            seedCategory(categories, "Data Science", "Statistics, data analysis, and machine learning essentials", "book");
            seedCategory(categories, "General Knowledge", "Computer science fundamentals and general tech trivia", "help-circle");

            // 1. Admin account initialization / migration
            Optional<User> adminOpt = users.findByEmail("admin@quiz.com");
            if (adminOpt.isEmpty()) {
                users.save(new User("Administrator", "admin@quiz.com", passwordEncoder.encode("admin123"), "ADMIN"));
            } else {
                User admin = adminOpt.get();
                if (!admin.getPassword().startsWith("$2a$") && !admin.getPassword().startsWith("$2b$")) {
                    admin.setPassword(passwordEncoder.encode("admin123"));
                    users.save(admin);
                }
            }

            // 2. Demo student initialization / migration
            Optional<User> studentOpt = users.findByEmail("student@quiz.com");
            User student;
            if (studentOpt.isEmpty()) {
                student = users.save(new User("Demo Student", "student@quiz.com", passwordEncoder.encode("student123"), "STUDENT"));
            } else {
                student = studentOpt.get();
                if (!student.getPassword().startsWith("$2a$") && !student.getPassword().startsWith("$2b$")) {
                    student.setPassword(passwordEncoder.encode("student123"));
                    student = users.save(student);
                }
            }

            // 3. Additional demo students for vibrant leaderboard and analytics
            User student2 = seedStudent(users, passwordEncoder, "Aarav Sharma", "aarav@quiz.com", "student123");
            User student3 = seedStudent(users, passwordEncoder, "Priya Patel", "priya@quiz.com", "student123");

            // Seed Quizzes and MCQs across languages & categories
            seed(quizzes, questions, "Java Basics", "Core Java multiple-choice questions covering OOP, keywords, and collections.", "Java", "Easy", 10,
                    new String[][]{
                            {"Which keyword is used to inherit a class in Java?", "extends", "implements", "inherits", "super", "A"},
                            {"Which method is the entry point of a Java application?", "start()", "main()", "run()", "init()", "B"},
                            {"Which collection does not allow duplicate elements?", "List", "Queue", "Set", "Map", "C"},
                            {"Which keyword creates an object in Java?", "class", "new", "this", "object", "B"},
                            {"Which data type stores true or false?", "int", "String", "boolean", "double", "C"}
                    });

            seed(quizzes, questions, "Python Basics", "Python programming fundamentals, data structures, and functions.", "Python", "Easy", 10,
                    new String[][]{
                            {"Which symbol starts a single-line Python comment?", "//", "#", "/*", "--", "B"},
                            {"Which keyword defines a function in Python?", "func", "function", "def", "define", "C"},
                            {"Which data type stores an ordered mutable collection?", "tuple", "set", "list", "dict", "C"},
                            {"Which function displays output to the screen?", "echo()", "print()", "display()", "write()", "B"},
                            {"Which file extension is commonly used for Python files?", ".java", ".py", ".pt", ".python", "B"}
                    });

            seed(quizzes, questions, "Web Development Essentials", "HTML5, CSS3, JavaScript modern web development fundamentals.", "Web Development", "Medium", 12,
                    new String[][]{
                            {"Which HTML tag is used to define an internal style sheet?", "<script>", "<style>", "<css>", "<link>", "B"},
                            {"Which CSS property controls the text size?", "font-style", "text-size", "font-size", "text-style", "C"},
                            {"Which JavaScript method converts a JSON string to an object?", "JSON.stringify()", "JSON.parse()", "JSON.toObject()", "JSON.convert()", "B"},
                            {"What does DOM stand for?", "Document Object Model", "Data Oriented Module", "Digital Ordinance Method", "Desktop Object Management", "A"},
                            {"Which HTTP method is typically used to request data from a server?", "POST", "GET", "PUT", "DELETE", "B"}
                    });

            seed(quizzes, questions, "SQL & Database Management", "SQL queries, relational database normalization, and indexes.", "Database Management", "Medium", 15,
                    new String[][]{
                            {"Which SQL statement is used to retrieve data from a database?", "GET", "SELECT", "FETCH", "EXTRACT", "B"},
                            {"Which clause is used to filter records in a SELECT query?", "WHERE", "HAVING", "FILTER", "LIMIT", "A"},
                            {"Which constraint uniquely identifies each record in a database table?", "UNIQUE", "FOREIGN KEY", "PRIMARY KEY", "CHECK", "C"},
                            {"Which SQL command removes all records from a table without logging each row deletion?", "DELETE", "TRUNCATE", "DROP", "REMOVE", "B"},
                            {"Which JOIN returns all rows when there is a match in ONE of the tables?", "INNER JOIN", "FULL OUTER JOIN", "CROSS JOIN", "LEFT JOIN", "B"}
                    });

            seed(quizzes, questions, "C & C++ Programming", "Systems programming, memory management, pointers, and OOP in C++.", "C & C++", "Medium", 12,
                    new String[][]{
                            {"Which header file is required in C for standard I/O like printf()?", "stdlib.h", "string.h", "stdio.h", "conio.h", "C"},
                            {"Which operator returns the memory address of a variable?", "*", "&", "->", "%", "B"},
                            {"Which feature in C++ allows multiple functions with the same name but different parameters?", "Inheritance", "Polymorphism / Overloading", "Encapsulation", "Abstraction", "B"},
                            {"Which keyword dynamically deallocates memory in C++?", "free", "delete", "clear", "dispose", "B"},
                            {"What is the size of an int data type typically in 32-bit/64-bit systems?", "1 byte", "2 bytes", "4 bytes", "8 bytes", "C"}
                    });

            seed(quizzes, questions, "Data Science & Machine Learning", "Basics of data analysis, statistics, and machine learning concepts.", "Data Science", "Hard", 15,
                    new String[][]{
                            {"Which Python library is primarily used for tabular data manipulation?", "NumPy", "Pandas", "Matplotlib", "Scikit-Learn", "B"},
                            {"What type of learning involves labeled training data?", "Unsupervised Learning", "Reinforcement Learning", "Supervised Learning", "Semi-structured Learning", "C"},
                            {"Which metric is commonly used to evaluate classification performance?", "Mean Squared Error", "F1 Score", "R-squared", "Mean Absolute Error", "B"},
                            {"Which algorithm is widely used for clustering data into k groups?", "Linear Regression", "K-Means", "Random Forest", "Logistic Regression", "B"},
                            {"What technique prevents overfitting by adding a penalty term?", "Normalization", "Regularization", "Quantization", "Imputation", "B"}
                    });

            // Ensure all existing quizzes have published = true
            quizzes.findAll().forEach(q -> {
                if (q.getPublished() == null) {
                    q.setPublished(true);
                    quizzes.save(q);
                }
            });

            // Seed demo quiz attempts for students if attempts table is empty
            if (attempts.count() == 0) {
                Quiz javaQuiz = quizzes.findAll().stream().filter(q -> "Java Basics".equalsIgnoreCase(q.getTitle())).findFirst().orElse(null);
                Quiz pythonQuiz = quizzes.findAll().stream().filter(q -> "Python Basics".equalsIgnoreCase(q.getTitle())).findFirst().orElse(null);

                if (javaQuiz != null && student != null) {
                    createSampleAttempt(attempts, attemptAnswers, student, javaQuiz, 4, 5, 80.0);
                }
                if (pythonQuiz != null && student != null) {
                    createSampleAttempt(attempts, attemptAnswers, student, pythonQuiz, 5, 5, 100.0);
                }
                if (javaQuiz != null && student2 != null) {
                    createSampleAttempt(attempts, attemptAnswers, student2, javaQuiz, 5, 5, 100.0);
                }
                if (pythonQuiz != null && student3 != null) {
                    createSampleAttempt(attempts, attemptAnswers, student3, pythonQuiz, 3, 5, 60.0);
                }
            }
        };
    }

    private void seedCategory(CategoryRepository repo, String name, String desc, String icon) {
        if (repo.findByNameIgnoreCase(name).isEmpty()) {
            repo.save(new Category(name, desc, icon));
        }
    }

    private User seedStudent(UserRepository repo, PasswordEncoder encoder, String name, String email, String pass) {
        Optional<User> found = repo.findByEmail(email);
        if (found.isPresent()) return found.get();
        return repo.save(new User(name, email, encoder.encode(pass), "STUDENT"));
    }

    private void seed(QuizRepository quizzes, QuestionRepository questions,
                      String title, String description, String category, String difficulty,
                      Integer duration, String[][] rows) {
        Quiz quiz = quizzes.findAll().stream()
                .filter(q -> title.equalsIgnoreCase(q.getTitle()))
                .findFirst()
                .orElseGet(() -> {
                    Quiz nq = new Quiz(title, description, category, difficulty, duration);
                    nq.setPublished(true);
                    return quizzes.save(nq);
                });

        if (quiz.getPublished() == null) {
            quiz.setPublished(true);
            quizzes.save(quiz);
        }

        if (questions.findByQuizId(quiz.getId()).isEmpty()) {
            for (String[] r : rows) {
                questions.save(make(quiz, r[0], r[1], r[2], r[3], r[4], r[5]));
            }
        }
    }

    private Question make(Quiz q, String text, String a, String b, String c, String d, String correct) {
        Question x = new Question();
        x.setQuiz(q);
        x.setQuestion(text);
        x.setOptionA(a);
        x.setOptionB(b);
        x.setOptionC(c);
        x.setOptionD(d);
        x.setCorrectAnswer(correct);
        x.setMarks(1);
        return x;
    }

    private void createSampleAttempt(QuizAttemptRepository attempts, AttemptAnswerRepository attemptAnswers,
                                     User user, Quiz quiz, int score, int total, double percentage) {
        QuizAttempt attempt = new QuizAttempt(user, quiz);
        attempt.setScore(score);
        attempt.setTotalMarks(total);
        attempt.setPercentage(percentage);
        attempt.setStatus("COMPLETED");
        attempt.setStartedAt(LocalDateTime.now().minusDays(1));
        attempt.setSubmittedAt(LocalDateTime.now().minusDays(1).plusMinutes(6));
        attempts.save(attempt);
    }
}
