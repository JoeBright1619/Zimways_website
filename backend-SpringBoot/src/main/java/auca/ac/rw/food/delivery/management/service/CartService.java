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

        // 👇 ADD THIS LINE
        cart.getCartItems().add(newCartItem);

        cartItemRepository.save(newCartItem);
    }

    return cartRepository.save(cart);  // Persist changes to Cart and its items
}


    @Transactional
    public Cart removeItemFromCart(Cart cart, UUID itemId, int quantityToRemove) {
        for (CartItem ci : cart.getCartItems()) {
            if (ci.getItem().getId().equals(itemId)) {
                int currentQty = ci.getQuantity();

                if (currentQty > quantityToRemove) {
                    ci.setQuantity(currentQty - quantityToRemove);
                    ci.setTotalPrice(ci.getQuantity() * ci.getItem().getPrice());
                    cartItemRepository.save(ci);
                } else {
                    // If quantity is 1 or less than or equal to quantityToRemove, do not remove completely
                    // but leave at minimum 1
                    if (currentQty > 1) {
                        ci.setQuantity(1);
                        ci.setTotalPrice(ci.getItem().getPrice());
                        cartItemRepository.save(ci);
                    }
                }
                break; // Item found and processed, break loop
            }
        }

        return cartRepository.findById(cart.getId()).orElse(cart);
    }


    public void clearCart(Cart cart) {
        
        cartItemRepository.deleteAll(cart.getCartItems());
        cart.getCartItems().clear();
        cartRepository.save(cart);
    }

    public void deleteCart(UUID id) {
        cartRepository.deleteById(id);
    }

    @Transactional
    public Cart deleteCartItem(Cart cart, UUID itemId) {
        CartItem itemToRemove = null;
        for (CartItem ci : cart.getCartItems()) {
            if (ci.getItem().getId().equals(itemId)) {
                itemToRemove = ci;
                break;
            }
        }

        if (itemToRemove != null) {
            cart.getCartItems().remove(itemToRemove);
            cartItemRepository.delete(itemToRemove);
        }

        return cartRepository.save(cart);
    }
}
