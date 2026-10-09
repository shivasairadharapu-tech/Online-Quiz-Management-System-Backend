package com.example.quiz.config;

import com.example.quiz.model.Question;
import com.example.quiz.model.Quiz;
import com.example.quiz.model.User;
import com.example.quiz.repository.QuestionRepository;
import com.example.quiz.repository.QuizRepository;
import com.example.quiz.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner init(UserRepository users, QuizRepository quizzes, QuestionRepository questions) {
        return args -> {
            if (users.findByEmail("admin@quiz.com").isEmpty()) {
                users.save(new User("Administrator", "admin@quiz.com", "admin123", "ADMIN"));
            }
            if (users.findByEmail("student@quiz.com").isEmpty()) {
                users.save(new User("Demo Student", "student@quiz.com", "student123", "STUDENT"));
            }

            // Seeds every common programming-language quiz that is missing.
            // Existing quizzes/questions are not duplicated.
            seed(quizzes, questions, "Java Basics", "Core Java multiple-choice questions", "Java", "Easy", 10,
                    new String[][]{
                            {"Which keyword is used to inherit a class in Java?", "extends", "implements", "inherits", "super", "A"},
                            {"Which method is the entry point of a Java application?", "start()", "main()", "run()", "init()", "B"},
                            {"Which collection does not allow duplicate elements?", "List", "Queue", "Set", "Map", "C"},
                            {"Which keyword creates an object?", "class", "new", "this", "object", "B"},
                            {"Which type stores true or false?", "int", "String", "boolean", "double", "C"}
                    });

            seed(quizzes, questions, "Python Basics", "Python programming fundamentals", "Python", "Easy", 10,
                    new String[][]{
                            {"Which symbol starts a Python comment?", "//", "#", "/*", "--", "B"},
                            {"Which keyword defines a function in Python?", "func", "function", "def", "define", "C"},
                            {"Which data type stores an ordered mutable collection?", "tuple", "set", "list", "dict", "C"},
                            {"Which function displays output in Python?", "echo()", "print()", "display()", "write()", "B"},
                            {"Which extension is commonly used for Python files?", ".java", ".py", ".pt", ".python", "B"}
                    });

            seed(quizzes, questions, "C Programming", "C language fundamentals", "C", "Easy", 10,
                    new String[][]{
                            {"Which function is the usual entry point of a C program?", "start()", "main()", "run()", "begin()", "B"},
                            {"Which header is used for printf()?", "string.h", "math.h", "stdio.h", "stdlib.h", "C"},
                            {"Which symbol ends a C statement?", ".", ":", ";", ",", "C"},
                            {"Which type stores a single character?", "char", "string", "character", "byte", "A"},
                            {"Which operator gets the address of a variable?", "*", "&", "#", "@", "B"}
                    });

            seed(quizzes, questions, "C++ Programming", "C++ object-oriented programming basics", "C++", "Easy", 10,
                    new String[][]{
                            {"Which stream is commonly used for output in C++?", "cin", "cout", "print", "out", "B"},
                            {"Which keyword is used to create a class?", "object", "structonly", "class", "define", "C"},
                            {"Which operator is used for scope resolution?", ".", "->", "::", "?", "C"},
                            {"Which feature allows the same function name with different parameters?", "Inheritance", "Overloading", "Encapsulation", "Casting", "B"},
                            {"Which header is commonly used for cout?", "iostream", "stdio", "console", "stream.h", "A"}
                    });

            seed(quizzes, questions, "JavaScript Basics", "JavaScript language fundamentals", "JavaScript", "Easy", 10,
                    new String[][]{
                            {"Which keyword declares a block-scoped variable that can be reassigned?", "const", "let", "fixed", "varonly", "B"},
                            {"Which method prints a message to the browser console?", "console.log()", "print()", "log.console()", "writeLog()", "A"},
                            {"Which value represents an explicitly empty value?", "undefined", "null", "empty", "void", "B"},
                            {"Which operator checks strict equality?", "=", "==", "===", "!=", "C"},
                            {"Which keyword declares a function?", "def", "fun", "function", "method", "C"}
                    });

            seed(quizzes, questions, "C# Programming", "C# and .NET programming fundamentals", "C#", "Easy", 10,
                    new String[][]{
                            {"Which keyword defines a class in C#?", "class", "type", "structclass", "define", "A"},
                            {"Which method is commonly the entry point of a C# console application?", "Start()", "Main()", "Run()", "Begin()", "B"},
                            {"Which namespace contains Console?", "System", "Console", "Microsoft", "Runtime", "A"},
                            {"Which symbol is used for single-line comments?", "#", "//", "--", "/*", "B"},
                            {"Which type stores true or false?", "bool", "boolean", "bit", "logical", "A"}
                    });

            seed(quizzes, questions, "PHP Basics", "PHP web programming fundamentals", "PHP", "Easy", 10,
                    new String[][]{
                            {"Which opening tag is commonly used for PHP code?", "<php>", "<?php", "<script php>", "<?", "B"},
                            {"Which symbol starts a PHP variable name?", "#", "@", "$", "%", "C"},
                            {"Which function outputs text in PHP?", "echo", "printline", "display", "write", "A"},
                            {"Which extension is commonly used for PHP files?", ".php", ".ph", ".phtmlx", ".web", "A"},
                            {"Which operator concatenates strings in PHP?", "+", ".", "&", "::", "B"}
                    });

            seed(quizzes, questions, "SQL Basics", "SQL database query fundamentals", "SQL", "Easy", 10,
                    new String[][]{
                            {"Which command retrieves data from a table?", "GET", "SELECT", "FETCHALL", "READ", "B"},
                            {"Which command adds a new row?", "ADD", "INSERT", "CREATE ROW", "APPEND", "B"},
                            {"Which clause filters rows?", "WHERE", "FILTER", "HAVINGONLY", "WHEN", "A"},
                            {"Which command changes existing rows?", "CHANGE", "UPDATE", "MODIFY ROW", "ALTER DATA", "B"},
                            {"Which keyword sorts query results?", "SORT", "ORDER BY", "GROUP", "ARRANGE", "B"}
                    });

            seed(quizzes, questions, "Kotlin Basics", "Kotlin programming fundamentals", "Kotlin", "Easy", 10,
                    new String[][]{
                            {"Which keyword declares an immutable variable in Kotlin?", "var", "let", "val", "const", "C"},
                            {"Which function is the common entry point?", "start()", "main()", "run()", "begin()", "B"},
                            {"Which symbol is used for string templates?", "$", "#", "@", "%", "A"},
                            {"Which keyword declares a mutable variable?", "var", "val", "mut", "change", "A"},
                            {"Kotlin is commonly used for Android development. Which statement is correct?", "True", "False", "Only for databases", "Only for CSS", "A"}
                    });

            seed(quizzes, questions, "Go Programming", "Go language fundamentals", "Go", "Easy", 10,
                    new String[][]{
                            {"Which keyword declares a function in Go?", "func", "def", "function", "fun", "A"},
                            {"Which package is commonly used for formatted I/O?", "fmt", "iofmt", "print", "console", "A"},
                            {"Which keyword starts a goroutine?", "go", "async", "thread", "spawn", "A"},
                            {"Which file extension is used for Go source files?", ".go", ".golang", ".g", ".src", "A"},
                            {"Which keyword declares a constant?", "constant", "const", "fixed", "let", "B"}
                    });

            seed(quizzes, questions, "Ruby Basics", "Ruby programming fundamentals", "Ruby", "Easy", 10,
                    new String[][]{
                            {"Which keyword begins a Ruby method?", "def", "function", "method", "fun", "A"},
                            {"Which symbol is commonly used for an instance variable?", "@", "$", "#", "%", "A"},
                            {"Which method prints output in Ruby?", "puts", "echo", "printline", "displayln", "A"},
                            {"Which extension is commonly used for Ruby files?", ".rb", ".ruby", ".r", ".rub", "A"},
                            {"Ruby is primarily known as what kind of language?", "Object-oriented", "Assembly-only", "Markup-only", "Query-only", "A"}
                    });

            seed(quizzes, questions, "Swift Basics", "Swift programming fundamentals", "Swift", "Easy", 10,
                    new String[][]{
                            {"Which keyword declares an immutable value in Swift?", "var", "let", "const", "fixed", "B"},
                            {"Which keyword declares a variable in Swift?", "var", "let", "mut", "change", "A"},
                            {"Which function prints text?", "console()", "print()", "echo()", "write()", "B"},
                            {"Which language is Swift primarily associated with?", "Apple platforms", "SQL servers", "CSS browsers", "Linux kernels only", "A"},
                            {"Which extension is commonly used for Swift source files?", ".swift", ".sw", ".ios", ".apple", "A"}
                    });
        };
    }

    private void seed(QuizRepository quizzes, QuestionRepository questions,
                      String title, String description, String category, String difficulty,
                      Integer duration, String[][] rows) {
        Quiz quiz = quizzes.findAll().stream()
                .filter(q -> title.equalsIgnoreCase(q.getTitle()))
                .findFirst()
                .orElseGet(() -> quizzes.save(new Quiz(title, description, category, difficulty, duration)));

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
        return x;
    }
}
