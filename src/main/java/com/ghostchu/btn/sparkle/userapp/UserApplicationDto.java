package com.ghostchu.btn.sparkle.userapp;

import com.ghostchu.btn.sparkle.user.UserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserApplicationDto {
    private Long id;
    private String appId;
    private String comment;
    private Long createdAt;
    private UserDto user;
}
