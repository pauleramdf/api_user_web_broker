package user.dto.userorders;

import lombok.Data;

@Data
public class OrderPageDto {

    private Integer page;
    private Integer totalPages;
    private Integer size;
}