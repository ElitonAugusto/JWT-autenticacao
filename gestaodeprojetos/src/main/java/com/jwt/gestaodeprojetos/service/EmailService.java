package com.jwt.gestaodeprojetos.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.jwt.gestaodeprojetos.model.MensagemEmail;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {
    
    @Autowired
    private JavaMailSender javaMailSender;

    /**
     * Metodo para enviar E-mail.
     * @param menssagemEmail Menssagem que vai ser enviada.
     * @throws MessagingException
     */
    public void enviar (MensagemEmail mensagemEmail) throws MessagingException{

        // É uma mensagem de e-mail que entende os tipos MIME
        //(identificador usado na Internet para indicar o tipo de dado que um arquivo contém)
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        // Declarando a codificação que vai ser usada para enviar o E-mail.
        //(tudo o que helper receber ele passa depois por aqui para o mimeMessage)
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

        // Pega o remetente
        helper.setFrom(mensagemEmail.getRemetente());
        helper.setSubject(mensagemEmail.getAssunto());
        helper.setText(mensagemEmail.getMensagem(), true); //'true' sug. que pode enviar como html
        // o legal de usar html é que podemos colocar imagens, gifs..

        // Recebe uma lista de endereços de internet (destinatarios)
        // e transformando em um novo Array de String
        helper.setTo(mensagemEmail.getDestinatarios()
            .toArray(new String[mensagemEmail.getDestinatarios().size()])
        ); 

        // Eviando o E-mail
        // envia o mimeMessage depois que ele foi configurado pelo helper
        javaMailSender.send(mimeMessage);
    }
    
}
