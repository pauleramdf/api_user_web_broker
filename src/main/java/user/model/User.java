package user.model;

import lombok.Getter;
import lombok.Setter;
import net.minidev.json.annotate.JsonIgnore;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name="users")
public class User implements Serializable {
    private static final long serialversionUID = 1L;

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "dollar_balance", nullable = false)
    private Double dollar_balance;

    @Column(name = "enabled", nullable = false)
    private boolean enabled;

    @CreationTimestamp
    @Column(name = "created_on")
    private Timestamp created_on;
    @UpdateTimestamp
    @Column(name = "updated_on")
    private Timestamp updated_on;

    @PrePersist
    private void onCreate(){
        this.enabled = true;
    }

    public User(){
    }

    public User(String username, String password, Double dollar_balance) {
        this.username = username;
        this.password = password;
        this.dollar_balance = dollar_balance;
    }
}
