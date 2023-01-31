package com.jwt.gestaodeprojetos.security;

import java.util.Date;
import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.jwt.gestaodeprojetos.model.Usuario;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JWTService {

    // Chave secreta utilizada pela JWT codificar e descodificar o token
    // Chave que sabe criptografar e descriptografar o token
    // OBS. Geralmente não deixamos isso dentro do codigo e sim pegamos 
    // de alguma configuraçao ou de algum lugar, pois isso tem que ser muito seguro
    private static final String chavePrivateJWT = "secretKey";
    // o nome tem que ser muito seguro para niguem conseguir descriptografar
    // mas como é uma aplicação simples deixei como secretKey

    /**
     * Metodo para gerar o token JWT.
     * @param authentication Autenticação do usuario.
     * @return Token.
     */
                // a Authentication sempre tem um usuario dentro 
    public String gerarToken(Authentication authentication){

        //deveria estar em uma configuração
        // tem que ser em milisegundos
        // 86400000 equivale +- 1 dia
        // o tempo vairia de acordo com a regra de negocios
        int tempoExpiracao = 86400000;

        // Pega a data de hoje mais o tempo de expiração
        Date dataExpiracao = new Date(new Date().getTime() + tempoExpiracao);

        // Pegando o usuario que esta tentando se autenticar
        // e fazendo o (time)casting para converter o object em usuario
        Usuario usuario = (Usuario)authentication.getPrincipal();

        // pega todos os dados e retorna um bonito do JWT
        // ele é um builder entao podemos construir com os paramentros que quiser
        return Jwts.builder()
        //setsubject recebe alguma coisa unica do usuario que ele pode validar o usuario que ele é unico
        .setSubject(usuario.getId().toString())
        //recebe a data que ele vai se basear para esse token (Start)
        .setIssuedAt(new Date())
        // data de expiração do token
        .setExpiration(dataExpiracao)
        // qual o tipo de criptografia e qual a chave primaria
        .signWith(SignatureAlgorithm.HS512, chavePrivateJWT)
        // manda o jwt compactar tudo,  vai criar o token e retornar uma String
        .compact();
    }

    /**
     * Metodo para retornar o id do usuario dono do token. 
     * @param token token do usuario.
     * @return id do usuario.
     */
    public Optional<Long> obterIdDoUsuario(String token){
        try {
            // Retorna as permições do token
           Claims claims = parse(token).getBody();
           // Retorna o id de dentro do token com base nas Claims
           // retorna se encontrar se não retorna null.
            return Optional.ofNullable(Long.parseLong(claims.getSubject()));
        } catch (Exception e) {
            // Se não encontrar nada devolve um optional null.
            return Optional.empty();
        }

    }

    /**
     * Metodo que sabe descobrir de dentro do token com base na chave
     * privada qual as (Claims) permissões do usuário.
     * @param token
     * @return (Claims) permissões do usuário.
     */
    private Jws<Claims> parse(String token) {
        return Jwts.parser().setSigningKey(chavePrivateJWT).parseClaimsJws(token);
    }


    
}
