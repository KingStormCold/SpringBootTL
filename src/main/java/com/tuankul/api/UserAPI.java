package com.tuankul.api;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.tuankul.entities.UserEntityJWT;
import com.tuankul.service.JWTService;
import com.tuankul.service.UserService;

@RestController
public class UserAPI {
    @Autowired
    private JWTService jwtService;
    @Autowired
    private UserService userService;

    /* ---------------- GET ALL USER ------------------------ */
    @GetMapping(value= "/users")
    public ResponseEntity<List<UserEntityJWT>> getAllUser() {
        return new ResponseEntity<List<UserEntityJWT>>(this.userService.findAll(),
                HttpStatus.OK);
    }

    /* ---------------- GET USER BY ID ------------------------ */
    @GetMapping(value="/users/{id}")
    public ResponseEntity<Object> getUserById(@PathVariable int id) {
        UserEntityJWT user = this.userService.findById(id);
        if (user != null) {
            return new ResponseEntity<Object>(user, HttpStatus.OK);
        }
        return new ResponseEntity<Object>("Not Found User",
                HttpStatus.NO_CONTENT);
    }

    /* ---------------- CREATE NEW USER ------------------------ */
    @PostMapping(value="/users")
    public ResponseEntity<String> createUser(@RequestBody UserEntityJWT user) {
        if (this.userService.add(user)) {
            return new ResponseEntity<String>("Created!", HttpStatus.CREATED);
        } else {
            return new ResponseEntity<String>("User Existed!",
                    HttpStatus.BAD_REQUEST);
        }
    }

    /* ---------------- DELETE USER ------------------------ */
    @DeleteMapping(value="/users/{id}")
    public ResponseEntity<String> deleteUserById(@PathVariable int id) {
        this.userService.delete(id);
        return new ResponseEntity<String>("Deleted!", HttpStatus.OK);
    }

    @PostMapping(value="/login")
    public ResponseEntity<String> login(HttpServletRequest request,
            @RequestBody UserEntityJWT user) {
        String result = "";
        HttpStatus httpStatus = null;
        try {
            if (this.userService.checkLogin(user)) {
                result = this.jwtService.generateTokenLogin(user.getUsername());
                httpStatus = HttpStatus.OK;
            } else {
                result = "Wrong userId and password";
                httpStatus = HttpStatus.BAD_REQUEST;
            }
        } catch (Exception ex) {
            result = "Server Error";
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<String>(result, httpStatus);
    }
}
