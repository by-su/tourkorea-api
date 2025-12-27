package info.tourkorea.userservice.domain.email;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import response.ErrorCode;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class EmailTokenService {

    private final EmailTokenRepository emailTokenRepository;

    public EmailToken findByLink(String link) {
        return emailTokenRepository.findEmailTokenByLink(link).orElseThrow(() -> new EntityNotFoundException(ErrorCode.ENTITY_NOT_FOUND.getErrorMsg()));
    }

    @Transactional
    public EmailToken save(EmailToken emailToken) {
        return emailTokenRepository.save(emailToken);
    }
}
