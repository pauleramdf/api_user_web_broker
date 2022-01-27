package user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import user.dto.MaxMinDTO;
import user.model.UserOrders;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserOrdersRepository extends JpaRepository<UserOrders, Long> {

    @Query(nativeQuery = true, value="""
    SELECT * FROM user_orders o 
    WHERE o.type = 0 AND o.status = 1 AND o.id_stock = :id_stock AND o.price >= :price AND o.remaining_volume > 0
    ORDER BY  o.created_on ASC
    """)
    List<UserOrders> findAllBuyOrders(@Param("id_stock") Long id_stock, @Param("price") Double price);

    @Query(nativeQuery = true, value="""
    SELECT * FROM user_orders o 
    WHERE o.type = 1 AND o.status = 1 AND o.id_stock = :id_stock AND o.price <= :price  AND o.remaining_volume > 0
    ORDER BY  o.created_on ASC
     """)
    List<UserOrders> findAllSellOrders(@Param("id_stock") Long id_stock, @Param("price") Double price);

    @Query(nativeQuery = true, value = """
    SELECT * FROM user_orders o
    WHERE o.status = 1   AND o.remaining_volume = 0
    """)
    List<UserOrders> findAllActive();

    @Query(nativeQuery = true, value= """
    SELECT * FROM user_orders as uo where uo.id_user = :id
""")
    List<UserOrders> FindAllOrdersByUser(@Param("id") Long id);

    @Query("SELECT new user.dto.MaxMinDTO(max(o.price),min(o.price)) FROM UserOrders o where o.type = 0 AND o.status = 1 AND o.id_stock = :id_stock")
    Optional<MaxMinDTO> findMaxMinBuyOrders(@Param("id_stock") Long id_stock);

    @Query("SELECT new user.dto.MaxMinDTO (max(o.price),min(o.price)) FROM UserOrders o where o.type = 1 AND o.status = 1 AND o.id_stock = :id_stock")
    Optional<MaxMinDTO> findMaxMinSellOrders(@Param("id_stock") Long id_stock);
}