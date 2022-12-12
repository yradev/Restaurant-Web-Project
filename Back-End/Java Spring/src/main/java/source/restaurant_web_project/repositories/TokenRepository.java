package source.restaurant_web_project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import source.restaurant_web_project.models.entity.Token;

@Repository
public interface TokenRepository extends JpaRepository<Token,Long> {
    Token findPasswordResetTokenByEmailAndToken(String email, String token);

    Token findPasswordResetTokenByEmail(String email);
}
