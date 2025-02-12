package teamyc.recordpet.domain.user.repository;

import java.util.Optional;
import org.springframework.data.repository.RepositoryDefinition;
import teamyc.recordpet.domain.user.entity.User;

@RepositoryDefinition(domainClass = User.class, idClass = Long.class)
public interface UserRepository {

    User save(User user);

    boolean existsByEmail(String email);

    boolean existsByNickname(String nickname);

    Optional<User> findByUserId(Long userId);
}