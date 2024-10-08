package br.com.etechoracio.blog.service;

import br.com.etechoracio.blog.dto.UsuarioInsercaoDTO;
import br.com.etechoracio.blog.dto.UsuarioResponseDTO;
import br.com.etechoracio.blog.entity.Usuario;
import br.com.etechoracio.blog.mapper.ModelMapperCustom;
import br.com.etechoracio.blog.repository.UsuarioRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j //"System.out"
@Service
public class UsuarioService {

    @Autowired
    private ModelMapperCustom mapper;

    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired //a forma de "auto-instânciamento" é configurada no config/SecurityConfiguration no método pass
    private PasswordEncoder passwordEncoder;

    public UsuarioResponseDTO inserir(UsuarioInsercaoDTO usuario) {
        var insert = mapper.map(usuario, Usuario.class);
        //senha plana => salvar sem criptografia

        //Criptografando senha
        var senhaCrifrada = passwordEncoder.encode(usuario.getSenha());
        log.info("Senha cifrada => {}", senhaCrifrada);

        insert.setSenha(senhaCrifrada);

        //token = > precisa ter validade (para expirar e não ser eterno)
        return mapper.map(usuarioRepository.save(insert), UsuarioResponseDTO.class);
    }

}