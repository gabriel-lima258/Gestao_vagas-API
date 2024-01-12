package br.com.gabriel.gestao_vagas.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

// @Configuration indica que essa é uma classe de configuração
@Configuration
@EnableMethodSecurity
public class SecurityConfig {
    // método para gerenciar as requisições
    
    @Autowired
    private SecurityCompanyFilter securityCompanyFilter;

    @Autowired
    private SecurityCandidateFilter securityCandidateFilter;

    // definindo rotas do docs swagger
    private static final String[] PERMIT_ALL_LIST = {
        "/swagger-ui/**",
        "/v3/api-docs/**",
        "/swagger-resources/**",
        "/actuator/**"
    };

    // @Bean serve para definir algum método já gerenciado pelo spring e sobrescreve-lo
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        // desabilita o padrão springSecurity para eu fazer a configuração costumizada
        http.csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> {
                auth.requestMatchers("/candidate/").permitAll()
                    .requestMatchers("/company/").permitAll()
                    .requestMatchers("/company/auth").permitAll()
                    .requestMatchers("/candidate/auth").permitAll()
                    .requestMatchers(PERMIT_ALL_LIST).permitAll();
                auth.anyRequest().authenticated();  
            })
            .addFilterBefore(securityCompanyFilter, BasicAuthenticationFilter.class)
            .addFilterBefore(securityCandidateFilter, BasicAuthenticationFilter.class);
            // adicionando o filtro criado

        return http.build();
    }

    // Criptografando todo qualquer tipo de senha no banco de dados
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
