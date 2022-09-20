package kr.co.mythings.shackit.core.common;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RootResponse<T> {
    String code;
    T data;

    public RootResponse(String code, T data) {
        this.code = code;
        this.data = data;
    }
}
