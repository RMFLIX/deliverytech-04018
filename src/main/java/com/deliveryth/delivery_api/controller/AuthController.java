package com.deliveryth.delivery_api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deliveryth.delivery_api.dto.requests.LoginRequestDTO;
import com.deliveryth.delivery_api.enums.Role;
import com.deliveryth.delivery_api.model.Usuario;
import com.deliveryth.delivery_api.repository.UsuarioRepository;
import com.deliveryth.delivery_api.Security.JwtUtil;

import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/ap/auth")
public class AuthController {
    private final UsuarioRepository repository;
    private final PasswordEncoder passwEncoder;
    private final JwtUtil jwtUtil;

    public AuthController(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, 
        JwtUtil jwtUtil) {
        this.repository= usuarioRepository;
        this.passwEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public ResponseEntity<?> cadastrar(@RequestBody LoginRequestDTO request){
        if(repository.existsByEmail(request.getEmail())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("E-mail já cadastrado.");
        }

        if(request.getRole() == Role.ADMIN){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Não é permitido criar um usuário.");
 
        }

        Usuario usuario = new Usuario();
        usuario.setEmail(request.getEmail());
        usuario.setSenha(passwEncoder.encode(request.getSenha()));
        usuario.setRole(request.getRole());
        repository.save(usuario);

        return ResponseEntity.status(HttpStatus.CREATED).body("Usuário cadastrado com sucesso.");
    }
}
