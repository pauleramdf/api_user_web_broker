package user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import user.model.UserOrders;

@Repository
public interface UserOrdersRepository extends JpaRepository<UserOrders, Long> {
}