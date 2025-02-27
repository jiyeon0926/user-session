package session.user.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import session.user.common.Const;
import session.user.dto.LoginReqDto;
import session.user.dto.LoginResDto;
import session.user.dto.UserDetailResDto;
import session.user.service.UserService;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class LoginController {

    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody LoginReqDto loginReqDto,
                                        HttpServletRequest request) {
        LoginResDto loginResDto = userService.login(loginReqDto.getName(), loginReqDto.getPassword());
        Long userId = loginResDto.getId();
        UserDetailResDto loginUser = userService.findUserById(userId);

        HttpSession session = request.getSession();
        session.setAttribute(Const.SESSION_KEY, loginUser);
        session.setAttribute(Const.SESSION_ROLE, loginUser.getRole());

        return new ResponseEntity<>("로그인 완료", HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if(session != null) {
            session.invalidate();
        }

        return new ResponseEntity<>("로그아웃 완료", HttpStatus.OK);
    }
}