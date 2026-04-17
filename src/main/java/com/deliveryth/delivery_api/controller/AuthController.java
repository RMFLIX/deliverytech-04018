package com.deliveryth.delivery_api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deliveryth.delivery_api.dto.requests.LoginRequestDTO;
import com.deliveryth.delivery_api.dto.responses.LoginResponseDTO;
import com.deliveryth.delivery_api.enums.Role;
import com.deliveryth.delivery_api.model.Usuario;
import com.deliveryth.delivery_api.repository.UsuarioRepository;
import com.deliveryth.delivery_api.Security.JwtUtil;


@RestController
@RequestMapping("/ap/auth")
public class AuthController {

    private final UsuarioRepository repository;
    private final PasswordEncoder passwEncoder;
    private final JwtUtil jwtUtil;

    public AuthController(UsuarioRepository usuarioRepository, 
                         PasswordEncoder passwordEncoder, 
                         JwtUtil jwtUtil) {
        this.repository= usuarioRepository;
        this.passwEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
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

        usuario.setRole(
            request.getRole() != null ? request.getRole() : Role.CLIENTE
        );

        usuario.setAtivo(true);

        repository.save(usuario);

        String token = jwtUtil.generateToken(usuario);

        return ResponseEntity.status(HttpStatus.CREATED)
        .body(new LoginResponseDTO(token));
    }

    @PostMapping
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO login) {
       
        Usuario usuario = repository.findByEmail(login.getEmail())
        .orElse(null);

        if (usuario == null ||
            !passwEncoder.matches(login.getSenha(), usuario.getSenha())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("Credenciais inválidas.");
            }

        String token = jwtUtil.generateToken(usuario);
        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    @GetMapping("/me")
    public ResponseEntity<Usuario> me(Authentication auth){
       
        String email = auth.getName();
       
        Usuario usuario = repository.findByEmail(email)
        .orElseThrow();

        return ResponseEntity.ok(usuario);
    }
}
