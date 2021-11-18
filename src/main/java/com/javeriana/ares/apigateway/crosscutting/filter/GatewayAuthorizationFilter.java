package com.javeriana.ares.apigateway.crosscutting.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.javeriana.ares.apigateway.crosscutting.constants.Constants;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.List;
import java.util.Objects;

@Component
public class GatewayAuthorizationFilter extends AbstractGatewayFilterFactory<GatewayAuthorizationFilter.Config> {

    public GatewayAuthorizationFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            List<String> accessToken = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION);
            if (Objects.nonNull(accessToken) && !accessToken.isEmpty() && accessToken.get(0).startsWith(Constants.BEARER)) {
                try {
                    String token = accessToken.get(0).substring(Constants.BEARER.length());
                    Algorithm algorithm = Algorithm.HMAC256(Constants.SECRET.getBytes());
                    JWTVerifier verifier = JWT.require(algorithm).build();
                    DecodedJWT jwt = verifier.verify(token);
                    String username = jwt.getSubject();
                    String rol = jwt.getClaim(Constants.ROL).asString();
                    exchange.getRequest()
                            .mutate()
                            .header(Constants.X_AUTH_TOKEN, Base64.getEncoder()
                                    .encodeToString((username + ";" + rol).getBytes()));
                } catch (Exception e) {
                    exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                }
            }
            return chain.filter(exchange);
        });
    }

    public static class Config {
    }
}
