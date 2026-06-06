package org.example.chatapplication.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserRequest {

    @NotBlank(message = "username cannot be empty or null")
    private String username;

    @NotBlank(message = "password cannot be empty or null")
    private String password;

}
