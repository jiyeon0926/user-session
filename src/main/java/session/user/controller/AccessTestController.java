package session.user.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccessTestController {

    // 관리자만 접근 허용
    @GetMapping("/admins/access")
    public ResponseEntity<String> adminAccess() {
        return new ResponseEntity<>("관리자만 접근할 수 있습니다.", HttpStatus.OK);
    }
}