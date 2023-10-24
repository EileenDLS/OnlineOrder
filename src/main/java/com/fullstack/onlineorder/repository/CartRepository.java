package com.fullstack.onlineorder.repository;

import com.fullstack.onlineorder.entity.CartEntity;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.ListCrudRepository;

// ListCrudRepository(T, ID), 这里就意味着设定的ID的类型是Long，后面只要使用CartRepository，其ID的data type都要是Long
public interface CartRepository extends ListCrudRepository<CartEntity, Long> {

    // SELECT * FROM carts WHERE customer_id = :customerId
    CartEntity getByCustomerId(Long customerId);


    // 只要是除了get操作，其他操作都要modify
    @Modifying
    @Query("UPDATE carts SET total_price = :totalPrice WHERE id = :cartId")
    void updateTotalPrice(Long cartId, Double totalPrice);
}
