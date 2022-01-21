package user.DTO;

import lombok.Getter;
import lombok.Setter;
import user.model.User;
import user.model.UserStockBalances;
import user.model.UserStockBalancesId;

@Getter
@Setter
public class GetStockBalancesDTO {

    private Long id_stock;
    private Long id_user;

    public GetStockBalancesDTO(){

    }
    public GetStockBalancesDTO(Long id_user, Long id_stock) {

        this.id_user = id_user;
        this.id_stock = id_stock;
    }

}
