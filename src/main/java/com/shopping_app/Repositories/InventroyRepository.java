package com.shopping_app.Repositories;
import com.shopping_app.Models.Inventory;
import org.springframework.data.repository.CrudRepository;
public interface InventroyRepository extends CrudRepository<Inventory, Integer> {
    
}
