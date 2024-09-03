//package com.tjtech.authen.security;
//
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.SignatureAlgorithm;
//import io.jsonwebtoken.security.Keys;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//import jakarta.servlet.http.HttpServletRequest;
//import java.security.Key;
//import java.util.Date;
//
//@Service
//public class JWTService {
//
//    @Value("${jwt.secret}")
//    private String secretKey;
//
//    @Value("${jwt.expiration}")
//    private long expirationTime;
//
//    private final UserDetailsService userDetailsService;
//
//    public JWTService(UserDetailsService userDetailsService) {
//        this.userDetailsService = userDetailsService;
//    }
//
//    // Generate a JWT token
//    public String generateToken(UserDetails userDetails) {
//        Claims claims = Jwts.claims().setSubject(userDetails.getUsername());
//        claims.put("roles", userDetails.getAuthorities());
//
//        Date now = new Date();
//        Date expirationDate = new Date(now.getTime() + expirationTime);
//
//        Key key = Keys.hmacShaKeyFor(secretKey.getBytes());
//
//        return Jwts.builder()
//                .setClaims(claims)
//                .setIssuedAt(now)
//                .setExpiration(expirationDate)
//                .signWith(key, SignatureAlgorithm.HS256)
//                .compact();
//    }
//
//    // Resolve JWT token from request header
//    public String resolveToken(HttpServletRequest request) {
//        String bearerToken = request.getHeader("Authorization");
//        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
//            return bearerToken.substring(7);
//        }
//        return null;
//    }
//
//    // Validate JWT token
//    public boolean validateToken(String token) {
//        try {
//            Key key = Keys.hmacShaKeyFor(secretKey.getBytes());
//            Jwts.parserBuilder()
//                    .setSigningKey(key)
//                    .build()
//                    .parseClaimsJws(token);
//            return true;
//        } catch (Exception e) {
//            // Handle different exceptions as needed
//            return false;
//        }
//    }
//
//    // Get Authentication object from token
//    public Authentication getAuthentication(String token) {
//        Claims claims = Jwts.parserBuilder()
//                .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
//                .build()
//                .parseClaimsJws(token)
//                .getBody();
//
//        String username = claims.getSubject();
//        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
//
//        if (userDetails != null) {
//            return new UsernamePasswordAuthenticationToken(userDetails, token, userDetails.getAuthorities());
//        }
//        return null;
//    }
//}
