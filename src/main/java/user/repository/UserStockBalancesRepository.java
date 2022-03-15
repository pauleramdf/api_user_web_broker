package user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import user.model.UserStockBalances;
import user.model.UserStockBalancesId;

import java.util.List;

@Repository
public interface UserStockBalancesRepository extends JpaRepository<UserStockBalances, UserStockBalancesId>{

    @Query(nativeQuery = true, value = "SELECT * from user_stock_balances as usb where usb.id_user = :id_user")
    List<UserStockBalances> findAllByUser(@Param("id_user") Long idUser);

}