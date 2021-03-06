package com.example.todospring.security;

import com.example.todospring.model.UserEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
//import io.jsonwebtoken.io.Decoders;
//import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenProvider {

    private static final String SECRET_KEY = "NMA8JPctFuna59f5";
    //private static final SecretKey key = Key.hmacShaKeyFor(Decoders.BASE64.decode(SECRET_KEY));

    public String create(UserEntity userEntity) {
        // 기한은 토큰 생성일로부터 1일로 설정
        Date expiryDate = Date.from(
                Instant.now()
                        .plus(1, ChronoUnit.DAYS)
        );

        /*
        원래라면 직접 JSON형식으로 만들어야 했겠지만, 라이브러리의 builder()를 통해 간편화딤
        {   //  header
            "alg":"HS512"
        }.
        {   // payload
            "sub" : "40288093784915d201784916a40c0001",
            "iss" : "demo app",
            "iat" : 1595733657,
            "exp" : 1596597657
        }.
        // SECRET_KEY를 이용해 서명한 부분
        Nn4d1MOVLZg79sfFACTIpCPKqWmpZMZQsbNrXdJJNWkRv50_17bPLQPwhMobT4vBOG6Q3JYjhDrKF1BSaUxZ0g
            */
        return Jwts.builder()
                // header에 들어갈 내용 및 서명을 하기 위한 SECRET_KEY
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                //payload에 들어갈 내용
                .setSubject(userEntity.getId()) // sub
                .setIssuer("demo app") // iss
                .setIssuedAt(new Date()) // iat
                .setExpiration(expiryDate) // exp
                .compact();
    }

    public String validateAndGetUserId(String token) {
        // parseClaimsJws 메소드가 Base64로 디코딩 및 파싱
        // 헤더와 페이로드를 setSigningKey로 넘어온 시크릿을 이용해 서명한 후 token의 서명과 비교
        // 위조되지 않았다면 페이로드(Claims)리턴, 위조라면 예외를 날림
        // 그중 우리는 userId가 필요함으로 getBody를 부른다.
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject(); // subject는 우리가 원하는 사용자의 아이디를 뜻한다.
    }
}
