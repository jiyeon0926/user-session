package session.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import session.user.dto.UserResDto;
import session.user.dto.UserSignUpReqDto;
import session.user.service.UserService;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // 사용자 회원가입
    @PostMapping
    public ResponseEntity<UserResDto> userSignup(@Valid @RequestBody UserSignUpReqDto userSignUpReqDto) {
        UserResDto signup = userService.userSignup(userSignUpReqDto.getName(), userSignUpReqDto.getPassword());

        return new ResponseEntity<>(signup, HttpStatus.CREATED);
    }
}