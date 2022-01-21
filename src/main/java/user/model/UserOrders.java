package user.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name ="user_orders")
public class UserOrders implements Serializable {
    private static final long serialversionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @ManyToOne
    @JoinColumn(name = "id_user")
    private User user;

    @Column(name = "id_stock")
    private Long id_stock;

    @Column(name = "stock_symbol")
    private String stock_symbol;

    @Column(name = "stock_name")
    private String stock_name;

    @Column(name = "volume")
    private Long volume;

    @Column(name = "price")
    private Double price;

    @Column(name = "total_price")
    private Double totalPrice;

    @Column(name = "remaining_volume")
    private Long remainingVolume;

    @Column(name = "type")
    private Integer type;

    @Column(name = "status")
    private Integer status;

    @CreationTimestamp
    @Column(name = "created_on")
    private Timestamp created_on;

    @UpdateTimestamp
    @Column(name = "updated_on")
    private Timestamp updated_on;

    @PrePersist
    private void onCreate(){
        this.status = 1;
    }

    public UserOrders(){
    }

    public UserOrders(User user, Long id_stock, String stock_symbol, String stock_name, Long volume, Double price, Double totalPrice, Long remainingVolume, Integer type, Integer status) {
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
    }
}
