package com.example.jwtauth.config;

import io.jsonwebtoken.Jwts;
import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

public class JwtAuthenticationFilter extends BasicAuthenticationFilter {

  public JwtAuthenticationFilter(AuthenticationManager authManager) {

    super(authManager);
  }

  @Override
  protected void doFilterInternal(HttpServletRequest req,
      HttpServletResponse res, FilterChain chain) throws IOException, ServletException {

    String header = req.getHeader("Authorization");

    if (header == null || !header.startsWith("Bearer ")) {
      chain.doFilter(req, res);
      return;
    }

    Authentication authentication = getAuthentication(req);

    SecurityContextHolder.getContext().setAuthentication(authentication);
    chain.doFilter(req, res);
  }

  private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {

    String token = request.getHeader("Authorization");

    if (token == null) {
      return null;
    }

    // parse the token.
    String user = Jwts.parser()
        .setSigningKey("MyJwtSecret")
        .parseClaimsJws(token.replace("Bearer ", ""))
        .getBody()
        .getSubject();

    if (user != null) {
      return new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());
    }

    return null;
  }
}
