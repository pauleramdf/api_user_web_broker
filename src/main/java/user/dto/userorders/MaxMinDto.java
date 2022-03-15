package user.dto.userorders;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MaxMinDto {

    Double maxPrice;
    Double minPrice;

    public MaxMinDto(){

    }
    public MaxMinDto(Double maxPrice, Double minPrice) {
        this.maxPrice = maxPrice;
        this.minPrice = minPrice;
    }

}