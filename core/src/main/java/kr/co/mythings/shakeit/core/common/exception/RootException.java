package kr.co.mythings.shackit.core.common.exception;

import lombok.Getter;

public class RootException extends RuntimeException {

    @Getter
    private String code;

    public RootException(String code, String message) {
        super(message);
        this.code = code;
    }
}
