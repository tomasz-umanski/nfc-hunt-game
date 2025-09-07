package pl.osetoctet.common.exception;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import pl.osetoctet.common.utils.OffsetDateTimeUtils;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private static final String ISO_8601_FORMAT = "YYYY-MM-DDTHH:mm:ssZ";

    private final MessageSource messageSource;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        List<String> errors = ex.getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());

        ErrorResponse errorResponse = createErrorResponse(HttpStatus.BAD_REQUEST, errors, request);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponse> handleCreationException(ValidationException ex, HttpServletRequest request) {
        ErrorResponse errorResponse = createErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RetrieveException.class)
    public ResponseEntity<ErrorResponse> handleRetrieveException(RetrieveException ex, HttpServletRequest request) {
        ErrorResponse errorResponse = createErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage(), request);
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<ErrorResponse> handleMissingRequestHeaderException(MissingRequestHeaderException ex, HttpServletRequest request) {
        ErrorResponse errorResponse = createErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex, HttpServletRequest request) {
        String message = determineHttpMessageNotReadableException(ex);
        ErrorResponse errorResponse = createErrorResponse(HttpStatus.BAD_REQUEST, message, request);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex, HttpServletRequest request) {
        String message = determineMethodArgumentTypeMismatchExceptionErrorMessage(ex);
        ErrorResponse errorResponse = createErrorResponse(HttpStatus.BAD_REQUEST, message, request);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthException(AuthenticationException ex, HttpServletRequest request) {
        ErrorResponse errorResponse = createErrorResponse(HttpStatus.UNAUTHORIZED, ex.getMessage(), request);
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ErrorResponse> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException ex, HttpServletRequest request) {
        ErrorResponse errorResponse = createErrorResponse(HttpStatus.PAYLOAD_TOO_LARGE, ex.getMessage(), request);
        return new ResponseEntity<>(errorResponse, HttpStatus.PAYLOAD_TOO_LARGE);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex, HttpServletRequest request) {
        ErrorResponse errorResponse = createErrorResponse(HttpStatus.METHOD_NOT_ALLOWED, ex.getMessage(), request);
        return new ResponseEntity<>(errorResponse, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException ex, HttpServletRequest request) {
        ErrorResponse errorResponse = createErrorResponse(HttpStatus.UNSUPPORTED_MEDIA_TYPE, ex.getMessage(), request);
        return new ResponseEntity<>(errorResponse, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }

    private ErrorResponse createErrorResponse(HttpStatus status, String message, HttpServletRequest request) {
        return new ErrorResponse(
                OffsetDateTimeUtils.getCurrentUtcTimestamp(),
                status.value(),
                messageSource.getMessage(message, null, message, request.getLocale()),
                request.getRequestURI()
        );
    }

    private ErrorResponse createErrorResponse(HttpStatus status, List<String> messages, HttpServletRequest request) {
        String errorMessage = messages.stream()
                .map(message -> messageSource.getMessage(message, null, message, request.getLocale()))
                .collect(Collectors.joining(", "));

        return new ErrorResponse(
                OffsetDateTimeUtils.getCurrentUtcTimestamp(),
                status.value(),
                errorMessage,
                request.getRequestURI()
        );
    }

    private String determineEnumErrorMessage(Object invalidValue, Class<?> enumType, String parameterName) {
        if (enumType == List.class || (enumType != null && List.class.isAssignableFrom(enumType))) {
            return String.format(
                    "Invalid value '%s' for parameter '%s'. Expected a list of valid event types.",
                    invalidValue,
                    parameterName
            );
        }

        if (enumType != null && enumType.isEnum()) {
            String enumValues = Arrays.stream(enumType.getEnumConstants())
                    .map(Object::toString)
                    .collect(Collectors.joining(", "));

            if (invalidValue instanceof List<?> invalidList) {
                String invalidListValues = invalidList.stream()
                        .map(Object::toString)
                        .collect(Collectors.joining(", "));

                return String.format(
                        "Invalid values %s for parameter '%s'. Allowed values are: [%s].",
                        invalidListValues,
                        parameterName,
                        enumValues
                );
            }

            return String.format(
                    "Invalid value '%s' for parameter '%s'. Allowed values are: [%s].",
                    invalidValue,
                    parameterName,
                    enumValues
            );
        }
        return null;
    }

    private String determineMethodArgumentTypeMismatchExceptionErrorMessage(MethodArgumentTypeMismatchException ex) {
        if (ex.getRequiredType() != null) {
            if (OffsetDateTime.class.equals(ex.getRequiredType())) {
                return String.format(
                        "Invalid date format for parameter '%s'. Expected format: '%s'.",
                        ex.getName(),
                        ISO_8601_FORMAT
                );
            }

            if (UUID.class.equals(ex.getRequiredType())) {
                return String.format(
                        "Invalid UUID format for parameter '%s'. Value '%s' is not a valid UUID. Expected format: xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx.",
                        ex.getName(),
                        ex.getValue()
                );
            }

            String enumErrorMessage = determineEnumErrorMessage(ex.getValue(), ex.getRequiredType(), ex.getName());
            if (enumErrorMessage != null) {
                return enumErrorMessage;
            }
        }
        return String.format("Invalid value '%s' for parameter '%s'.", ex.getValue(), ex.getName());
    }

    private String determineHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        Throwable cause = ex.getCause();
        if (cause instanceof InvalidFormatException invalidFormatException) {
            String parameterName = invalidFormatException.getPath().stream()
                    .map(JsonMappingException.Reference::getFieldName)
                    .reduce((first, second) -> second)
                    .orElse("unknown");

            return determineEnumErrorMessage(
                    invalidFormatException.getValue(),
                    invalidFormatException.getTargetType(),
                    parameterName
            );
        }
        return ex.getMessage();
    }

}