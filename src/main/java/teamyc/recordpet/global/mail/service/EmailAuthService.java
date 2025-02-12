package teamyc.recordpet.global.mail.service;

import static teamyc.recordpet.global.exception.ResultCode.EMAIL_SEND_FAILED;
import static teamyc.recordpet.global.exception.ResultCode.INVALID_CODE;
import static teamyc.recordpet.global.exception.ResultCode.NOT_FOUND_EMAIL_AUTH;

import jakarta.mail.Message.RecipientType;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import teamyc.recordpet.global.exception.GlobalException;
import teamyc.recordpet.global.mail.entity.EmailAuth;
import teamyc.recordpet.global.mail.repository.EmailAuthRepository;

@Service
@RequiredArgsConstructor
public class EmailAuthService {

    private final EmailAuthRepository emailAuthRepository;
    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String email;

    @Value("${spring.mail.url.auth}")
    private String authEmailLink;

    public String createAuthCode() {
        return UUID.randomUUID().toString().substring(0, 8);
    }

    public void sendMessage(String to) {
        try {
            String code = createAuthCode();
            MimeMessage message = createMessage(to, code);

            if (emailAuthRepository.existsById(to)) {
                emailAuthRepository.deleteById(to);
            }
            EmailAuth emailAuth = EmailAuth.builder().email(to).code(code).build();

            emailAuthRepository.save(emailAuth);
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new GlobalException(EMAIL_SEND_FAILED);
        }
    }

    public void checkCode(String email, String code) {
        EmailAuth emailAuth = emailAuthRepository.findById(email)
            .orElseThrow(() -> new GlobalException(NOT_FOUND_EMAIL_AUTH));

        if (!code.equals(emailAuth.getCode())) {
            throw new GlobalException(INVALID_CODE);
        }

        emailAuthRepository.deleteById(email);
        EmailAuth newEmailAuth = EmailAuth.builder().email(email).code(code).isChecked(true)
            .build();

        emailAuthRepository.save(newEmailAuth);
    }

    private MimeMessage createMessage(String to, String code)
        throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();

        message.setFrom(email);
        message.addRecipients(RecipientType.TO, to);
        message.setSubject("[RecordPet] 이메일 인증");
        message.setText(authEmailLink + "?email=" + to + "&authCode=" + code);

        return message;
    }

    public EmailAuth findById(String email) {
        return emailAuthRepository.findById(email)
            .orElseThrow(() -> new GlobalException(NOT_FOUND_EMAIL_AUTH));
    }
}