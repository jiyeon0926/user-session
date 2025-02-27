package session.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import session.user.config.util.PasswordEncoder;
import session.user.dto.UserResDto;
import session.user.entity.User;
import session.user.enums.UserRole;
import session.user.repository.UserRepository;

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
}