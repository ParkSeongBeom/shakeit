package kr.co.mythings.shackit.core.common;

import kr.co.mythings.shackit.core.common.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ApiExceptionHandler {

    /**
     * Exception
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = Exception.class)
    public RootResponse<Object> exceptionHandler(Exception ex) {
        log.error("Exception", ex);

        return new RootResponse<>(ApiStatusCode.BAD_REQUEST, ex.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = RuntimeException.class)
    public RootResponse<Object> runtimeExceptionHandler(RuntimeException ex) {
        log.error("RuntimeException", ex);

        return new RootResponse<>(ApiStatusCode.BAD_REQUEST, ex.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = RootException.class)
    public RootResponse<Object> rootExceptionHandler(RootException ex) {
        log.error("RootException", ex);

        return new RootResponse<>(ex.getCode(), ex.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = BizException.class)
    public RootResponse<Object> bizExceptionHandler(BizException ex) {
        log.error("BizException", ex);

        return new RootResponse<>(ex.getCode(), ex.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(value = URLException.class)
    public RootResponse<Object> urlExceptionHandler(URLException ex) {
        log.error("URLException", ex);

        return null;
    }

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(value = BypassException.class)
    public RootResponse<Object> bypassExceptionHandler(BypassException ex) {
        return new RootResponse<>(ex.getCode(), ex.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = ApiException.class)
    public RootResponse<Object> apiExceptionHandler(ApiException ex) {
        log.error("ApiException", ex);

        return new RootResponse<>(ex.getCode(), ex.getMessage());
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(value = UnAuthorizedException.class)
    public RootResponse<Object> unAuthorizedExceptionHandler(UnAuthorizedException ex) {
        log.error("UnAuthorizedException", ex);

        return new RootResponse<>(ex.getCode(), ex.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = DBException.class)
    public RootResponse<Object> dbExceptionHandler(DBException ex) {
        log.error("DBException", ex);

        return new RootResponse<>(ex.getCode(), ex.getMessage());
    }
}
