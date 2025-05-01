package school.faang.user_service.dto;

import lombok.Builder;

@Builder
public record MessageDto(
        String message,
        Integer code,
        Boolean status
) {

    public static MessageDto success(String message, int code) {
        return MessageDto.builder()
                .message(message)
                .code(code)
                .status(true)
                .build();
    }

    public static MessageDto error(String message, int code) {
        return MessageDto.builder()
                .message(message)
                .code(code)
                .status(false)
                .build();
    }

}
