package user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import user.model.UserStockBalances;

@Repository
public interface UserStockBalancesRepository extends JpaRepository<UserStockBalances,Long>{
}
