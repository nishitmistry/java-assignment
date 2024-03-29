package com.shopping_app.Repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.shopping_app.Models.Transaction;

public interface TransactionRepostitory extends CrudRepository<Transaction,String> {
    List<Transaction> findByOrderId(int orderId);
    boolean existsByStatusAndOrderId(String status, int orderId);
}
