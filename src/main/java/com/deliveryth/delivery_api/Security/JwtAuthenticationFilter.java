package com.deliveryth.delivery_api.Security;

import java.io.IOException;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.deliveryth.delivery_api.model.Usuario;
import com.deliveryth.delivery_api.repository.UsuarioRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter{
    
    private final JwtUtil jwtUtil;
    private final UsuarioRepository repository;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, 
                               UsuarioRepository repository) {
        this.jwtUtil = jwtUtil;
        this.repository= repository;
    }

    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain chain)
      throws IOException, ServletException{

       String path = request.getRequestURI();
       if (path.startsWith("/swagger-ui") || path.contains("/v3/api-docs") || path.contains("/webjars")){
        chain.doFilter(request, response);
        return;
       }

       String token = extractToken(request);

       if (token != null) {
        try{

            String email = jwtUtil.extracEmail(token);

            if(email != null && 
                SecurityContextHolder.getContext().getAuthentication() == null){
                
                 Usuario usuario = repository.findByEmail(email).orElse(null);

                if(usuario != null && 
                    jwtUtil.isTokenValid(token, usuario.getEmail())){
                    
                    String role = jwtUtil.extracRole(token);

                    SimpleGrantedAuthority authority = 
                    new SimpleGrantedAuthority("ROLE_" + role);

                    UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(
                        usuario, null, List.of(authority));

                    auth.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                    );

                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            }
        } catch (Exception e){
        System.out.println("Erro JWT: " + e.getMessage());
        }
    }
    
    chain.doFilter(request, response);
     }

    private String extractToken(HttpServletRequest request){
        String header = request.getHeader("Authorization");
        
        if(header == null || !header.startsWith("Bearer")){
            return null;
        }

        return header.substring(7);
    }
}
