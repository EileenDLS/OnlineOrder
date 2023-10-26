package com.fullstack.onlineorder.service;

import com.fullstack.onlineorder.entity.CartEntity;
import com.fullstack.onlineorder.entity.MenuItemEntity;
import com.fullstack.onlineorder.entity.OrderItemEntity;
import com.fullstack.onlineorder.model.CartDto;
import com.fullstack.onlineorder.model.OrderItemDto;
import com.fullstack.onlineorder.repository.CartRepository;
import com.fullstack.onlineorder.repository.MenuItemRepository;
import com.fullstack.onlineorder.repository.OrderItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class CartService {
    // depends on cart, menu and order entities
    private final CartRepository cartRepository;
    private final MenuItemRepository menuItemRepository;
    private final OrderItemRepository orderItemRepository;

    public CartService(
            CartRepository cartRepository,
            MenuItemRepository menuItemRepository,
            OrderItemRepository orderItemRepository) {
        this.cartRepository = cartRepository;
        this.menuItemRepository = menuItemRepository;
        this.orderItemRepository = orderItemRepository;
    }

    // 把要点的menu加到cart中
    /* 把一系列操作都放到一个commit里面，全部成功了这个commit才会放到数据库里，
     如果一旦有失败，前次操作成功的都会rollback，返回到没操作的状态，防止出现数据不一致问题
      例如：cartRepository.updateTotalPrice(）出错，orderItemRepository.save(）就会rollback
      防止cart里面数量都变了，但是总价还没有变*/
    @Transactional
    public void addMenuItemToCart(long customerId, long menuItemId) {
        CartEntity cart = cartRepository.getByCustomerId(customerId);
        MenuItemEntity menuItem = menuItemRepository.findById(menuItemId).get();
        OrderItemEntity orderItem = orderItemRepository.findByCartIdAndMenuItemId(cart.id(), menuItem.id());


        Long orderItemId;
        int quantity;

        // 这个菜有没有点过，没有就数量置为1，点过的话就把它的数量+1
        if (orderItem == null) {
            orderItemId = null;
            quantity = 1;
        } else {
            orderItemId = orderItem.id();
            quantity = orderItem.quantity() + 1;
        }
        OrderItemEntity newOrderItem = new OrderItemEntity(orderItemId, menuItemId, cart.id(), menuItem.price(), quantity);
        orderItemRepository.save(newOrderItem); // orderItemId == null, execute INSERT; != null, execute UPDATE
        cartRepository.updateTotalPrice(cart.id(), cart.totalPrice() + menuItem.price());
    }


    public CartDto getCart(Long customerId) {
        CartEntity cart = cartRepository.getByCustomerId(customerId);
        List<OrderItemEntity> orderItems = orderItemRepository.getAllByCartId(cart.id());
        // 将data转化为我们想要的样子（使用Dto），再传给前端
        List<OrderItemDto> orderItemDtos = getOrderItemDtos(orderItems);
        return new CartDto(cart, orderItemDtos);
    }


    // 清空购物车
    @Transactional
    public void clearCart(Long customerId) {
        // 得到用户对应的cart
        CartEntity cartEntity = cartRepository.getByCustomerId(customerId);
        // 删除这个购物车
        orderItemRepository.deleteByCartId(cartEntity.id());
        // 更新总价
        cartRepository.updateTotalPrice(cartEntity.id(), 0.0);
    }


    // 目的：通过orderItem来得到里面的所有menuItem的信息（因为原本数据库中order中只有menu的id，没有详细信息）
    private List<OrderItemDto> getOrderItemDtos(List<OrderItemEntity> orderItems) {
        Set<Long> menuItemIds = new HashSet<>();
        for (OrderItemEntity orderItem : orderItems) {
            menuItemIds.add(orderItem.menuItemId());  // 得到order里的所有menuItem_id
        }
        List<MenuItemEntity> menuItems = menuItemRepository.findAllById(menuItemIds);
        // key: menuItem_id; val: menuItem
        Map<Long, MenuItemEntity> menuItemMap = new HashMap<>();
        for (MenuItemEntity menuItem : menuItems) {
            menuItemMap.put(menuItem.id(), menuItem);
        }
        // order和menu的信息整合好放到orderItemDto中
        List<OrderItemDto> orderItemDtos = new ArrayList<>();
        for (OrderItemEntity orderItem : orderItems) {
            MenuItemEntity menuItem = menuItemMap.get(orderItem.menuItemId());
            OrderItemDto orderItemDto = new OrderItemDto(orderItem, menuItem);
            orderItemDtos.add(orderItemDto);
        }
        return orderItemDtos;
    }
}

