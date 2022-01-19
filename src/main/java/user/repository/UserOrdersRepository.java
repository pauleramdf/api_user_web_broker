package user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import user.DTO.MaxMinDTO;
import user.model.UserOrders;

import javax.validation.constraints.Max;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserOrdersRepository extends JpaRepository<UserOrders, Long> {

    @Query(nativeQuery = true, value="SELECT * FROM user_orders o where o.type = 0 AND o.status = 1 and o.stock_name = :stock_name order by  o.created_on asc")
    List<UserOrders> findAllBuyOrders(@Param("stock_name") String stock_name);

    @Query(nativeQuery = true, value="SELECT * FROM user_orders o where o.type = 1 AND o.status = 1 and o.stock_name = :stock_name order by  o.created_on asc ")
    List<UserOrders> findAllSellOrders(@Param("stock_name") String stock_name);

    @Query("SELECT new user.DTO.MaxMinDTO(max(o.price),min(o.price)) FROM UserOrders o where o.type = 0 AND o.status = 1 AND o.stock_name = :stock_name")
    Optional<MaxMinDTO> findMaxMinBuyOrders(@Param("stock_name") String stock_name);

    @Query("SELECT new user.DTO.MaxMinDTO (max(o.price),min(o.price)) FROM UserOrders o where o.type = 1 AND o.status = 1 AND o.stock_name = :stock_name")
    Optional<MaxMinDTO> findMaxMinSellOrders(@Param("stock_name") String stock_name);
}