package session.user.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class UserSignUpReqDto {

    @Pattern(regexp = "^[A-Za-z\\d_.]+$", message = "이름은 영어, 숫자, '_', '.' 만 입력 가능합니다.")
    @Size(max = 20)
    private String name;

    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "비밀번호는 최소 8자 이상, 대문자, 소문자, 숫자, 특수문자를 포함해야 합니다.")
    @Size(max = 100)
    private String password;
}