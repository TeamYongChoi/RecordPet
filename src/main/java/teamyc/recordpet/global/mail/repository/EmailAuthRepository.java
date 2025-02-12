package teamyc.recordpet.global.mail.repository;

import java.util.Optional;
import org.springframework.data.repository.RepositoryDefinition;
import teamyc.recordpet.global.mail.entity.EmailAuth;

@RepositoryDefinition(domainClass = EmailAuth.class, idClass = String.class)
public interface EmailAuthRepository {

    Optional<EmailAuth> findById(String email);

    void deleteById(String email);

    boolean existsById(String email);

    void save(EmailAuth emailAuth);
}