package user.DTO;

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

    public Double getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(Double maxPrice) {
        this.maxPrice = maxPrice;
    }

    public Double getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(Double minPrice) {
        this.minPrice = minPrice;
    }
}
