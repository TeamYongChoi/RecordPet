package teamyc.recordpet.domain.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user")
@Getter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false)
    private String nickname;
    @Column(nullable = false, unique = true)
    private String email;
    private String password;

    @Enumerated(value = EnumType.STRING)
    private Role role;
    private String userProfileImageUrl;

    @Builder
    public User(Long userId, String nickname, String email, String password, Role role,
        String userProfileImageUrl) {
        this.userId = userId;
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.role = role;
        this.userProfileImageUrl = userProfileImageUrl;
    }
}
