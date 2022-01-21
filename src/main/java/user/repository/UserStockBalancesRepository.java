package user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import user.DTO.FindAllByUserDTO;
import user.model.User;
import user.model.UserStockBalances;
import user.model.UserStockBalancesId;

import java.util.List;

@Repository
public interface UserStockBalancesRepository extends JpaRepository<UserStockBalances, UserStockBalancesId>{

    @Query(nativeQuery = true, value = "SELECT * from user_stock_balances as usb where usb.id_user = :id_user")
    List<FindAllByUserDTO> findAllByUser(@Param("id_user") Long id_user);

//    @Query(value = "SELECT new user.model.UserStockBalances(usb.id_stock, usb.user, usb.stock_symbol, usb.stock_name, usb.volume,usb.enabled ) from UserStockBalances as usb where usb.user.id = :id_user and usb.id_stock = :id_stock")
//    Optional<UserStockBalances> find(@Param("id_user") Long id_user, @Param("id_stock") Long id_stock);

//    @Transactional
//    @Modifying
//    @Query(nativeQuery = true ,value = """
//            INSERT into public.user_stock_balances (id_user, id_stock, stock_symbol, stock_name, volume, created_on, updated_on, enabled)
//            values (:id_user, :id_stock, :stock_symbol, :stock_name, :volume, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, :enabled)
//            on conflict(id_user, id_stock)
//            do update set
//                stock_symbol = :stock_symbol,
//                stock_name = :stock_name,
//                volume = :volume
//            where
//                public.user_stock_balances.id_user = :id_user and
//                public.user_stock_balances.id_stock = :id_stock
//            """)
//    void saveWallet(@Param("id_user")  Long user,  @Param("id_stock") Long id_stock, @Param("stock_symbol") String stock_symbol,@Param("stock_name") String stock_name, @Param("volume") Long volume,@Param("enabled") boolean enabled);

}
;