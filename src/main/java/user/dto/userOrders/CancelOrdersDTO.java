package user.dto.userOrders;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CancelOrdersDTO {
    private Long id;
    private Long id_stock;
    private int type;
}
