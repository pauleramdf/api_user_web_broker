package user.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MaxMinDTO {

    Double maxPrice;
    Double minPrice;

    public MaxMinDTO(){

    }
    public MaxMinDTO(Double maxPrice, Double minPrice) {
        if(!(maxPrice == null)){
            this.maxPrice = maxPrice;
            this.minPrice = minPrice;
        }

    }

}
