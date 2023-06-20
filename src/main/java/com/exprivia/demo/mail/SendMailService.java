package com.exprivia.demo.mail;

import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import lombok.AllArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.exprivia.demo.dto.ContattaciDto;

@AllArgsConstructor
@Service
public class SendMailService {
	private final JavaMailSender javaMailSender;
	
	public String sendEmail(String email, String token, String tipoUser) throws UnsupportedEncodingException {
		MimeMessage message = javaMailSender.createMimeMessage();
		
		try {
			MimeMessageHelper helper = new MimeMessageHelper(message, true);
			if(helper != null) {
				helper.setFrom("prova.scuola.spring@gmail.com", "ScuolaXX");
				helper.setTo(email);
				helper.setSubject("Reset password");
				helper.setText(generateTextMailRecoverPass(token, tipoUser), true);
			}
		}catch(MessagingException e) {
			e.printStackTrace();
		}
		javaMailSender.send(message);
		return "Email inviata a: " + email + "! Si prega di controllare la posta elettronica";
	}
	

	public String sendSupportMail(ContattaciDto contattaciDto) throws UnsupportedEncodingException, MessagingException {
		MimeMessage message = javaMailSender.createMimeMessage();
		
		MimeMessageHelper helper = new MimeMessageHelper(message, true);
		if(helper != null) {
			helper.setFrom(contattaciDto.getMail());
			helper.setTo("scuola.spring@gmail.com");
			helper.setSubject("Richiesta di supporto");
			helper.setText(contattaciDto.getDescrizione(), true);
		}
	
		javaMailSender.send(message);
		return "Richiesta di supporto inviata. Grazie per averci segnalato un errore, cercheremo di risolverlo il prima possibile";
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
