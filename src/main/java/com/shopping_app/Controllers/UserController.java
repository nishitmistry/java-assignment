package com.shopping_app.Controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.shopping_app.Constants.OrderStatus;
import com.shopping_app.DTOs.TransactionDTO;
import com.shopping_app.Models.Inventory;
import com.shopping_app.Models.Order;
import com.shopping_app.Models.Transaction;
import com.shopping_app.Models.User;
import com.shopping_app.Repositories.CouponRepository;
import com.shopping_app.Repositories.InventroyRepository;
import com.shopping_app.Repositories.OrderRepository;
import com.shopping_app.Repositories.TransactionRepostitory;
import com.shopping_app.Repositories.UserRepository;
import com.shopping_app.Shared.Primatives.Error;
import com.shopping_app.Shared.Primatives.Result;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/User")
public class UserController {
    @Autowired
    private CouponRepository couponRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private InventroyRepository inventroyRepository;
    @Autowired
    private TransactionRepostitory transactionRepostitory;

    @PostMapping()
    public ResponseEntity<User> postMethodName() {
        User user = new User();
        userRepository.save(user);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/{UserId}/order")
    public ResponseEntity PostOrders(@PathVariable int UserId, @RequestParam int qty, @RequestParam String coupon) {
        System.out.println(UserId);
        Result result = ValidatePostOrder(UserId, qty, coupon);
        if (result.getIsFailure()) {
            return ResponseEntity
                    .of(ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, result.getError().getMessage()))
                    .build();
        }
        AddOrdersCommand(UserId, qty, coupon);
        return ResponseEntity.ok(null);
    }

