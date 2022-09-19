package com.edu.ulab.app.web.handler;

import com.edu.ulab.app.exception.AppException;
import com.edu.ulab.app.exception.BadRequestException;
import com.edu.ulab.app.exception.NotFoundException;
import com.edu.ulab.app.exception.NullPointerException;
import com.edu.ulab.app.web.response.BaseWebResponse;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<BaseWebResponse> handleNotFoundExceptionException(@NonNull final NotFoundException exc) {
        return responseException(exc, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({BadRequestException.class})
    public ResponseEntity<BaseWebResponse> handleBadRequestExceptionException(@NonNull final BadRequestException exc) {
        return responseException(exc, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<BaseWebResponse> handleNullPointerExceptionException(@NonNull final NullPointerException exc) {
        return responseException(exc, HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<BaseWebResponse> responseException(AppException exc, HttpStatus httpStatus) {
        log.error(exc.getMessage());
        return ResponseEntity.status(httpStatus)
                .body(new BaseWebResponse(createErrorMessage(exc)));
    }

    private String createErrorMessage(Exception exception) {
        final String message = exception.getMessage();
        log.error(ExceptionHandlerUtils.buildErrorMessage(exception));
        return message;
    }
}
