package com.leomac00.reststudy.controllers;

import com.leomac00.reststudy.Utils.MyMediaType;
import com.leomac00.reststudy.data.vo.v1.security.AccountCredentialsVO;
import com.leomac00.reststudy.data.vo.v1.security.TokenVO;
import com.leomac00.reststudy.services.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Authentication", description = "Endpoints for managing user authentication")
@RestController
@RequestMapping("auth")
public class AuthController {
    @Autowired
    private AuthService service;

    @PostMapping(consumes = {MyMediaType.APPLICATION_JSON,
            MyMediaType.APPLICATION_YAML,
            MyMediaType.APPLICATION_XML},
            produces = {MyMediaType.APPLICATION_JSON,
                    MyMediaType.APPLICATION_YAML,
                    MyMediaType.APPLICATION_XML})
    @Operation(
            summary = "Signs in user.",
            description = "This endpoint is used for signin in user.",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = {
                                    @Content(
                                            mediaType = MyMediaType.APPLICATION_JSON,

                                            array = @ArraySchema(schema = @Schema(implementation = TokenVO.class))
                                    )
                            }),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
            }
    )
    public ResponseEntity signin(@RequestBody AccountCredentialsVO credentialsVO) {
        var token = service.signin(credentialsVO);
        return token == null ? ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid client request!") : token;
    }

    @PutMapping(value = "/refresh/{username}")
    @Operation(
            summary = "Refresh user token.",
            description = "This endpoint is used for refreshing user's token.",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = {
                                    @Content(
                                            mediaType = MyMediaType.APPLICATION_JSON,

                                            array = @ArraySchema(schema = @Schema(implementation = TokenVO.class))
                                    )
                            }),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
            }
    )
    public ResponseEntity refreshToken(
            @PathVariable(name = "username") String username,
            @RequestHeader("Authorization") String refreshToken) {
        
        if (username.isBlank() || refreshToken.isBlank())
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid client request!");

        var token = service.refreshToken(username, refreshToken);
        return token == null ? ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid client request!") : token;
    }
}
