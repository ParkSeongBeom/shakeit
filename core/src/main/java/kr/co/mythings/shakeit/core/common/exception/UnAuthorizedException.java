package kr.co.mythings.shackit.core.common.exception;

public class UnAuthorizedException extends ApiException {

    public UnAuthorizedException(String code, String message) {
        super(code, message);
    }
}
