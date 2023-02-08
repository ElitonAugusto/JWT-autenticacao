package com.jwt.gestaodeprojetos.view.usuario;

import com.jwt.gestaodeprojetos.model.Usuario;

public class LoginResponse {

    private String token;
    private Usuario usuario;
    
    public LoginResponse(String token, Usuario usuario) {
        this.token = token;
        this.usuario = usuario;
    }

    public LoginResponse() {
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    
    
}
