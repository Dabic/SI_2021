package rs.raf.broker.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import rs.raf.broker.domain.User;
import rs.raf.broker.exceptions.CustomHttpException;
import rs.raf.broker.persistance.UserRepository;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

public class JwtAuthorizationHeader extends BasicAuthenticationFilter {

    private final UserRepository userRepository;
    private final String header = "Authorization";
    private final String prefix = "Bearer ";
    private final String secret;

    public JwtAuthorizationHeader(
            AuthenticationManager authenticationManager,
            UserRepository userRepository,
            String secret) {
        super(authenticationManager);
        this.userRepository = userRepository;
        this.secret = secret;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        String authorizationHeader = request.getHeader(header);
        if (authorizationHeader == null || authorizationHeader.isEmpty() || !authorizationHeader.startsWith(prefix)) {
            chain.doFilter(request, response);
            return;
        }
        SecurityContextHolder.getContext().setAuthentication(getAuthentication(request));
        chain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {

        String token = request.getHeader(header);
        if (token == null) {
            return null;
        }
        String username;
        try {
            username = JWT.require(Algorithm.HMAC512(secret))
                    .build()
                    .verify(token.replace(prefix, ""))
                    .getSubject();

            if (username == null) {
                return null;
            }

            Optional<User> user = userRepository.findById(username);
            if (user.isPresent()) {
                return new UsernamePasswordAuthenticationToken(
                        user.get().getUsername(),
                        user.get().getPassword(),
                        user.get().getAuthorities());
            }
        } catch (JWTDecodeException e) {
            return null;
        }
        return null;
    }
}
