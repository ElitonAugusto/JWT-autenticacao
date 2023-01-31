package com.jwt.gestaodeprojetos.service;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.jwt.gestaodeprojetos.model.Usuario;
import com.jwt.gestaodeprojetos.repository.UsuarioRepository;

@Service
public class UsuarioService {
    
    @Autowired
    private UsuarioRepository repositorioUsuario;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<Usuario> obterTodos(){
        return repositorioUsuario.findAll();
    }

    public Optional<Usuario> obterPorId(Long id){
        return repositorioUsuario.findById(id);
    }

    public Optional<Usuario> obterPorEmail(String email){
        return repositorioUsuario.findByEmail(email);
    }

    public Usuario adicionar(Usuario usuario){
        usuario.setId(null);
        if(obterPorEmail(usuario.getEmail()).isPresent()){
            throw new InputMismatchException("Já existe um usuario cadastrados com o email: "
            + usuario.getEmail());
        }
        // Aqui codifica a senha para não deixar publica, gerando um hash.
        String senha = passwordEncoder.encode(usuario.getSenha());

        usuario.setSenha(senha);
        return repositorioUsuario.save(usuario);
    }
}