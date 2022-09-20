package kr.co.mythings.shakeit.api.user.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class LoginReq {
    @NotEmpty
    private String email;
    @NotEmpty
    private String password;
    @NotNull
    private LoginDevice loginDevice;

    @Data
    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
    public static class LoginDevice {
        @NotEmpty
        private String deviceUuid;
        @NotEmpty
        private String deviceName;
        @NotEmpty
        private String deviceType;
        @NotEmpty
        private String deviceOs;
        private String pushToken;
    }
}
