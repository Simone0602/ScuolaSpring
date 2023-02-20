package com.exprivia.demo.mail;

import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.exprivia.demo.exception.DontSendEmailException;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class SendMailService {
	private final JavaMailSender javaMailSender;
	
	@Autowired
	public SendMailService(JavaMailSender javaMailSender) {
		this.javaMailSender = javaMailSender;
	}
	
	@SuppressWarnings("unused")
	public String sendEmail(String email, String token, String tipoUser) throws UnsupportedEncodingException {
		MimeMessage message = javaMailSender.createMimeMessage();
		
		try {
			MimeMessageHelper helper = new MimeMessageHelper(message, true);
			if(helper != null) {
				helper.setFrom("ProgettoExprivia@gmail.com", "ScuolaXX");
				helper.setTo(email);
				helper.setSubject("Reset password");
				helper.setText(generateTextMailRecoverPass(token, tipoUser), true);
			}else {
				throw new DontSendEmailException("Errore nell'invio dell'Email");
			}
		}catch(MessagingException e) {
			e.printStackTrace();
		}
		javaMailSender.send(message);
		return "Email inviata a: " + email + "! Si prega di controllare la posta elettronica";
	}
	private String generateTextMailRecoverPass(String token, String tipoUser) {
		String text = "<strong>Recupero password</strong>"
				+ "<div>"
				+	"<p>Si prega di confermare la password cliccando sul link qui affinaco "
				+	"<a href=\"http://localhost:4200/password-dimenticata/" + tipoUser + "/" + token + "\">"
				+		"Modifica password"
				+ 	"</a>"
				+ "</div>";
		return text;
	}
}
