package user.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Getter
@Setter
@Entity
@Table(name ="user_orders")
public class UserOrders implements Serializable {
    private static final long SERIALVERSIONUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_user")
    private User user;

    @Column(name = "id_stock")
    private Long idStock;

    @Column(name = "stock_symbol")
    private String stockSymbol;

    @Column(name = "stock_name")
    private String stockName;

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
    private Timestamp created;

    @UpdateTimestamp
    @Column(name = "updated_on")
    private Timestamp updated;

    @PrePersist
    private void onCreate(){
        this.status = 1;
    }

    public UserOrders(){
        //construtor vazio
    }
}
