package kr.co.mythings.shackit.core.common.exception;

public class ApiException extends RootException {

    public ApiException(String code, String message) {
        super(code, message);
    }
}
