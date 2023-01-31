package com.jwt.gestaodeprojetos.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.jwt.gestaodeprojetos.service.UsuarioService;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UsuarioService usuarioService;

    // Obtem o usuario pelo nome
    @Override
    public UserDetails loadUserByUsername(String email){
        // Usuario usuario = getUser(() -> usuarioService.obterPorEmail(email));
        // return usuario;
        return usuarioService.obterPorEmail(email).get();
    }

    // Obtem o usuario pelo id
    public UserDetails obterUsuarioPorId (Long id){
        return usuarioService.obterPorId(id).get();
    }
    
    // Tenta obter um usuario, se não lança uma exceção

    // public Usuario getUser(Supplier<Optional<Usuario>> supplier){
    //   return supplier.get().orElseThrow(() -> new UsernameNotFoundException("Usuario não encontrado"));
    // }
    
}
