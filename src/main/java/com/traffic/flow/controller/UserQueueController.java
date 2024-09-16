package com.traffic.flow.controller;

import com.traffic.flow.dto.RegisterUserResponse;
import com.traffic.flow.service.UserQueueService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

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

}
