package user.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Data
public class GetStockBalancesDTO {

    @NotBlank
    private String username;

}
