package info.tourkorea.userservice.domain.email;


import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailTokenRepository extends JpaRepository<EmailToken, Long> {
    Optional<EmailToken> findEmailTokenByLink(String link);
}
