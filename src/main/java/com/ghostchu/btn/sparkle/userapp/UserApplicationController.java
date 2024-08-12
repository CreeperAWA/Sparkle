package com.ghostchu.btn.sparkle.userapp;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import com.ghostchu.btn.sparkle.exception.TooManyUserApplicationException;
import com.ghostchu.btn.sparkle.exception.UserApplicationNotFoundException;
import com.ghostchu.btn.sparkle.exception.UserNotFoundException;
import com.ghostchu.btn.sparkle.user.UserDto;
import com.ghostchu.btn.sparkle.user.UserService;
import com.ghostchu.btn.sparkle.wrapper.StdResp;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api")
public class UserApplicationController {
    private final UserApplicationService userApplicationService;
    private final UserService userService;

    public UserApplicationController(UserApplicationService userApplicationService, UserService userService) {
        this.userApplicationService = userApplicationService;
        this.userService = userService;
    }

    @SaCheckLogin
    @GetMapping("/userapp")
    public StdResp<List<UserApplicationDto>> getUserApplications() throws UserNotFoundException {
        return new StdResp<>(true, null, userApplicationService.getUserApplications(userService.exchangeUserFromUserDto((UserDto) StpUtil.getLoginId()))
                .stream()
                .map(userApplicationService::toDto).toList());
    }

    @SaCheckLogin
    @GetMapping("/userapp/{appId}")
    public StdResp<UserApplicationDto> getUserApplication(@PathVariable("appId") String appId) throws UserApplicationNotFoundException {
        var optional = userApplicationService.getUserApplication(appId);
        if (optional.isEmpty()) {
            throw new UserApplicationNotFoundException();
        }
        var usrApp = optional.get();
        if (!Objects.equals(usrApp.getUser().getId(), ((UserDto) StpUtil.getLoginId()).getId())) {
            StpUtil.checkPermission("userapp.read-other-app");
        }
        return new StdResp<>(true, null, userApplicationService.toDto(usrApp));
    }

    @SaCheckLogin
    @PostMapping("/userapp/{appId}/resetAppSecret")
    public StdResp<UserApplicationVerboseDto> resetUserApplicationAppSecret(@PathVariable("appId") String appId) throws UserApplicationNotFoundException {
        var optional = userApplicationService.getUserApplication(appId);
        if (optional.isEmpty()) {
            throw new UserApplicationNotFoundException();
        }
        var usrApp = optional.get();
        if (!Objects.equals(usrApp.getUser().getId(), ((UserDto) StpUtil.getLoginId()).getId())) {
            StpUtil.checkPermission("userapp.reset-other-appsecret");
        }
        return new StdResp<>(true, null, userApplicationService.toVerboseDto(userApplicationService.resetUserApplicationSecret(usrApp.getId())));
    }

    @SaCheckLogin
    @PatchMapping("/userapp/{appId}")
    public StdResp<UserApplicationDto> editUserApplication(@PathVariable("appId") String appId, @RequestBody UserApplicationEditRequest req) throws UserApplicationNotFoundException {
        var optional = userApplicationService.getUserApplication(appId);
        if (optional.isEmpty()) {
            throw new UserApplicationNotFoundException();
        }
        var usrApp = optional.get();
        if (!Objects.equals(usrApp.getUser().getId(), ((UserDto) StpUtil.getLoginId()).getId())) {
            StpUtil.checkPermission("userapp.edit-other-app");
        }
        return new StdResp<>(true, null, userApplicationService.toDto(userApplicationService.editUserApplicationComment(usrApp.getId(), req.getComment())));
    }


    @SaCheckLogin
    @DeleteMapping("/userapp/{appId}")
    public StdResp<Void> deleteUserApplication(@PathVariable("appId") String appId) throws UserApplicationNotFoundException {
        var optional = userApplicationService.getUserApplication(appId);
        if (optional.isEmpty()) {
            throw new UserApplicationNotFoundException();
        }
        var usrApp = optional.get();
        if (!Objects.equals(usrApp.getUser().getId(), ((UserDto) StpUtil.getLoginId()).getId())) {
            StpUtil.checkPermission("userapp.delete-other-app");
        }
        return new StdResp<>(true, "用户应用程序删除成功", null);
    }

    @SaCheckLogin
    @PutMapping("/userapp")
    public StdResp<UserApplicationVerboseDto> createUserApplication(@RequestBody UserApplicationCreateRequest req) throws UserNotFoundException, TooManyUserApplicationException {
        var user = userService.exchangeUserFromUserDto((UserDto) StpUtil.getLoginId());
        var usrApp = userApplicationService.generateUserApplicationForUser(user, req.getComment(), new Timestamp(System.currentTimeMillis()));
        return new StdResp<>(true, null, userApplicationService.toVerboseDto(usrApp));
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserApplicationCreateRequest {
        private String comment;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserApplicationEditRequest {
        private String comment;
    }
}
