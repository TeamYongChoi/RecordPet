package teamyc.recordpet.global.image;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;
import teamyc.recordpet.global.image.entity.ProfileImage;
import teamyc.recordpet.global.image.entity.Type;

@RepositoryDefinition(domainClass = ProfileImage.class, idClass = Long.class)
public interface ProfileImageRepository {

    @Query(value = "select p from ProfileImage p where p.isBasic = true AND p.type = :type")
    ProfileImage findBasicImage(@Param("type") Type type);

    ProfileImage save(ProfileImage profileImage);
}
