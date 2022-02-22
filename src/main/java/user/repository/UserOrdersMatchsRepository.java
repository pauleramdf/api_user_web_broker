package user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import user.model.UserOrdersMatchs;
import user.model.UserOrdersMatchsId;

import java.util.List;

@Repository
public interface UserOrdersMatchsRepository extends JpaRepository<UserOrdersMatchs, UserOrdersMatchsId> {

    @Query(nativeQuery = true, value = "SELECT * from user_orders_matchs as u where u.id_sell_order = :sell_order_id")
    List<UserOrdersMatchs> getMatchSellHistory(@Param("sell_order_id") Long id);

    @Query(nativeQuery = true,value =  "SELECT * from user_orders_matchs as u where u.id_buy_order = :buy_order_id")
    List<UserOrdersMatchs>  getMatchBuyHistory(@Param("buy_order_id") Long id);
}
