package com.shopping_app.Repositories;

import org.springframework.data.repository.CrudRepository;

import com.shopping_app.Models.User;

public interface UserRepository extends CrudRepository<User,Integer> {
    
}
