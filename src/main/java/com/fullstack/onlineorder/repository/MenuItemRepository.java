package com.fullstack.onlineorder.repository;

import com.fullstack.onlineorder.entity.MenuItemEntity;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;

public interface MenuItemRepository extends ListCrudRepository<MenuItemEntity, Long> {
    // SELECT * FROM menu_items WHERE restaurant_id = :restaurantId
    List<MenuItemEntity> getByRestaurantId(Long restaurantId);
}
