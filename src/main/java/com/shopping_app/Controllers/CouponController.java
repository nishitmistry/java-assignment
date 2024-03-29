package com.shopping_app.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shopping_app.Models.Coupon;
import com.shopping_app.Repositories.CouponRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/Coupon")
public class CouponController {
    @Autowired
    private CouponRepository couponRepository;

    @PostMapping()
    public void postCoupon(@RequestBody CouponDto request) {
        Coupon coupon = new Coupon(request.CouponCode,request.Discount);
        couponRepository.save(coupon);
    }
    @GetMapping
    public ResponseEntity<Iterable<Coupon>> getCoupons() {
        return ResponseEntity.ok(couponRepository.findAll());
    }
    

}

class CouponDto {
    public String CouponCode;
    public double Discount;
    public CouponDto() {}
    public CouponDto(String couponCode, double discount) {
        CouponCode = couponCode;
        Discount = discount;
    }
    public String getCouponCode() {
        return CouponCode;
    }
    public void setCouponCode(String couponCode) {
        CouponCode = couponCode;
    }
    public double getDiscount() {
        return Discount;
    }
    public void setDiscount(double discount) {
        Discount = discount;
    }
}