package com.shopping_app.Repositories;

import org.springframework.data.repository.CrudRepository;

import com.shopping_app.Models.Order;

public interface OrderRepository extends CrudRepository<Order,Integer> {
    
}
