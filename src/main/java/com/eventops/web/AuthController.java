package com.eventops.web;


import java.util.Collections;

import com.eventops.model.Client;
import com.eventops.model.Role;
import com.eventops.payload.LoginDto;
import com.eventops.payload.SignUpDto;
import com.eventops.repository.ClientRepository;
import com.eventops.repository.RoleRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;






@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginDto loginDto){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDto.getnameOrEmail(), loginDto.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        Map<String,Object> response = new HashMap<>();
        List<String> errores = new ArrayList<>();
        Client userlogged = clientRepository.findByNameOrEmail(loginDto.getnameOrEmail(), loginDto.getnameOrEmail()).get();

        if(userlogged == null){
            errores.add("Este usuario no est?? registrado. Primero debes registrarte");
            response.put("errores", errores);
            return ResponseEntity.badRequest().body(response);
        }
        
        clientRepository.save(userlogged);
        return new ResponseEntity<>("User signed-in successfully!.", HttpStatus.OK);
    }


    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignUpDto signUpDto){
        Map<String,Object> response = new HashMap<>();
        List<String> errores = new ArrayList<>();

        // add check for email exists in DB
        if(clientRepository.existsByEmail(signUpDto.getEmail())){
            errores.add("Este email ya est?? registrado");
            response.put("errores", errores);
            return ResponseEntity.badRequest().body(response);
        }

        if(!signUpDto.getPassword().equals(signUpDto.getConfirm())){
            errores.add("Las contrase??as no coinciden");
            response.put("errores", errores);
            return ResponseEntity.badRequest().body(response);
        }

     
        

        // create user object
        Client user = new Client();
        user.setName(signUpDto.getName());
        user.setEmail(signUpDto.getEmail());
        user.setPassword(passwordEncoder.encode(signUpDto.getPassword()));
        user.setApellidos(signUpDto.getApellidos());

        

        Role roles = roleRepository.findByName("ROLE_USER").get();
        user.setRoles(Collections.singleton(roles));
        
        clientRepository.save(user);

        return new ResponseEntity<>("Usuario registrado correctamente", HttpStatus.OK);

    }    
}