    @GetMapping("/{UserId}/order")
    public ResponseEntity<Iterable<Order>> getUserOrders(@PathVariable int UserId) {
        Result result = ValidateGetOrders(UserId);
        if (result.getIsFailure()) {
            return ResponseEntity
                    .of(ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, result.getError().getMessage()))
                    .build();
        }
        return ResponseEntity.ok().body(GetOrderQuery(UserId));
    }

    @PostMapping("/{UserId}/{OrderId}/pay")
    public ResponseEntity<TransactionDTO> payOrder(@PathVariable int UserId, @PathVariable int OrderId, @RequestParam double amount) {
        Result result = ValidatePayOrder(UserId, OrderId, amount);
        if (result.getIsFailure()) {
            return ResponseEntity
                    .of(ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, result.getError().getMessage()))
                    .build();
        }
        return payOrderCommand(UserId, OrderId, amount);
    }

    @GetMapping("/{userId}/order/{orderId}")
    public ResponseEntity<List<Transaction>> getTranactions(@PathVariable int userId, @PathVariable int orderId) {
        Result result = ValidateGetTranactions(userId, orderId);
        if (result.getIsFailure()) {
            return ResponseEntity
                    .of(ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, result.getError().getMessage()))
                    .build();
        }
        return ResponseEntity.ok().body(transactionRepostitory.findByOrderId(orderId));
    }

    private Result ValidateGetTranactions(int userId, int orderId) {
        String ErrorCode = "User.GetTransaction";
        Optional<User> user = userRepository.findById(userId);
        if (!user.isPresent()) {
            return Result.Failure(new Error(ErrorCode,
                    "User Does Not Exist"));
        }
        if (!orderRepository.findById(orderId).isPresent()) {
            return Result.Failure(new Error(ErrorCode,
                    "Order Does Not Exist"));
        }
        return Result.Success();
    }

    private ResponseEntity<TransactionDTO> payOrderCommand(int userId, int OrderId, double amount) {
        Optional<Order> Optionalorder = orderRepository.findById(OrderId);
        if (!Optionalorder.isPresent()) {
            Transaction transaction = CreateTransaction(new Order(), OrderStatus.FAILED);

            return new ResponseEntity<>(
                    new TransactionDTO(userId,
                            OrderId,
                            transaction.getTransactionId(),
                            OrderStatus.FAILED,
                            "Payment Failed due to invalid order id"),
                            new HttpHeaders(),
                    HttpStatus.BAD_REQUEST);
        }
        Order order = orderRepository.findById(OrderId).get();
        if (transactionRepostitory.existsByStatusAndOrderId(OrderStatus.SUCCESS, OrderId)) {
            Transaction transaction = CreateTransaction(order, OrderStatus.FAILED);

            return new ResponseEntity<>(
                    new TransactionDTO(userId,
                            OrderId,
                            transaction.getTransactionId(),
                            OrderStatus.FAILED,
                            "Order is already paid for"),
                            new HttpHeaders(),
                    HttpStatus.METHOD_NOT_ALLOWED);
        }

        if (order.getAmount() != amount) {
            Transaction transaction = CreateTransaction(order, OrderStatus.FAILED);
            return new ResponseEntity<>(
                    new TransactionDTO(userId,
                            OrderId,
                            transaction.getTransactionId(),
                            OrderStatus.FAILED,
                            "Payment Failed as amount is invalid"),
                            new HttpHeaders(),
                    HttpStatus.BAD_REQUEST);
        }
        Random rn = new Random();
        int random = rn.nextInt(3);
        switch (random) {
            case 0: {
                Transaction transaction = CreateTransaction(order, OrderStatus.SUCCESS);

                return new ResponseEntity<>(
                        new TransactionDTO(userId,
                                OrderId,
                                transaction.getTransactionId(),
                                OrderStatus.SUCCESS,
                                ""),
                                new HttpHeaders(),
                        HttpStatus.OK);
            }
            case 1: {
                Transaction transaction = CreateTransaction(order, OrderStatus.FAILED);
                return new ResponseEntity<>(
                        new TransactionDTO(userId,
                                OrderId,
                                transaction.getTransactionId(),
                                OrderStatus.FAILED,
                                "No response from payment server"),
                                new HttpHeaders(),
                        HttpStatus.GATEWAY_TIMEOUT);
            }
            case 2: {
                Transaction transaction = CreateTransaction(order, OrderStatus.FAILED);
                return new ResponseEntity<>(
                        new TransactionDTO(userId,
                                OrderId,
                                transaction.getTransactionId(),
                                OrderStatus.FAILED,
                                "Payment Failed from bank"),
                                new HttpHeaders(),
                        HttpStatus.BAD_REQUEST);
            }
        }
        return ResponseEntity.internalServerError().body(new TransactionDTO());

    }

    private Transaction CreateTransaction(Order order, String status) {
        Transaction transaction = new Transaction(UUID.randomUUID().toString(),
                LocalDateTime.now(),
                status, order);
        transactionRepostitory.save(transaction);
        return transaction;
    }

    private Result ValidatePayOrder(int userId, int OrderId, double amount) {
        String ErrorCode = "User.PayOrder";
        Optional<User> user = userRepository.findById(userId);

        if (!user.isPresent()) {
            return Result.Failure(new Error(ErrorCode,
                    "User Does Not Exist"));
        }
        return Result.Success();
    }

    private Iterable<Order> GetOrderQuery(int UserId) {
        User user = userRepository.findById(UserId).get();
        List<Integer> OrderIds = user.getOrderIds();
        return orderRepository.findAllById(OrderIds);
    }

    private Result ValidateGetOrders(int UserId) {
        String ErrorCode = "User.PostOrder";
        Optional<User> user = userRepository.findById(UserId);
        if (!user.isPresent()) {
            return Result.Failure(new Error(ErrorCode,
                    "User Does Not Exist"));
        }
        return Result.Success();
    }

    private void AddOrdersCommand(int UserId, int qty, String coupon) {
        // Adding the used Coupon
        User User = userRepository.findById(UserId).get();
        User.getCouponsUsed().add(coupon);

        // assuming the price of product to be 100
        double amount = coupon== ""? (100*qty) :  (100 - couponRepository.findById(coupon).get().getDiscount()) / 100 * (qty * 100);
        Order order = new Order(amount, qty, LocalDateTime.now(), coupon);
        orderRepository.save(order);

        // Managing Inventory qty
        Inventory inventory = inventroyRepository.findById(1).get();
        inventory.setAvailable(inventory.getAvailable() - qty);
        inventory.setOrdered(inventory.getOrdered() + qty);
        inventroyRepository.save(inventory);

        // adding the orderid to user
        User.getOrderIds().add(order.getOrderId());
        userRepository.save(User);
    }

    private Result ValidatePostOrder(int UserId, int qty, String Coupon) {
        String ErrorCode = "User.PostOrder";
        Optional<User> user = userRepository.findById(UserId);
        if (!user.isPresent()) {
            return Result.Failure(new Error(ErrorCode,
                    "User Does Not Exist"));
        }
        User ExistingUser = user.get();
        if (qty <= 0) {
            return Result.Failure(new Error(ErrorCode,
                    "Qty cannot be less or equal to 0"));
        }
        if (Coupon!="" && (!couponRepository.findById(Coupon).isPresent() || ExistingUser.getCouponsUsed().contains(Coupon))) {
            return Result.Failure(new Error(ErrorCode,
                    "Coupon is not Valid"));
        }
        if (!inventroyRepository.findById(1).isPresent()
                && inventroyRepository.findById(1).get().getAvailable() < qty) {
            return Result.Failure(new Error(ErrorCode,
                    "Product is not available"));

        }
        return Result.Success();
    }

}
