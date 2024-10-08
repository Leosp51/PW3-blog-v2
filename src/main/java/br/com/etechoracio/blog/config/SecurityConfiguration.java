package br.com.etechoracio.blog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration// Faz com que a classe seja executada automaticamente
public class SecurityConfiguration {

    //@Autowired
    //private UserDetailsService userDetailsService;

    //@Autowired
    //private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean// =>para falar para spring como eu quero que ele instâncie; parecido com @autowired
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();//segurança simples
    }

    //@Bean
    //public AuthenticationProvider authenticationProvider() {
    //    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
    //    authProvider.setUserDetailsService(userDetailsService);
    //    authProvider.setPasswordEncoder(passwordEncoder());
    //    return authProvider;
    //}

    //@Bean
    //public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
    //    return config.getAuthenticationManager();
    //}

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors(Customizer.withDefaults())
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(request -> {
                    request.requestMatchers("/login").permitAll();//permite qualquer request pelo /login
                    request.requestMatchers("/usuarios").permitAll();
                    request.anyRequest().authenticated(); //qualquer resquest diferente do /login não é aceita (bloqueia)
                })

            .sessionManagement(manager -> manager.sessionCreationPolicy(STATELESS));

            //.authenticationProvider(authenticationProvider())

            //.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}
