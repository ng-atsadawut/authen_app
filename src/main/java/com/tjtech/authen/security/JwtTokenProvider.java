package com.tjtech.authen.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider {

    private final String SECRET_KEY = "your_secret_key"; // Replace with your actual secret key

    // Generate JWT token
    public String generateToken(Authentication authentication, String role) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return Jwts.builder()
                .claim("roles", role)  // Add roles to the token
                .setSubject(userDetails.getUsername())  // กำหนดชื่อผู้ใช้เป็น subject
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // หมดอายุใน 10 ชั่วโมง
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }

    // Extract JWT token from request header
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7); // Remove "Bearer " prefix
        }
        return null;
    }

    public List<GrantedAuthority> getAuthorities(String token) {
        Claims claims = getClaims(token);
        String rolesString = claims.get("roles", String.class); // ดึง roles เป็นสตริง

        // แปลงสตริง roles เป็นลิสต์ของสตริง
        List<String> roles = Arrays.asList(rolesString.split(","));

        return roles.stream()
                .map(SimpleGrantedAuthority::new) // แปลงแต่ละ role เป็น GrantedAuthority
                .collect(Collectors.toList());
    }

    // Extract Claims from JWT token
    private Claims getClaims(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }

    // Get Authentication from JWT token
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = User.withUsername(getClaims(token).getSubject())
                .authorities(getAuthorities(token))
                .password("") // Password is not used here
                .build();

        return new UsernamePasswordAuthenticationToken(userDetails.getUsername(), null, userDetails.getAuthorities());
    }

    // Validate JWT token
    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false; // Token is invalid or expired
        }
    }
}
