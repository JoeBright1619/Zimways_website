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
    @PostMapping("/customer/{customerId}/add-item")
    public ResponseEntity<Cart> addItemToCustomerCart(
            @PathVariable UUID customerId,
            @RequestBody CartDTO request
    ) {
        return customerService.getCustomerById(customerId)
                .map(customer -> {
                    Cart cart = cartService.getCartByCustomer(customer)
                            .orElseGet(() -> cartService.createCart(customer));

                    Cart updated = cartService.addItemToCart(cart, request.getItemId(), request.getQuantity());
                    return ResponseEntity.ok(updated);
                })
                .orElse(ResponseEntity.notFound().build());
    }



    // ✅ Remove Item from Cart
    @PostMapping("/customer/{customerId}/remove-item")
    public ResponseEntity<Cart> removeItemFromCart(
            @PathVariable UUID customerId,
            @RequestBody CartDTO request
    ) {
        Optional<Customer> customerOpt = customerService.getCustomerById(customerId);
        if (customerOpt.isEmpty()) return ResponseEntity.notFound().build();

        Optional<Cart> cartOpt = cartService.getCartByCustomer(customerOpt.get());
        if (cartOpt.isEmpty()) return ResponseEntity.notFound().build();

        Cart updated = cartService.removeItemFromCart(cartOpt.get(), request.getItemId(), request.getQuantity());
        return ResponseEntity.ok(updated);
    }



    // ✅ Clear Cart after Checkout
    @PostMapping("/customer/{customerId}/checkout")
public ResponseEntity<String> checkoutCustomerCart(@PathVariable UUID customerId) {
    return customerService.getCustomerById(customerId)
            .flatMap(customer -> cartService.getCartByCustomer(customer))
            .map(cart -> {
                cartService.clearCart(cart);
                return ResponseEntity.ok("Cart cleared successfully");
            })
            .orElse(ResponseEntity.notFound().build());
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

    // ✅ Delete Cart Item completely
    @DeleteMapping("/customer/{customerId}/items/{itemId}")
    public ResponseEntity<Cart> deleteCartItem(
            @PathVariable UUID customerId,
            @PathVariable UUID itemId
    ) {
        Optional<Customer> customerOpt = customerService.getCustomerById(customerId);
        if (customerOpt.isEmpty()) return ResponseEntity.notFound().build();

        Optional<Cart> cartOpt = cartService.getCartByCustomer(customerOpt.get());
        if (cartOpt.isEmpty()) return ResponseEntity.notFound().build();

        Cart updated = cartService.deleteCartItem(cartOpt.get(), itemId);
        return ResponseEntity.ok(updated);
    }
}
