package session.user.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class UserNameReqDto {

    @Pattern(regexp = "^[A-Za-z\\d_.]+$", message = "이름은 영어, 숫자, '_', '.' 만 입력 가능합니다.")
    @Size(max = 20)
    private String name;
}