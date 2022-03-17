package user.dto;

import lombok.Data;

@Data
public class PageDto {

    private Integer page;
    private Integer totalPages;
    private Integer size;
}
