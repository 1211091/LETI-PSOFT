package com.example.psoftprojectg7.SubscriptionManagement.api;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "A Joke")
public class JokeDTO {
    public String joke;
}
