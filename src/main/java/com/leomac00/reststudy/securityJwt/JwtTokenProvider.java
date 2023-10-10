package com.leomac00.reststudy.securityJwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.leomac00.reststudy.data.vo.v1.security.TokenVO;
import com.leomac00.reststudy.exceptions.InvalidJWTAuthenticationException;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Base64;
import java.util.List;
import java.util.Date;


@Service
public class JwtTokenProvider {
    // @Value will grab the value from this context's application.yml
    @Value("${security.jwt.token.secret-key:secret}")
    private String secretKey = "secret";
    @Value("${security.jwt.token.expire-length:3600000}")
    private long expireLength = 3600000; //1h in ms

    @Autowired
    private UserDetailsService userDetailsService;

    private Algorithm algorithm = null;
    private final String BEARER = "Bearer ";

    // Post construct does somethign after spring initializes the dependencies
    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
        algorithm = Algorithm.HMAC256(secretKey);
    }

    public TokenVO createAccessToken(String username, List<String> roles) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + expireLength);

        var accessToken = getAccessToken(username, roles, now, expiry);
        var refreshToken = getRefreshToken(username, roles, now);

        return new TokenVO(username, true, now, expiry, accessToken, refreshToken);
    }

    public TokenVO refreshToken(String token) {
        // Basically I'll get the current refreshToken, get the roles and user from it and create another access token with theese props
        if (token.contains(BEARER)) token = token.substring(BEARER.length());

        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = verifier.verify(token);

        var username = decodedJWT.getSubject();
        var roles = decodedJWT.getClaim("roles").asList(String.class);

        return createAccessToken(username, roles);
    }

    private String getAccessToken(String username, List<String> roles, Date now, Date expiry) {
        String issuerUrl = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .build()
                .toUriString();

        return JWT.create()
                .withClaim("roles", roles)
                .withIssuedAt(now)
                .withExpiresAt(expiry)
                .withSubject(username)
                .withIssuer(issuerUrl)
                .sign(algorithm)
                .strip();
    }

    private String getRefreshToken(String username, List<String> roles, Date now) {
        Date expiry = new Date(now.getTime() + (expireLength * 3));
        return JWT.create()
                .withClaim("roles", roles)
                .withIssuedAt(now)
                .withExpiresAt(expiry)
                .withSubject(username)
                .sign(algorithm)
                .strip();
    }

    public Authentication getAuthentication(String token) {
        DecodedJWT decodedJWT = decodedToken(token);
        UserDetails userDetails = this.userDetailsService
                .loadUserByUsername(decodedJWT.getSubject());
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    private DecodedJWT decodedToken(String token) {
        var alg = Algorithm.HMAC256(secretKey.getBytes());
        JWTVerifier verifier = JWT.require(alg).build();
        DecodedJWT decodedJWT = verifier.verify(token);

        return decodedJWT;
    }

    public String resolveToken(HttpServletRequest req) {
        String bearerToken = req.getHeader("Authorization");

        if (bearerToken != null && bearerToken.startsWith(BEARER)) {
            return bearerToken.substring(BEARER.length());
        }
        return null;
    }

    public boolean validateToken(String token) {
        DecodedJWT decodedJWT = decodedToken(token);
        try {
            if (decodedJWT.getExpiresAt().before(new Date())) {
                return false;
            }
            return true;
        } catch (Exception e) {
            throw new InvalidJWTAuthenticationException("Expired or invalid JWT token!");
        }
    }
}
