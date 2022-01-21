package user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import user.DTO.MaxMinDTO;
import user.model.UserOrders;
import user.repository.UserOrdersRepository;

import java.util.List;

@Service("orderService")
public class OrderService {
    @Autowired
    private UserOrdersRepository ordersRepository;

    public List<UserOrders> findAllBuyOrders(Long stock_name){
        return ordersRepository.findAllBuyOrders(stock_name);
    }

    public List<UserOrders> findAllSellOrders(Long stock_name){
        return ordersRepository.findAllSellOrders(stock_name);
    }

    public MaxMinDTO findMaxMinSellOrders(Long stock){
        return ordersRepository.findMaxMinSellOrders(stock).orElse(new MaxMinDTO(Double.valueOf(0),Double.valueOf(0)));
    }

    public MaxMinDTO findMaxMinBuyOrders(Long stock){
        return ordersRepository.findMaxMinSellOrders(stock).orElse(new MaxMinDTO(Double.valueOf(0),Double.valueOf(0)));
    }

    public UserOrders save(UserOrders orders){
        if(orders.getRemainingVolume() == 0)
            orders.setStatus(0);
        return ordersRepository.save(orders);
    }

    public UserOrders subVolume(UserOrders orders, Long volume){
        orders.setRemainingVolume(orders.getVolume() - volume);
        if(orders.getRemainingVolume() == 0)
            orders.setStatus(0);

        return ordersRepository.save(orders);
    }

    public UserOrders addVolume(UserOrders orders, Long volume){
        orders.setVolume( orders.getVolume() + volume);
        return ordersRepository.save(orders);
    }

    public void disable(UserOrders order) {
        order.setStatus(0);
        ordersRepository.save(order);
    }
}
