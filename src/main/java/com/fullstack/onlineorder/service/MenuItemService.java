package com.fullstack.onlineorder.service;

import com.fullstack.onlineorder.entity.MenuItemEntity;
import com.fullstack.onlineorder.repository.MenuItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MenuItemService {
    private final MenuItemRepository menuItemRepository;
    public MenuItemService(MenuItemRepository menuItemRepository) {
        this.menuItemRepository = menuItemRepository;
    }

    // get menu by restaurant
    public List<MenuItemEntity> getMenuItemsByRestaurantId(long restaurantId) {
        return menuItemRepository.getByRestaurantId(restaurantId);
    }

    // get menu details by menu_id
    public MenuItemEntity getMenuItemById(long menuId) {
        return menuItemRepository.findById(menuId).get();
    }
}

