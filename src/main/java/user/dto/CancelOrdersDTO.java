package user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CancelOrdersDTO {
    private Long id_user;
    private Long id_stock;
    private int type;
    private int status;
}
