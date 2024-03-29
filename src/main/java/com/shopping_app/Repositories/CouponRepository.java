package com.shopping_app.Repositories;

import org.springframework.data.repository.CrudRepository;

import com.shopping_app.Models.Coupon;

public interface CouponRepository extends CrudRepository<Coupon,String> {
    
}
