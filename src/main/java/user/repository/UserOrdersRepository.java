package user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import user.model.UserOrders;

import java.util.List;

@Repository
public interface UserOrdersRepository extends JpaRepository<UserOrders, Long> {

    @Query(nativeQuery = true, value="SELECT o.stock_name, o.stock_symbol, o.volume, o.price, o.remaining_volume, o.total_price FROM user_orders o where o.type = 0 AND o.status = 1")
    List<UserOrders> findAllBuyOrders(@Param("stock_name") String stock_name);

    @Query(nativeQuery = true, value="SELECT o.stock_name, o.stock_symbol, o.volume, o.price,o.remaining_volume, o.total_price FROM user_orders o where o.type = 1 AND o.status = 1")
    List<UserOrders> findAllSellOrders(@Param("stock_name") String stock_name);
}