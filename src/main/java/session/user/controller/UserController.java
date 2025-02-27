package session.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import session.user.dto.UserDetailResDto;
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

    // 회원탈퇴
    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);

        return new ResponseEntity<>("회원 탈퇴 완료", HttpStatus.OK);
    }

    // 사용자 단 건 조회
    @GetMapping("/{userId}")
    public ResponseEntity<UserDetailResDto> findUserById(@PathVariable Long userId) {
        UserDetailResDto user = userService.findUserById(userId);

        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}