package com.edu.ulab.app.web;

import com.edu.ulab.app.facade.UserDataFacade;
import com.edu.ulab.app.web.constant.WebConstant;
import com.edu.ulab.app.web.request.create.UserBookRequest;
import com.edu.ulab.app.web.request.update.UserBookUpdateRequest;
import com.edu.ulab.app.web.response.BaseWebResponse;
import com.edu.ulab.app.web.response.UserBookResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Pattern;

import static com.edu.ulab.app.web.constant.WebConstant.REQUEST_ID_PATTERN;
import static com.edu.ulab.app.web.constant.WebConstant.RQID;

@Slf4j
@RestController
@RequestMapping(value = WebConstant.VERSION_URL + "/users",
        produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {
    private final UserDataFacade userDataFacade;

    public UserController(UserDataFacade userDataFacade) {
        this.userDataFacade = userDataFacade;
    }

    @PostMapping()
    @Operation(summary = "Create user and his books row.",
            responses = {
                    @ApiResponse(description = "Created user with his books",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = UserBookResponse.class))),
                    @ApiResponse(responseCode = "400", description = "The request is malformed",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = BaseWebResponse.class)))
            })
    public UserBookResponse createUserWithBooks(@RequestBody UserBookRequest request,
                                                @RequestHeader(RQID) @Pattern(regexp = REQUEST_ID_PATTERN) final String requestId) {
        UserBookResponse response = userDataFacade.createUserWithBooks(request);
        log.info("Response with created user and his books: {}", response);
        return response;
    }

    @PutMapping()
    @Operation(summary = "Update user and his books by user index and books indexes.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Updated user with his books",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = UserBookResponse.class))),
                    @ApiResponse(responseCode = "400", description = "The request is malformed",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = BaseWebResponse.class))),
                    @ApiResponse(responseCode = "404", description = "The resource you try reach is not found",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = BaseWebResponse.class)))
            })
    public UserBookResponse updateUserWithBooks(@RequestBody UserBookUpdateRequest request) {
        UserBookResponse response = userDataFacade.updateUserWithBooks(request);
        log.info("Response with updated user and his books: {}", response);
        return response;
    }

    @GetMapping(value = "/{userId}")
    @Operation(summary = "Get user and his book by user index.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User with his books by user index",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = UserBookResponse.class))),
                    @ApiResponse(responseCode = "400", description = "The request is malformed",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = BaseWebResponse.class))),
                    @ApiResponse(responseCode = "404", description = "The resource you try reach is not found",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = BaseWebResponse.class)))
            })
    public UserBookResponse getUserWithBooks(@PathVariable Long userId) {
        UserBookResponse response = userDataFacade.getUserWithBooks(userId);
        log.info("Response with user and his books: {}", response);
        return response;
    }

    @DeleteMapping(value = "/{userId}")
    @Operation(summary = "Delete user and his book by user index.",
            responses = {
                    @ApiResponse(responseCode = "400", description = "The request is malformed",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = BaseWebResponse.class)))
            })
    public void deleteUserWithBooks(@PathVariable Long userId) {
        userDataFacade.deleteUserWithBooks(userId);
    }
}
