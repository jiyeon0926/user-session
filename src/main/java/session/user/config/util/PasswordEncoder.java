package session.user.config.util;

import at.favre.lib.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;

@Component
public class PasswordEncoder {

    /**
     * @param rawPassword 요청 받은 비밀번호
     * @return 비밀번호 암호화
     */
    public String encode(String rawPassword) {
        return BCrypt.withDefaults().hashToString(BCrypt.MIN_COST, rawPassword.toCharArray());
    }

    /**
     * @param rawPassword 요청 받은 비밀번호
     * @param encodedPassword 암호화된 비밀번호
     * @return rawPassword와 encodedPassword를 비교하여 일치하는지 판단
     */
    public boolean matches(String rawPassword, String encodedPassword) {
        BCrypt.Result result = BCrypt.verifyer().verify(rawPassword.toCharArray(), encodedPassword);
        return result.verified;
    }
}