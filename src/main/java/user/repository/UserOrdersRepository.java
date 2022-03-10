package user.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import user.dto.stocks.StockPricesDTO;
import user.dto.userOrders.MaxMinDto;
import user.model.UserOrders;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserOrdersRepository extends JpaRepository<UserOrders, Long> {

    @Query("select c from UserOrders c where c.user.id = :id_user")
    Page<UserOrders> findAllPaged(Pageable pageable, @Param("id_user") Long id);

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

    @Query("SELECT new user.dto.userOrders.MaxMinDto(max(o.price),min(o.price)) FROM UserOrders o where o.type = :type AND o.status = 1 AND o.id_stock = :id_stock")
    Optional<MaxMinDto> findMaxMinOrders(@Param("id_stock") Long idStock, @Param("type") Integer type);

    @Query(nativeQuery = true, value= """
        SELECT ask.ask_min , ask.ask_max , bid.bid_min, bid.bid_max, ask.id_stock  
        FROM (
            SELECT min(price) AS ask_min, max(price) AS ask_max, id_stock
            FROM user_orders uo  WHERE id_stock = :id_stock AND type = 0 AND status = 1 GROUP BY id_stock
        ) AS ask 
        FULL JOIN (
            SELECT min(price) AS bid_min, max(price) AS bid_max, id_stock FROM user_orders
            WHERE id_stock = :id_stock AND type = 1 AND status = 1 GROUP BY id_stock
        ) AS bid 
        ON bid.id_stock = ask.id_stock
        """)
    Optional<StockPricesDTO> findAskBid(@Param("id_stock") Long id_stock);
}

