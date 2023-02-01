package com.jwt.gestaodeprojetos.security;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.InputMismatchException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
// classe onde toda a requisição vem antes de chegar no nosso endpoint.
public class JWTAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JWTService jwtService;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    // Metodo principal onde toda a requisição vem antes de chegar no nosso
    // endpoint.
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // Obtem o token de dentro da requisição
        String token = obterToken(request);
        // Obtem o id do usuario que esta dentro do token.
        // - usa-se o optional pois pode ser que venha nulo
        Optional<Long> id = jwtService.obterIdDoUsuario(token);

        // Se não achou o id, é porque o token esta incorreto.  
        if (!id.isPresent()) {
            throw new InputMismatchException("Token inválido!");
        }

        // Obtem o usuario dono do token pelo seu id.
        UserDetails usuario = customUserDetailsService.obterUsuarioPorId(id.get());

        // Nesse ponto verificamos se o usuario esta autenticado.
        // Aqui também poderiamos validar as permições.
        UsernamePasswordAuthenticationToken autenticacao = new UsernamePasswordAuthenticationToken(usuario, null,
                Collections.emptyList());

        // Mudando a autenticação para propria requisição
        autenticacao.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        // Repasso a autenticação para o contexto do security.
        // A partir de agora o spring vai tomar conta de tudo.
        SecurityContextHolder.getContext().setAuthentication(autenticacao);
    }

    private String obterToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");

        // Verifica se veio alguma coisa sem ser espaçoes em branco dentro do token.
        // (Verifica se é só textos).
        // ! foi negado entao se veio espaçoes retorna null.
        if (!StringUtils.hasText(token)) {
            return null;
        }

        // o token vem dessa forma:
        // Bearer s4dssdsvf.656vdaddvd.5vb45bs4
        // entao usa o . substring(7) para pular 7 caracteres e pegar só o token
        return token.substring(7);
    }

}
