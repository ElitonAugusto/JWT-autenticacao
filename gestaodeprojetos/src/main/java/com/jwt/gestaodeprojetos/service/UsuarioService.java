package com.jwt.gestaodeprojetos.service;

import java.util.Collections;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.jwt.gestaodeprojetos.model.Usuario;
import com.jwt.gestaodeprojetos.repository.UsuarioRepository;
import com.jwt.gestaodeprojetos.security.JWTService;
import com.jwt.gestaodeprojetos.view.usuario.LoginResponse;

@Service
public class UsuarioService {

    private static final String hederPrefix = "Bearer ";
    
    @Autowired
    private UsuarioRepository repositorioUsuario;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    public JWTService jwtService;

    @Autowired
    public AuthenticationManager authenticationManager;

    

    public UsuarioService(UsuarioRepository repositorioUsuario, PasswordEncoder passwordEncoder, JWTService jwtService,
            AuthenticationManager authenticationManager) {
        this.repositorioUsuario = repositorioUsuario;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

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

    public LoginResponse logar(String email, String senha){

        // Loga e devolve uma autenticação.
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(email, senha, Collections.emptyList()));
        
        // Passa a autenticação para o spring security tomar conta.
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Gera o token do usuario para devolver para o usuario.
        // Exemplo: Bearer fs3d51vd56vv6sa.55dmifnmro151.sdinfu4515vv
        String token = hederPrefix + jwtService.gerarToken(authentication);

        Usuario usuario = repositorioUsuario.findByEmail(email).get();

        return new LoginResponse(token, usuario);
    }
}
