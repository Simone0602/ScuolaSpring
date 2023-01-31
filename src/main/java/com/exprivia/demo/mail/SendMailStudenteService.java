package com.exprivia.demo.mail;

import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.exprivia.demo.exception.DontSendEmailException;
import com.exprivia.demo.model.Studente;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class SendMailStudenteService {
	private final JavaMailSender javaMailSender;
	
	@Autowired
	public SendMailStudenteService(JavaMailSender javaMailSender) {
		this.javaMailSender = javaMailSender;
	}
	
	public String sendEmail(Studente studente)throws UnsupportedEncodingException {
		MimeMessage message = javaMailSender.createMimeMessage();
		String text = "";
		
		try {
			MimeMessageHelper helper = new MimeMessageHelper(message, true);
			if(helper != null) {
				helper.setFrom("ProgettoExprivia@gmail.com", "ScuolaXX");
				helper.setTo(studente.getMail());
				helper.setSubject("Reset password");
				helper.setText(text, true);
			}
			throw new DontSendEmailException("Errore nell'invio dell'Email");
		}catch(MessagingException e) {
			e.printStackTrace();
		}
		javaMailSender.send(message);
		return "Email inviata a: " + studente.getMail();
	}
	
}
