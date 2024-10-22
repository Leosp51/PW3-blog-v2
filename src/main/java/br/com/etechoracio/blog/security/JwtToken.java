package br.com.etechoracio.blog.security;

import br.com.etechoracio.blog.dto.UsuarioAutenticadoDTO;
import br.com.etechoracio.blog.enums.RoleEnum;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;


import java.security.Key;
import java.time.Duration;
import java.util.Date;
import java.util.List;

@Component //parecido com config => injetar a classe em outra classe
public class JwtToken {

    @Value("${token.security.key}")// @Value Pega o valor presente no application.properties
    private String jwtSecurityKey;

    @Value("${token.security.expiration-time}") //pega o valor do application.properties
    private Duration jwtExpirationTime;

    private Key getChaveAssinatura() {
        return Keys.hmacShaKeyFor(jwtSecurityKey.getBytes());
    }

    private Claims getClaims(String token) { //setta a assinatura (construir
        return Jwts.parserBuilder()
                .setSigningKey(getChaveAssinatura())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

   public String gerar(UserDetails userDetails) { //recebe um userDetails (regras e valores), cria o token(jwt) usando isso
       return Jwts.builder()
               .setSubject(userDetails.getUsername()) //informações
               .claim("roles", ((UsuarioAutenticadoDTO)userDetails).getRole())
               .claim("id", ((UsuarioAutenticadoDTO)userDetails).getId())
               .claim("nome", ((UsuarioAutenticadoDTO)userDetails).getNome())
               .setIssuedAt(new Date(System.currentTimeMillis()))
               .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationTime.toMillis())) //expiração
               .signWith(getChaveAssinatura(), SignatureAlgorithm.HS256) //codificação e retorna uma string (cria o token)
               .compact();
   }

    @SuppressWarnings("unchecked")
    public UserDetails getUsuarioAutenticado(String token) { //pega informações do token
        var claims = getClaims(token); //pega as regras
        var role = claims.get("roles", List.class).stream().findAny().orElse(RoleEnum.COMENTADOR.name()); //pega as roles
        return (UserDetails) UsuarioAutenticadoDTO.builder().login(claims.getSubject())
                                              .id(claims.get("id", Integer.class))
                                              .role(RoleEnum.getByValue((String) role))
                                    .build();
    }

}
