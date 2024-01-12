package br.com.gabriel.gestao_vagas.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import br.com.gabriel.gestao_vagas.providers.JWTProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class SecurityCompanyFilter extends OncePerRequestFilter {

    @Autowired
    private JWTProvider jwtProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, 
            HttpServletResponse response, 
            FilterChain filterChain)
            throws ServletException, IOException {
        
        // antes de inciar uma autenticação, ter certeza que esta vazia
        // SecurityContextHolder.getContext().setAuthentication(null);
        String header = request.getHeader("Authorization");

        // verificando se a requisição de segurança pertence a company
        if (request.getRequestURI().startsWith("/company")){
             // se for uma rota que precisa de autenticação vai entrar no if
            if(header != null){
                var token = this.jwtProvider.validateToken(header);

                // se o token for nulo -> erro
                if (token == null) {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    return;
                }

                // mapeando as roles de company
                var roles = token.getClaim("roles").asList(Object.class);
                var grants = roles.stream()
                        .map(role -> new SimpleGrantedAuthority("ROLE_" + role.toString().toUpperCase()))
                        .toList();

                // buscando o id da company pelo token
                request.setAttribute("company_id", token.getSubject());
                // injetando as informações do usuário autenticado no contexto do Spring Security, 
                // permitindo que essas informações sejam utilizadas em todas as requisições 
                UsernamePasswordAuthenticationToken auth = 
                new UsernamePasswordAuthenticationToken(token.getSubject(), null, grants);
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }

        filterChain.doFilter(request, response);
    }
    
}
