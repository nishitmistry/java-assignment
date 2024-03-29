package com.shopping_app.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shopping_app.Models.Inventory;
import com.shopping_app.Repositories.InventroyRepository;
import org.springframework.web.bind.annotation.PostMapping;



@RestController
@RequestMapping("/api/Inventory")
public class InventoryController {
    @Autowired
    private InventroyRepository inventroyRepository;
    @PostMapping
    public ResponseEntity postMethodName() {
        //Assuming that we have only one product with one price only of 100 qty
       Inventory inventory = new Inventory(0, 100.0, 100);
       inventroyRepository.save(inventory);
       return ResponseEntity.ok().body(null);
    }
    
    @GetMapping
    public ResponseEntity<Iterable<Inventory>> GetInventory(){
        return ResponseEntity.ok().body(inventroyRepository.findAll());
    }
}
