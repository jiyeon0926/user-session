package session.user.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import session.user.enums.UserRole;

@Getter
@RequiredArgsConstructor
public class UserDetailResDto {

    private final Long id;
    private final String name;
    private final UserRole role;
}
