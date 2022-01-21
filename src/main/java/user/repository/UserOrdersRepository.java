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

    @Query(nativeQuery = true, value="SELECT * FROM user_orders o where o.type = 0 AND o.status = 1 and o.id_stock = :id_stock order by  o.created_on asc")
    List<UserOrders> findAllBuyOrders(@Param("id_stock") Long id_stock);

    @Query(nativeQuery = true, value="SELECT * FROM user_orders o where o.type = 1 AND o.status = 1 and o.id_stock = :id_stock order by  o.created_on asc ")
    List<UserOrders> findAllSellOrders(@Param("id_stock") Long id_stock);

    @Query("SELECT new user.DTO.MaxMinDTO(max(o.price),min(o.price)) FROM UserOrders o where o.type = 0 AND o.status = 1 AND o.id_stock = :id_stock")
    Optional<MaxMinDTO> findMaxMinBuyOrders(@Param("id_stock") Long id_stock);

    @Query("SELECT new user.DTO.MaxMinDTO (max(o.price),min(o.price)) FROM UserOrders o where o.type = 1 AND o.status = 1 AND o.id_stock = :id_stock")
    Optional<MaxMinDTO> findMaxMinSellOrders(@Param("id_stock") Long id_stock);
}