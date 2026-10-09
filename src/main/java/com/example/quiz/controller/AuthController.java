package com.example.quiz.controller;
import com.example.quiz.model.User; import com.example.quiz.repository.UserRepository; import org.springframework.http.*; import org.springframework.web.bind.annotation.*; import java.util.*;
@RestController @RequestMapping("/api") @CrossOrigin(origins="*")
public class AuthController{
 private final UserRepository repo; public AuthController(UserRepository repo){this.repo=repo;}
 @PostMapping("/register") public ResponseEntity<?> register(@RequestBody User u){ if(u.getEmail()==null||u.getPassword()==null) return ResponseEntity.badRequest().body(Map.of("message","Email and password are required")); if(repo.findByEmail(u.getEmail()).isPresent()) return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message","Email already registered")); u.setRole("STUDENT"); User saved=repo.save(u); return ResponseEntity.ok(Map.of("id",saved.getId(),"name",saved.getName(),"email",saved.getEmail(),"role",saved.getRole())); }
 @PostMapping("/login") public ResponseEntity<?> login(@RequestBody Map<String,String> data){ String email=data.get("email"), password=data.get("password"); Optional<User> found=repo.findByEmail(email==null?"":email); if(found.isEmpty()||!found.get().getPassword().equals(password)) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message","Invalid email or password")); User u=found.get(); return ResponseEntity.ok(Map.of("id",u.getId(),"name",u.getName(),"email",u.getEmail(),"role",u.getRole())); }
}
