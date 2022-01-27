package user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import user.dto.FindAllOrdersByUserDTO;
import user.dto.MaxMinDTO;
import user.model.User;
import user.model.UserOrders;
import user.repository.UserOrdersRepository;
import user.repository.UserRepository;

import java.util.List;

@Service("orderService")
public class OrderService {
    @Autowired
    private UserOrdersRepository ordersRepository;

    @Autowired
    private UserRepository userRepository;

    public List<UserOrders> findAllBuyOrders(Long stock_name, Double price){
        return ordersRepository.findAllBuyOrders(stock_name, price);
    }

    public List<UserOrders> findAllSellOrders(Long stock_name, Double price){
        return ordersRepository.findAllSellOrders(stock_name, price);
    }

    public MaxMinDTO findMaxMinSellOrders(Long stock){
        return ordersRepository.findMaxMinSellOrders(stock).orElse(new MaxMinDTO(Double.valueOf(0),Double.valueOf(0)));
    }

    public MaxMinDTO findMaxMinBuyOrders(Long stock){
        return ordersRepository.findMaxMinBuyOrders(stock).orElse(new MaxMinDTO(null,null));
    }

    public void updateAllStatus(){
        List<UserOrders> orders = ordersRepository.findAllActive();
        for ( UserOrders order: orders
             ) {
                order.setStatus(0);
                ordersRepository.save(order);
        }
    }
    public List<UserOrders> getAllOrdersActives(){
        return ordersRepository.findAllActive();
    }
    public UserOrders save(UserOrders orders){
        return ordersRepository.save(orders);
    }

    public UserOrders subVolume(UserOrders order, Long volume){
        order.setRemainingVolume(order.getRemainingVolume() - volume);
        return ordersRepository.save(order);
    }

    public UserOrders addVolume(UserOrders order, Long volume){
        order.setVolume( order.getRemainingVolume() + volume);
        return ordersRepository.save(order);
    }

    public void disable(UserOrders order) {
        order.setStatus(0);
        ordersRepository.save(order);
    }

    public List<FindAllOrdersByUserDTO> FindAllOrdersByUser(String username) {
        User user = userRepository.findByName(username).orElseThrow();
        List<FindAllOrdersByUserDTO> list = ordersRepository.FindAllOrdersByUser(user.getId()).stream().map( (u) -> new FindAllOrdersByUserDTO(u)).toList();
        return list;
    }
}
