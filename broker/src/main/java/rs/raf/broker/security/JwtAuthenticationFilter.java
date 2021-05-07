package rs.raf.broker.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import rs.raf.broker.domain.User;
import rs.raf.broker.persistance.UserRepository;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final String secret;
    private final Long tokenExpirationTime;
    private final UserRepository userRepository;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager,
                                   String secret,
                                   Long tokenExpirationTime,
                                   UserRepository userRepository) {

        this.setFilterProcessesUrl("/api/users/login");
        this.authenticationManager = authenticationManager;
        this.secret = secret;
        this.tokenExpirationTime = tokenExpirationTime;
        this.userRepository = userRepository;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        try {
            User user = new ObjectMapper().readValue(request.getInputStream(), User.class);
            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {

        List<String> roles = authResult.getAuthorities().stream().map(role -> role.getAuthority()).collect(Collectors.toList());
        String[] roleArray =roles.toArray(new String[0]);
        Optional<User> user = userRepository.findById(((User) authResult.getPrincipal()).getUsername());
        if (!user.isPresent()) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.flushBuffer();
            return;
        }
        String teamName = user.get().getTeam() != null ? user.get().getTeam().getName() : "";
        String token = JWT.create()
                .withSubject(((User) authResult.getPrincipal()).getUsername())
                .withExpiresAt(
                        new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(tokenExpirationTime)))
                .withArrayClaim("roles", roleArray)
                .withClaim("teamName", teamName)
                .sign(Algorithm.HMAC512(secret));
        String header = "Authorization";
        String prefix = "Bearer ";
        response.addHeader("Access-Control-Expose-Headers", header);
        response.addHeader(header, String.format("%s%s", prefix, token));
        response.setStatus(HttpServletResponse.SC_OK);
        response.flushBuffer();
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.flushBuffer();
    }
}
