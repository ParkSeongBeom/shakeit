package kr.co.mythings.shakeit.api.user.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class RegistReq {
    @NotEmpty
    private String nickName;
    @NotEmpty
    private String email;
    @NotEmpty
    private String password;
}
