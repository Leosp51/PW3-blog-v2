package br.com.etechoracio.blog.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtToken jwtToken; //criação e leitura de token jwt

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var token = getToken(request);
        if (token.isEmpty()) {
            filterChain.doFilter(request, response);
            return;
        }

        var userDetails = jwtToken.getUsuarioAutenticado(token.get());//

        var authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));//

        SecurityContext context = SecurityContextHolder.createEmptyContext(); //contexto de segurança
        context.setAuthentication(authToken);//
        SecurityContextHolder.setContext(context);// contexto é o que está acontecendo na aplicação

        filterChain.doFilter(request, response);
    }

    private Optional<String> getToken(HttpServletRequest request) {
        var token = request.getHeader(HttpHeaders.AUTHORIZATION); //pega se o header de autorização foi enviado junto
        return token == null || token.isEmpty() ? Optional.empty()
                                                : Optional.of(token);
    }


}
