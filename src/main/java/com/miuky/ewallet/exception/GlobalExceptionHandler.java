package com.miuky.ewallet.exception;

import com.miuky.ewallet.dto.response.ApiResponse;
import com.miuky.ewallet.dto.response.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import tools.jackson.databind.exc.UnrecognizedPropertyException;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<List<ValidationError>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        List<ValidationError> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(err -> new ValidationError(err.getField(), err.getDefaultMessage())).toList();
        return ApiResponse.fail(errors);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<ErrorResponse> handleJsonParseException(HttpMessageNotReadableException ex, HttpServletRequest request) {
        String message = "Dữ liệu gửi lên chứa trường không hợp lệ hoặc sai định dạng.";

        if (ex.getCause() instanceof UnrecognizedPropertyException) {
            String fieldName = ((UnrecognizedPropertyException) ex.getCause()).getPropertyName();
            message = "Trường '" + fieldName + "' không được hỗ trợ trong yêu cầu này.";
        }

        return ApiResponse.fail(ErrorResponse.build(400, null, message, request));
    }


    @ExceptionHandler(AppException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<ErrorResponse> handleAppException(AppException ex, HttpServletRequest request) {
        return ApiResponse.fail(ErrorResponse.build(ex.getErrorCode(), request));
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<ErrorResponse> handleOtherException(Exception ex, HttpServletRequest request) {
        return ApiResponse.fail(ErrorResponse.build(300, null, ex.getMessage(), request));
    }
}
