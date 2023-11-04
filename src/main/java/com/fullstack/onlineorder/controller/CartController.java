package com.fullstack.onlineorder.controller;

import com.fullstack.onlineorder.model.AddToCartBody;
import com.fullstack.onlineorder.model.CartDto;
import com.fullstack.onlineorder.service.CartService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CartController {

    // depend on cartService and customerService
    private final CartService cartService;


    public CartController(CartService cartService) {
        this.cartService = cartService;
    }


    // Get request: read
    @GetMapping("/cart")
    public CartDto getCart() {
        // return the cart info of user1 (buz customerService not finished yet)
        return cartService.getCart(1L);
    }


    // Post request: update - 改变存储对象的状态时，就用post
    @PostMapping("/cart")
    public void addToCart(@RequestBody AddToCartBody body) {
        cartService.addMenuItemToCart(1L, body.menuId());
    }
    // @RequestBody：get the menuId to add menuItem to cart


    // 不用delete而是post的原因在于，我们不是删除cart，只是因为付钱而清空了购物车
    @PostMapping("/cart/checkout")
    public void checkout() {
        cartService.clearCart(1L);
    }
}

