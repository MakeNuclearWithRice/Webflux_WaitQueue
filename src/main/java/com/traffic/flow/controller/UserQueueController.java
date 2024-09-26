package com.traffic.flow.controller;

import com.traffic.flow.dto.AllowUserResponse;
import com.traffic.flow.dto.AllowedUserResponse;
import com.traffic.flow.dto.RankNumberResponse;
import com.traffic.flow.dto.RegisterUserResponse;
import com.traffic.flow.service.UserQueueService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.security.NoSuchAlgorithmException;
import java.time.Duration;

@RestController
@RequestMapping("/api/v1/queue")
@RequiredArgsConstructor
public class UserQueueController {

    private final UserQueueService userQueueService;


    @PostMapping("")
    public Mono<RegisterUserResponse> registerUser(@RequestParam(name ="queue", defaultValue = "default") String queue,
                                                   @RequestParam(name = "user_id") Long userId){
        return userQueueService.registerWaitQueue(queue, userId)
                .map(RegisterUserResponse::new);
    }

    @PostMapping("/allow")
    public Mono<AllowUserResponse> allowUser(@RequestParam(name ="queue", defaultValue = "default") String queue,
                                             @RequestParam(name = "count") Long count){
        return userQueueService.allowUser(queue, count)
                .map(allowed -> new AllowUserResponse(count, allowed));
    }

    @GetMapping("/allowed")
    public Mono<AllowedUserResponse> isAllowedUser(@RequestParam(name ="queue", defaultValue = "default") String queue,
                                                   @RequestParam(name = "user_id") Long userId){
        return userQueueService.isAllowed(queue, userId)
                .map(AllowedUserResponse::new);

    }

    @GetMapping("/rank")
    public Mono<RankNumberResponse> getUserRank(@RequestParam(name ="queue", defaultValue = "default") String queue,
                                                @RequestParam(name = "user_id") Long userId){
        return userQueueService.getRank(queue, userId)
                .map(RankNumberResponse::new);

    }

    @GetMapping("/touch")
    Mono<?> touch(@RequestParam(name ="queue", defaultValue = "default") String queue,
                  @RequestParam(name = "user_id") Long userId,
                  ServerWebExchange exchange){
        return Mono.defer(()->userQueueService.generateToken(queue, userId))
                .map(token -> {
                    exchange.getResponse().addCookie(
                            ResponseCookie
                                    .from("user-queue-%s-token".formatted(queue), token)
                                    .maxAge(Duration.ofSeconds(300))
                                    .path("/")
                                    .build()
                    );
                    return token;
                });
    }

}
