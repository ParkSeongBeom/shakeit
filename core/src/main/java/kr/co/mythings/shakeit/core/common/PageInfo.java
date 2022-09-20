package kr.co.mythings.shackit.core.common;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PageInfo {
    private Integer page;
    private Integer limit;
    private Integer offset;
    private Integer total;

    @Builder
    public PageInfo(Integer page, Integer limit, Integer offset, Integer total) {
        this.page = page;
        this.limit = limit;
        this.offset = offset;
        this.total = total;
    }
}
