package user.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name ="user_orders")
public class UserOrders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @ManyToOne
    @JoinColumn(name = "id_user")
    private User user;

    private Long id_stock;
    private String stock_symbol;
    private String stock_name;
    private Long volume;
    private double price;
    @Column(name = "total_price")
    private double totalPrice;
    @Column(name = "remaining_volume")
    private Long remainingVolume;
    private int type;
    private int status;
    private Timestamp created_on;
    private Timestamp updated_on;

    public UserOrders(){
        this.created_on = Timestamp.valueOf(LocalDateTime.now());
        this.updated_on = Timestamp.valueOf(LocalDateTime.now());
        this.status = 1;
    }

    public UserOrders(User user, Long id_stock, String stock_symbol, String stock_name, Long volume, double price, double totalPrice, Long remainingVolume, int type, int status) {
        this.id_stock = id_stock;
        this.user = user;
        this.stock_symbol = stock_symbol;
        this.stock_name = stock_name;
        this.volume = volume;
        this.price = price;
        this.totalPrice = totalPrice;
        this.remainingVolume = remainingVolume;
        this.type = type;
        this.status = status;
        this.created_on = Timestamp.valueOf(LocalDateTime.now());
        this.updated_on = Timestamp.valueOf(LocalDateTime.now());
    }
}
