package br.com.etechoracio.blog.dto;

import br.com.etechoracio.blog.entity.Usuario;
import br.com.etechoracio.blog.enums.RoleEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioAutenticadoDTO implements UserDetails {

    private Integer id;
    private String nome;
    private String login;
    private String senha;
    private RoleEnum role;

    public Usuario toUsuario() {
        Usuario usuario = new Usuario();
        usuario.setId(id);
        return usuario;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(){
        var auts = new SimpleGrantedAuthority(role.getValue());
        return List.of(auts);
    }
    @Override
    public String getPassword(){
        return senha;
    }

    @Override
    public String getUsername() {
        return nome;
    }

}
