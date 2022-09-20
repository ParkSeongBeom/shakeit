package kr.co.mythings.shakeit.api.user.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class LoginRes {
    private String loginToken;

    private UserInfo userInfo;
}
