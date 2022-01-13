package user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import user.model.User_stock_balances;

@Repository
public interface UserStockBalancesRepository extends JpaRepository<User_stock_balances,Long>{
}
