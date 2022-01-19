package user.model;

import lombok.Getter;
import lombok.Setter;
import net.minidev.json.annotate.JsonIgnore;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.web.reactive.function.client.WebClient;

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
    @JsonIgnore
    @Column(name = "password", nullable = false)
    private String password;
    @Column(name = "dollar_balance", nullable = false)
    private double dollar_balance;
    @Column(name = "enabled", nullable = false)
    private boolean enabled;
    @CreatedDate
    @Column(name = "created_on")
    private Timestamp created_on;
    @LastModifiedDate
    @Column(name = "updated_on")
    private Timestamp updated_on;

    public User(){
        this.enabled = true;
        this.created_on = Timestamp.valueOf(LocalDateTime.now());
        this.updated_on = Timestamp.valueOf(LocalDateTime.now());
    }

}
