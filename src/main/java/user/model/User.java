package user.model;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import javax.persistence.*;

@Entity
@Table(name="users")
public class User implements Serializable {
    private static final long serialversionUID = 1L;

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private BigDecimal dollar_ballance;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "username", nullable = false)
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    @Column(name = "password", nullable = false)
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    @Column(name = "dollar_ballance", nullable = false)
    public BigDecimal getDollar_ballance() {
        return dollar_ballance;
    }
    public void setDollar_ballance(BigDecimal dollar_ballance) {
        this.dollar_ballance = dollar_ballance;
    }

    @Column(name = "enabled", nullable = false)
    public boolean isEnabled() {
        return enabled;
    }
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }


    public Timestamp getCreated_on() {
        return created_on;
    }
    public void setCreated_on(Timestamp created_on) {
        this.created_on = Timestamp.valueOf(LocalDateTime.now());

    }

    public Timestamp getUpdated_on() {
        return updated_on;
    }
    public void setUpdated_on(Timestamp updated_on) {
        this.updated_on = Timestamp.valueOf(LocalDateTime.now());
    }
}
