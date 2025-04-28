package auca.ac.rw.food.delivery.management.controller;

import auca.ac.rw.food.delivery.management.model.Cart;
import auca.ac.rw.food.delivery.management.model.Customer;
import auca.ac.rw.food.delivery.management.model.MenuItem;
import auca.ac.rw.food.delivery.management.service.CartService;
import auca.ac.rw.food.delivery.management.service.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/carts")
public class CartController {
    private final CartService cartService;
    private final CustomerService customerService;

    public CartController(CartService cartService, CustomerService customerService) {
        this.cartService = cartService;
        this.customerService = customerService;
    }

    // ✅ Get Cart by Customer ID
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<Cart> getCartByCustomer(@PathVariable UUID customerId) {
        Optional<Customer> customer = customerService.getCustomerById(customerId);
        if (customer.isPresent()) {
            Optional<Cart> cart = cartService.getCartByCustomer(customer.get());
            return cart.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
        }
        return ResponseEntity.notFound().build();
    }

    // ✅ Create Cart for Customer (Only needed when a new customer registers)
    @PostMapping("/customer/{customerId}")
    public ResponseEntity<Cart> createCart(@PathVariable UUID customerId) {
        Optional<Customer> customer = customerService.getCustomerById(customerId);
        if (customer.isPresent()) {
            Cart cart = cartService.createCart(customer.get());
            return ResponseEntity.ok(cart);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // ✅ Add Item to Cart
    @PostMapping("/customer/{customerId}/add-item")
    public ResponseEntity<Cart> addItemToCart(@PathVariable UUID customerId, @RequestBody MenuItem menuItem) {
        Optional<Customer> customer = customerService.getCustomerById(customerId);
        if (customer.isPresent()) {
            Cart cart = cartService.addItemToCart(customer.get(), menuItem);
            return ResponseEntity.ok(cart);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // ✅ Remove Item from Cart
    @PostMapping("/customer/{customerId}/remove-item")
    public ResponseEntity<Cart> removeItemFromCart(@PathVariable UUID customerId, @RequestBody MenuItem menuItem) {
        Optional<Customer> customer = customerService.getCustomerById(customerId);
        if (customer.isPresent()) {
            Cart cart = cartService.removeItemFromCart(customer.get(), menuItem);
            return ResponseEntity.ok(cart);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // ✅ Clear Cart after Checkout
    @PostMapping("/customer/{customerId}/checkout")
    public ResponseEntity<Void> checkout(@PathVariable UUID customerId) {
        Optional<Customer> customer = customerService.getCustomerById(customerId);
        if (customer.isPresent()) {
            cartService.clearCart(customer.get());
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // ✅ Delete Cart (Admin only)
    @DeleteMapping("/{cartId}")
    public ResponseEntity<Void> deleteCart(@PathVariable UUID cartId) {
        Optional<Cart> cart = cartService.getCartById(cartId);
        if (cart.isPresent()) {
            cartService.deleteCart(cartId);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
