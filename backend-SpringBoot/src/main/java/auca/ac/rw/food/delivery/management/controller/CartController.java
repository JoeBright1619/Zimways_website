package auca.ac.rw.food.delivery.management.controller;

import auca.ac.rw.food.delivery.management.DTO.CartDTO;
import auca.ac.rw.food.delivery.management.model.Cart;
import auca.ac.rw.food.delivery.management.model.Customer;
import auca.ac.rw.food.delivery.management.model.Item;
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
         return customerService.getCustomerById(customerId)
                .flatMap(cartService::getCartByCustomer)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ✅ Create Cart for Customer (Only needed when a new customer registers)
    @PostMapping("/customer/{customerId}")
    public ResponseEntity<Cart> createCart(@PathVariable UUID customerId) {
        return customerService.getCustomerById(customerId)
                .map(customer -> ResponseEntity.ok(cartService.createCart(customer)))
                .orElse(ResponseEntity.notFound().build());

    }

        
    

    // ✅ Add Item to Cart
      @PostMapping("/{cartId}/add-item")
    public ResponseEntity<Cart> addItemToCart(
            @PathVariable UUID cartId,
            @RequestBody CartDTO request
    ) {
        Optional<Cart> cart = cartService.getCartById(cartId);
        return cart.map(c -> {
            Cart updated = cartService.addItemToCart(c, request.getItemId(), request.getQuantity());
            return ResponseEntity.ok(updated);
        }).orElse(ResponseEntity.notFound().build());
    }



    // ✅ Remove Item from Cart
     @PostMapping("/{cartId}/remove-item")
    public ResponseEntity<Cart> removeItemFromCart(
            @PathVariable UUID cartId,
            @RequestBody CartDTO request
    ) {
        Optional<Cart> cart = cartService.getCartById(cartId);
        return cart.map(c -> {
            Cart updated = cartService.removeItemFromCart(c, request.getItemId());
            return ResponseEntity.ok(updated);
        }).orElse(ResponseEntity.notFound().build());
    }

    // ✅ Clear Cart after Checkout
    @PostMapping("/{cartId}/checkout")
    public ResponseEntity<Void> checkout(@PathVariable UUID cartId) {
        Optional<Cart> cart = cartService.getCartById(cartId);
        if (cart.isPresent()) {
            cartService.clearCart(cart.get());
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
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
