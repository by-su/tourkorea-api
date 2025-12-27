package info.tourkorea.userservice.domain.user;

import info.tourkorea.componentsecurity.security.util.SecurityContextHolderUtil;
import info.tourkorea.exception.*;
import info.tourkorea.userservice.dto.request.PasswordUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import request.UserContext;
import response.ErrorCode;

import java.util.List;
import java.util.Optional;

import static info.tourkorea.userservice.domain.user.UserDTO.*;
import static info.tourkorea.userservice.dto.request.UserRequest.SettingsRequest;
import static response.ErrorCode.DATA_LENGTH_OVER_EXCEPTION;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public List<User> getUsers(List<Long> ids) {
        return userRepository.findAllById(ids);
    }

    public void checkEmail(String email) {
        if (userRepository.existsByEmail(email))
            throw new DuplicatedException(ErrorCode.DUPLICATED_VALUE);
    }

    public User login(LoginDTO dto) {
        final User user = userRepository.findByEmail(dto.getEmail()).orElseThrow(() -> new NotFoundEntityException(ErrorCode.ENTITY_NOT_FOUND.getErrorMsg()));

        final String rawPassword = dto.getRawPassword();
        final String encodedPassword = user.getPassword();
        if (!passwordEncoder.matches(rawPassword, encodedPassword)) {
            throw new AuthorizationFailException(ErrorCode.INCORRECT_USER);
        }

        return user;
    }

    public User registerUser(RegisterDTO dto) {
        checkIfDuplicatedUsername(dto.username());

        final User user = dto.toEntity();
        userRepository.save(user);

        return user;
    }

    public User googleLogin(GoogleLoginDTO loginDTO) {

        Optional<User> byEmail = userRepository.findByEmail(loginDTO.getEmail());
        if (byEmail.isPresent()) {
            User user = byEmail.get();
            if (!user.isGoogle()) throw new AuthenticationFailException(ErrorCode.NOT_GOOGLE_LOGIN);
            return user;
        }

        return this.googleSignup(loginDTO);
    }

    private User googleSignup(GoogleLoginDTO googleLoginDto) {
        final User user = googleLoginDto.toEntity();
        return userRepository.save(user);
    }

    public User facebookLogin(FacebookLoginDTO dto) {
        User user = dto.toEntity();
        return userRepository.save(user);
    }

    public void leave(String email) {
        final User user = userRepository.findByEmail(email).orElseThrow(() -> new NotFoundEntityException(ErrorCode.ENTITY_NOT_FOUND.getErrorMsg()));
        user.leave();
    }

    public void isExist(String email) {
        userRepository.findByEmail(email).orElseThrow(() -> new NotFoundEntityException(ErrorCode.ENTITY_NOT_FOUND.getErrorMsg()));
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new NotFoundEntityException(ErrorCode.ENTITY_NOT_FOUND.getErrorMsg()));
    }

    public User findById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new NotFoundEntityException(ErrorCode.ENTITY_NOT_FOUND.getErrorMsg()));
    }

    public User updateSettings(Long userId, SettingsRequest request) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundEntityException(ErrorCode.ENTITY_NOT_FOUND.getErrorMsg()));

        if (request.description() != null && request.description().length() > 1000) throw new DataLengthOverException(DATA_LENGTH_OVER_EXCEPTION);

        user.updateSettings(request);
        return user;
    }

    public void checkIfDuplicatedUsername(String username) {
        if (userRepository.existsByUsername(username)) throw new DuplicatedEntityException(ErrorCode.DUPLICATED_VALUE.getErrorMsg());
    }

    public void updatePassword(PasswordUpdateRequest request) {
        UserContext userContext = SecurityContextHolderUtil.getUserContext().orElseThrow(() -> new AuthorizationFailException(ErrorCode.AUTHORIZATION_FAIL));
        User user = userRepository.findById(userContext.getId()).orElseThrow(() -> new NotFoundEntityException(ErrorCode.ENTITY_NOT_FOUND.getErrorMsg()));

        if (!passwordEncoder.matches(request.currentPassword(), user.getPassword())) {
            throw new PasswordNotMatchException();
        }

        user.changePassword(passwordEncoder.encode(request.newPassword()));
    }

    public void updateProfileImage(String imgPath) {
        UserContext userContext = SecurityContextHolderUtil.getUserContext().orElseThrow(() -> new AuthorizationFailException(ErrorCode.AUTHORIZATION_FAIL));
        User user = userRepository.findById(userContext.getId()).orElseThrow(() -> new NotFoundEntityException(ErrorCode.ENTITY_NOT_FOUND.getErrorMsg()));
        user.updateProfileImage(imgPath);
    }
}
