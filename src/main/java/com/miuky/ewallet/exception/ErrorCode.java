package com.miuky.ewallet.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    SUCCESS("Giao dịch thành công", HttpStatus.OK),
    USER_NOT_FOUND("Không tìm thấy người dùng", HttpStatus.NOT_FOUND),
    WALLET_NOT_FOUND("Không tìm thấy ví", HttpStatus.NOT_FOUND),
    RECIPIENT_NOT_FOUND("Không tìm thấy người nhận", HttpStatus.NOT_FOUND),
    WALLET_LOCKED("Ví đã bị khóa", HttpStatus.FORBIDDEN),
    USER_BANNED("Người dng đã bị cấm", HttpStatus.FORBIDDEN),
    INSUFFICIENT_BALANCE("Số dư không đủ để thực hiện giao dịch", HttpStatus.BAD_REQUEST),
    DUPLICATE_TRANSACTION("Giao dịch đang được xử lý, vui lòng không thử lại", HttpStatus.CONFLICT),
    SYSTEM_ERROR("Hệ thống đang bảo trì, vui lòng thử lại sau", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_TOKEN("Invalid token", HttpStatus.UNAUTHORIZED),
    PHONE_EXISTED("Phone existed", HttpStatus.CONFLICT),
    INVALID_CREDENTIALS("Invalid email or password", HttpStatus.UNAUTHORIZED),
    SYSTEM_WALLET_NOT_INITIALIZED("Ví hệ thống chưa được khởi tạo", HttpStatus.NOT_FOUND),
    INVALID_TRANSFER("Invalid transfer", HttpStatus.CONFLICT);

    private final String message;
    private final HttpStatus httpStatus;

    ErrorCode(String message, HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
