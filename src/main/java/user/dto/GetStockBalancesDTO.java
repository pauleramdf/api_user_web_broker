package user.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class GetStockBalancesDTO {

    private Long id_stock;
    @NotBlank
    private String username;

    public GetStockBalancesDTO(){

    }
    public GetStockBalancesDTO(String username, Long id_stock) {

        this.username = username;
        this.id_stock = id_stock;
    }

}
