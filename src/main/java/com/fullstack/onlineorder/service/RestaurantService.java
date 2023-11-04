package com.fullstack.onlineorder.service;

import com.fullstack.onlineorder.entity.MenuItemEntity;
import com.fullstack.onlineorder.entity.RestaurantEntity;
import com.fullstack.onlineorder.model.MenuItemDto;
import com.fullstack.onlineorder.model.RestaurantDto;
import com.fullstack.onlineorder.repository.MenuItemRepository;
import com.fullstack.onlineorder.repository.RestaurantRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RestaurantService {
    // this service depends on menu entity and restaurant entity
    private final MenuItemRepository menuItemRepository;
    private final RestaurantRepository restaurantRepository;


    // constructor
    public RestaurantService(
            RestaurantRepository restaurantRepository,
            MenuItemRepository menuItemRepository) {
        this.restaurantRepository = restaurantRepository;
        this.menuItemRepository = menuItemRepository;
    }


    // 对menu进行按restaurant分类，并且整合成Dto返回
    public List<RestaurantDto> getRestaurants() {
        // get all restaurant and menu data
        List<RestaurantEntity> restaurantEntities = restaurantRepository.findAll();
        List<MenuItemEntity> menuItemEntities = menuItemRepository.findAll();

        // key: Long type -- restaurant id; value: menu item
        Map<Long, List<MenuItemDto>> groupedMenuItems = new HashMap<>();
        for (MenuItemEntity menuItemEntity : menuItemEntities) {
            // .computeIfAbsent(): 如果map里面没有这个restaurant id， 则创建新的，并初始化val（是一个arraylist），如果存在，return对应的val
            List<MenuItemDto> group = groupedMenuItems.computeIfAbsent(menuItemEntity.restaurantId(), k -> new ArrayList<>());
            MenuItemDto menuItemDto = new MenuItemDto(menuItemEntity);
            group.add(menuItemDto);  // menu放到对应的restaurant里
        }
        List<RestaurantDto> results = new ArrayList<>();
        for (RestaurantEntity restaurantEntity : restaurantEntities) {
            // ？？？？？？？
            RestaurantDto restaurantDto = new RestaurantDto(restaurantEntity, groupedMenuItems.get(restaurantEntity.id()));
            results.add(restaurantDto);
        }
        return results;
    }
}

