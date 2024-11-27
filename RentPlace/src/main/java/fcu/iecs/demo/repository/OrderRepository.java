package fcu.iecs.demo.repository;



import fcu.iecs.demo.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    // 可以擴展自定義的查詢方法
    Order findByOrderId(int orderId);
}
