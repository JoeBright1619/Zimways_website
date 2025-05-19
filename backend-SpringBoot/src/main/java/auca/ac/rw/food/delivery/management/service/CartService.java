package auca.ac.rw.food.delivery.management.service;

import auca.ac.rw.food.delivery.management.model.*;
import auca.ac.rw.food.delivery.management.repository.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class CartService {
    private final CartRepository cartRepository;
    private final CustomerRepository customerRepository;
    private final ItemRepository itemRepository;
    private final CartItemRepository cartItemRepository;


    public CartService(CartRepository cartRepository, CustomerRepository customerRepository,
                       ItemRepository itemRepository, CartItemRepository cartItemRepository) {
        this.cartRepository = cartRepository;
        this.customerRepository = customerRepository;
        this.itemRepository = itemRepository;
        this.cartItemRepository = cartItemRepository;
    }

    public List<Cart> getAllCarts() {
        return cartRepository.findAll();
    }

    public Optional<Cart> getCartById(UUID id) {
        return cartRepository.findById(id);
    }

    public Optional<Cart> getCartByCustomer(Customer customer) {
        return cartRepository.findByCustomer(customer);
    }

    public Cart createCart(Customer customer) {
        Cart cart = new Cart(customer);
        return cartRepository.save(cart);
    }

    @Transactional
    public Cart addItemToCart(Cart cart, UUID itemId, int quantity) {
        
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("Item not found"));

        // Check if item already exists in cart
        Optional<CartItem> existing = cart.getCartItems().stream()
                .filter(ci -> ci.getItem().getId().equals(itemId))
                .findFirst();

        if (existing.isPresent()) {
            CartItem cartItem = existing.get();
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
            cartItem.setTotalPrice(cartItem.getQuantity() * item.getPrice());
            cartItemRepository.save(cartItem);
        } else {
            CartItem newCartItem = new CartItem(cart, item, quantity);
            cartItemRepository.save(newCartItem);
        }

        return cartRepository.findById(cart.getId()).get(); // refresh with updated cart items
    }

    public Cart removeItemFromCart(Cart cart, UUID itemId) {
        

        cart.getCartItems().removeIf(ci -> {
            boolean shouldRemove = ci.getItem().getId().equals(itemId);
            if (shouldRemove) cartItemRepository.delete(ci);
            return shouldRemove;
        });

        return cartRepository.save(cart);
    }

    public void clearCart(Cart cart) {
        
        cartItemRepository.deleteAll(cart.getCartItems());
        cart.getCartItems().clear();
        cartRepository.save(cart);
    }

    public void deleteCart(UUID id) {
        cartRepository.deleteById(id);
    }
}
