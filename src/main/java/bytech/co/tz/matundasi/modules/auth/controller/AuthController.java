package bytech.co.tz.matundasi.modules.auth.controller;

import bytech.co.tz.matundasi.common.response.HttpResponse;
import bytech.co.tz.matundasi.modules.auth.payload.LoginRequest;
import bytech.co.tz.matundasi.modules.auth.payload.RefreshTokenRequest;
import bytech.co.tz.matundasi.modules.auth.response.LoginResponse;
import bytech.co.tz.matundasi.modules.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "Authentication API")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    @Operation(summary = "Log in",
            description = "Checks the username and password and returns a JWT plus a refresh token and the user's " +
                    "details, branch, roles and permissions. Send the token on later requests as " +
                    "'Authorization: Bearer <token>'; when it expires, exchange the refresh token at /auth/refresh. " +
                    "Fails with 401 for a wrong username or password, and 403 if the account is not active.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Logged in"),
            @ApiResponse(responseCode = "400", description = "Validation failed"),
            @ApiResponse(responseCode = "401", description = "Invalid username or password"),
            @ApiResponse(responseCode = "403", description = "User account is not active"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<HttpResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(
                new HttpResponse<>(true, "Logged in successfully.", HttpStatus.OK, authService.login(request)));
    }

    @PostMapping("/refresh")
    @Operation(summary = "Refresh the session",
            description = "Exchanges a refresh token for a new access token and a new refresh token (same body as " +
                    "login). Each refresh token works once. Fails with 401 if it is unknown, already used, revoked " +
                    "or expired, and 403 if the account is no longer active.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Session refreshed"),
            @ApiResponse(responseCode = "400", description = "Validation failed"),
            @ApiResponse(responseCode = "401", description = "Refresh token is invalid or expired"),
            @ApiResponse(responseCode = "403", description = "User account is not active"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<HttpResponse<LoginResponse>> refresh(@Valid @RequestBody RefreshTokenRequest request) {
        return ResponseEntity.ok(
                new HttpResponse<>(true, "Session refreshed successfully.", HttpStatus.OK, authService.refresh(request)));
    }

    @PostMapping("/logout")
    @Operation(summary = "Log out",
            description = "Revokes the refresh token so it can't be used again. Always succeeds, even for an " +
                    "unknown or already-revoked token.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Logged out"),
            @ApiResponse(responseCode = "400", description = "Validation failed"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<HttpResponse<Void>> logout(@Valid @RequestBody RefreshTokenRequest request) {
        authService.logout(request);
        return ResponseEntity.ok(new HttpResponse<>(true, "Logged out successfully.", HttpStatus.OK));
    }
}
