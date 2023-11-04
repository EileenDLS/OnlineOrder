package com.fullstack.onlineorder.controller;

import com.fullstack.onlineorder.entity.MenuItemEntity;
import com.fullstack.onlineorder.model.RestaurantDto;
import com.fullstack.onlineorder.service.MenuItemService;
import com.fullstack.onlineorder.service.RestaurantService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MenuController {

    // depend on restaurantService and menuItemService
    private final RestaurantService restaurantService;
    private final MenuItemService menuItemService;


    public MenuController(RestaurantService restaurantService, MenuItemService menuItemService) {
        this.restaurantService = restaurantService;
        this.menuItemService = menuItemService;
    }


    // return specific restaurant
    // restaurantId is a variable which assigned value by frontend
    // 这样命名API就是restful api的一部分
    @GetMapping("/restaurant/{restaurantId}/menu")
    public List<MenuItemEntity> getMenuByRestaurant(@PathVariable("restaurantId")  long restaurantId) {
        // @PathVariable("restaurantId")： get frontend val
        return menuItemService.getMenuItemsByRestaurantId(restaurantId);
    }


    // return all restaurant
    @GetMapping("/restaurants/menu")
    public List<RestaurantDto> getMenuForAllRestaurants() {
        return restaurantService.getRestaurants();
    }
}

