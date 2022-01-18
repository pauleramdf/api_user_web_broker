package user.DTO;

import user.model.User;
import user.model.UserOrders;

public class CreateOrdersDTO {
    private Long id_user;
    private Long id_stock;
    private String stock_name;
    private String stock_symbol;
    private Long volume;
    private double price;
    private Long remaining_volume;
    private double total_price;
    private int type;
    private int status;

    public UserOrders transformaParaObjeto(User user){
        return new UserOrders(user, id_stock, stock_symbol, stock_name, volume, price, total_price, remaining_volume, type, status);
    }

    public Long getId_user() {
        return id_user;
    }

    public void setId_user(Long id_user) {
        this.id_user = id_user;
    }

    public Long getId_stock() {
        return id_stock;
    }

    public void setId_stock(Long id_stock) {
        this.id_stock = id_stock;
    }

    public String getStock_name() {
        return stock_name;
    }

    public void setStock_name(String stock_name) {
        this.stock_name = stock_name;
    }

    public String getStock_symbol() {
        return stock_symbol;
    }

    public void setStock_symbol(String stock_symbol) {
        this.stock_symbol = stock_symbol;
    }

    public Long getVolume() {
        return volume;
    }

    public void setVolume(Long volume) {
        this.volume = volume;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Long getRemaining_volume() {
        return remaining_volume;
    }

    public void setRemaining_volume(Long remaining_volume) {
        this.remaining_volume = remaining_volume;
    }

    public double getTotal_price() {
        return total_price;
    }

    public void setTotal_price(double total_price) {
        this.total_price = total_price;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
