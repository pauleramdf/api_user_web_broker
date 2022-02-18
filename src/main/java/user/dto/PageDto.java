package user.dto;

import lombok.Data;

@Data
public class PageDto {

    private String username;
    private Integer page;
    private Integer totalPages;
    private Integer size;
}
