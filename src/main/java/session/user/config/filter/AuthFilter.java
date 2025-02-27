package session.user.config.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.PatternMatchUtils;
import session.user.common.Const;
import session.user.enums.UserRole;

import java.io.IOException;

@Slf4j
public class AuthFilter implements Filter {
    private static final String[] WHITE_LIST = {"/admins", "/users", "/users/login"};

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String requestURI = httpRequest.getRequestURI();

        if (isWhiteList(requestURI)) {
            chain.doFilter(request, response);
            return;
        }

        HttpSession session = httpRequest.getSession(false);

        if (session == null || session.getAttribute(Const.SESSION_KEY) == null) {
            httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "로그인 해주세요.");
            return;
        }

        UserRole role = (UserRole) session.getAttribute(Const.SESSION_ROLE);

        if (!isAuthorized(requestURI, role)) {
            httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "권한이 없습니다.");
            return;
        }

        chain.doFilter(request, response);
    }

    private boolean isWhiteList(String requestURI) {
        return PatternMatchUtils.simpleMatch(WHITE_LIST, requestURI);
    }

    private boolean isAuthorized(String requestURI, UserRole role) {
        if (UserRole.ADMIN.equals(role)) {
            return requestURI.startsWith("/admins") || requestURI.startsWith("/users");
        } else if (UserRole.USER.equals(role)) {
            return requestURI.startsWith("/users");
        }

        return false;
    }
}