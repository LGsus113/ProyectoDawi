package com.cibertec.edu.Proyecto_DAWI.service.impl;

import com.cibertec.edu.Proyecto_DAWI.entity.Usuario;
import com.cibertec.edu.Proyecto_DAWI.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static org.springframework.security.core.userdetails.User.withUsername;

@Service
public class CustomUserService implements UserDetailsService {
    @Autowired
    UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Usuario> optional = usuarioRepository.findByEmail(username);
        if (optional.isEmpty()){
            throw new UsernameNotFoundException("Usuario no encontrado");
        }

        Usuario usuario = optional.get();
        UserBuilder userBuilder = withUsername(usuario.getNombre());
        userBuilder.password(usuario.getContrasena());
        userBuilder.roles(String.valueOf(usuario.getRol()));

        return userBuilder.build();
    }
}
