# JWT token Authentication with Symmetric Key

How to configure authentication in Spring boot application using JWT token and Symmetric key.
The same HS256 key will be used for JWT creation and verification.

## Run and test the App

(1) Run API `JwtApplication.java`

(2) Generate JWT `JwtTokenGenerator.test_token()`

(3) Call the API
```
curl GET http://localhost:8080/ -H "Authorization: Bearer <jwt-token>
```
You should see as response "Hello JWT!"

```
GET http://localhost:8080/details
Authorization:Bearer <jwt-token>
```

You should see the Principal's data in the response body.

[Http calls here](./REST/api.http)


## OAuth Security based in JWT Symmetric keys

```
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private JwtService jwtService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                ...
                .oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt)
                ...
                .build();
    }
    
    //JWT verification using the secret key
    @Bean
    JwtDecoder jwtDecoder() {
        return token -> jwtService.verifyWithSymmetricKey(token);
    }
}

public class JwtService {

    private final String publicKey = "78f06caa0f51406c24ab503faaee375af166c62cf1e48fc5a220a68b19f26e5e";
    private Algorithm verifyAlgorithm = Algorithm.HMAC256(publicKey.getBytes());

    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
    private final String privateKey = "3f48fdc84a8a4b9fed8493986c61379d1e79931fac29a2635e5e5b8cced8dcf1";
    private byte[] privateKeyBytes = DatatypeConverter.parseBase64Binary(privateKey);
    private Key signingKeyPrivate = new SecretKeySpec(privateKeyBytes, signatureAlgorithm.getJcaName());
    
    
     public Jwt verifyWithSymmetricKey(String token) {
            Claims claims = Jwts.parser()
                    .setSigningKey(DatatypeConverter.parseBase64Binary(privateKey))
                    .parseClaimsJws(token).getBody();
            Jwt jwt = new Jwt(claims...);
            return jwt;
        }
    }
```

## Links
* [jwts with java](https://developer.okta.com/blog/2018/10/31/jwts-with-java)

