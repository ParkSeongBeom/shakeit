package kr.co.mythings.shakeit.api.common;

import kr.co.mythings.shackit.core.common.ApiStatusCode;
import kr.co.mythings.shackit.core.common.RootResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/")
public class DefaultController {

    @GetMapping("/alb-check")
    public RootResponse albCheck(
    ) {
        return new RootResponse<>(ApiStatusCode.OK, null);
    }
}
