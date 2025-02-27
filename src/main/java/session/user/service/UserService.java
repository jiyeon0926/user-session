package session.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import session.user.config.util.PasswordEncoder;
import session.user.dto.LoginResDto;
import session.user.dto.UserDetailResDto;
import session.user.dto.UserResDto;
import session.user.entity.User;
import session.user.enums.UserRole;
import session.user.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserResDto userSignup(String name, String password) {
        userRepository.findUserByName(name)
                .ifPresent(user -> {
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "이미 있는 이름입니다.");
                });

        String encodedPassword = passwordEncoder.encode(password);
        User user = new User(name, encodedPassword, UserRole.USER);
        User savedUser = userRepository.save(user);

        return new UserResDto(
                savedUser.getId(),
                savedUser.getName(),
                savedUser.getRole().name(),
                savedUser.getCreatedAt(),
                savedUser.getModifiedAt()
        );
    }

    @Transactional
    public void deleteUser(Long userId) {
        User user = userRepository.findUserById(userId);
        userRepository.delete(user);
    }

    @Transactional(readOnly = true)
    public UserDetailResDto findUserById(Long userId) {
        User user = userRepository.findUserByIdAndIsDeleted(userId, false)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자가 존재하지 않습니다."));

        return new UserDetailResDto(
                user.getId(),
                user.getName(),
                user.getRole()
        );
    }

    @Transactional
    public void updatePassword(Long userId, String oldPassword, String newPassword) {
        User user = userRepository.findUserByIdAndIsDeleted(userId, false)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자가 존재하지 않습니다."));

        boolean matches = passwordEncoder.matches(oldPassword, user.getPassword());

        if (!matches) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다.");
        }

        if (newPassword.equals(oldPassword)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "동일한 비밀번호를 사용할 수 없습니다.");
        }

        String encodedNewPassword = passwordEncoder.encode(newPassword);
        user.updatePassword(encodedNewPassword);
    }

    @Transactional
    public UserResDto updateName(Long userId, String name) {
        User user = userRepository.findUserByIdAndIsDeleted(userId, false)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자가 존재하지 않습니다."));

        userRepository.findUserByName(name)
                .ifPresent(userName -> {
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "이미 있는 이름입니다.");
                });

        if (name.equals(user.getName())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "이미 동일한 이름입니다.");
        }

        user.updateName(name);

        return new UserResDto(
                user.getId(),
                user.getName(),
                user.getRole().name(),
                user.getCreatedAt(),
                user.getModifiedAt()
        );
    }

    public LoginResDto login(String name, String password) {
        User user = userRepository.findUserByName(name)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 사용자입니다."));

        boolean matches = passwordEncoder.matches(password, user.getPassword());

        if (!matches) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다.");
        }

        return new LoginResDto(user.getId());
    }
}