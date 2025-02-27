package session.user.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import session.user.common.Const;
import session.user.dto.*;
import session.user.entity.User;
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
    public ResponseEntity<String> deleteUser(@PathVariable Long userId,
                                             HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        UserDetailResDto user = (UserDetailResDto) session.getAttribute(Const.SESSION_KEY);
        Boolean check = (Boolean) session.getAttribute(Const.SESSION_CHECK);

        userService.deleteUser(userId, user.getId(), check);
        session.invalidate();

        return new ResponseEntity<>("회원 탈퇴 완료", HttpStatus.OK);
    }

    // 사용자 단 건 조회
    @GetMapping("/{userId}")
    public ResponseEntity<UserDetailResDto> findUserById(@PathVariable Long userId) {
        UserDetailResDto user = userService.findUserById(userId);

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    // 비밀번호 변경
    @PatchMapping("/{userId}/password")
    public ResponseEntity<String> updatePassword(@PathVariable Long userId,
                                                 @Valid @RequestBody UserPasswordReqDto userPasswordReqDto,
                                                 HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        UserDetailResDto user = (UserDetailResDto) session.getAttribute(Const.SESSION_KEY);

        userService.updatePassword(userId, userPasswordReqDto.getOldPassword(), userPasswordReqDto.getNewPassword(), user.getId());

        return new ResponseEntity<>("비밀번호 변경 완료", HttpStatus.OK);
    }

    // 이름 변경
    @PatchMapping("/{userId}")
    public ResponseEntity<UserResDto> updateName(@PathVariable Long userId,
                                                 @Valid @RequestBody UserNameReqDto userNameReqDto,
                                                 HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        UserDetailResDto user = (UserDetailResDto) session.getAttribute(Const.SESSION_KEY);

        UserResDto userResDto = userService.updateName(userId, userNameReqDto.getName(), user.getId());

        return new ResponseEntity<>(userResDto, HttpStatus.OK);
    }

    // 비밀번호 확인
    @PostMapping("/check")
    public ResponseEntity<String> checkPassword(@Valid @RequestBody UserCheckReqDto userCheckReqDto,
                                                HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        UserDetailResDto user = (UserDetailResDto) session.getAttribute(Const.SESSION_KEY);

        userService.checkPassword(userCheckReqDto.getPassword(), user.getId());

        session.setAttribute(Const.SESSION_CHECK, true);

        return new ResponseEntity<>("비밀번호 확인 완료", HttpStatus.OK);
    }
}