package br.com.gabriel.gestao_vagas.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import br.com.gabriel.gestao_vagas.providers.JWTCandidateProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// definindo como component para spring poder gerenciar o ciclo de vida 
@Component
public class SecurityCandidateFilter extends OncePerRequestFilter {

    @Autowired
    private JWTCandidateProvider jwtcandidateProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        // SecurityContextHolder.getContext().setAuthentication(null);
        String header = request.getHeader("Authorization");

        // verificando se a requisição de segurança é de candidate
        if(request.getRequestURI().startsWith("/candidate")) {
            if (header != null) {
            var token = this.jwtcandidateProvider.validateToken(header);

            if (token == null) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }

            // se for válido passar o id do candidate para o token
            request.setAttribute("candidate_id", token.getSubject());
            
            // pegando todos as list roles do candidate e convertendo para objeto 
            var roles = token.getClaim("roles").asList(Object.class);

            // mapeando cada role para passar para o autenticação e convertendo para uma lista
            // para o spring reconhecer uma role devemos passar o prefix ROLE_ antes
            var grants = roles.stream()
            .map(role -> new SimpleGrantedAuthority("ROLE_" + role.toString().toUpperCase()))
            .toList();

            UsernamePasswordAuthenticationToken auth = 
            new UsernamePasswordAuthenticationToken(token.getSubject(), null, grants);
            SecurityContextHolder.getContext().setAuthentication(auth);

            }
        }
 
        filterChain.doFilter(request, response);
    }
}
