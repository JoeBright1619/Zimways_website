package auca.ac.rw.food.delivery.management.service;

import auca.ac.rw.food.delivery.management.model.Cart;
import auca.ac.rw.food.delivery.management.model.CartItem;
import auca.ac.rw.food.delivery.management.model.Customer;
import auca.ac.rw.food.delivery.management.model.Item;
import auca.ac.rw.food.delivery.management.repository.CartItemRepository;
import auca.ac.rw.food.delivery.management.repository.CartRepository;
import auca.ac.rw.food.delivery.management.repository.ItemRepository;
import auca.ac.rw.food.delivery.management.repository.CustomerRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class CartItemService {
    private final CartItemRepository cartItemRepo;
    private final CartRepository cartRepo;
    private final ItemRepository itemRepo;
    private final CustomerRepository customerRepo;

    public CartItemService(CartItemRepository cartItemRepo, CartRepository cartRepo,
                           ItemRepository itemRepo, CustomerRepository customerRepo) {
        this.cartItemRepo = cartItemRepo;
        this.cartRepo = cartRepo;
        this.itemRepo = itemRepo;
        this.customerRepo = customerRepo;
    }

    // ✅ Add an item to the customer's cart (or increase quantity if it exists)
    @Transactional
    public CartItem addItemToCart(UUID customerId, UUID itemId) {
        Customer customer = customerRepo.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        Item item = itemRepo.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item not found"));

        Cart cart = cartRepo.findByCustomer(customer).orElseGet(() -> cartRepo.save(new Cart(customer)));

        Optional<CartItem> existingCartItem = cart.getCartItems()
                .stream()
                .filter(ci -> ci.getItem().getId().equals(itemId))
                .findFirst();

        CartItem cartItem;
        if (existingCartItem.isPresent()) {
            cartItem = existingCartItem.get();
            cartItem.setQuantity(cartItem.getQuantity() + 1);
        } else {
            cartItem = new CartItem(cart, item, 1);
            cart.getCartItems().add(cartItem);
        }

        cartRepo.save(cart); // save relationships
        return cartItemRepo.save(cartItem);
    }

    // ✅ Remove one quantity or delete the item if quantity == 1
    @Transactional
    public void removeItemFromCart(UUID customerId, UUID itemId) {
        Customer customer = customerRepo.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        Cart cart = cartRepo.findByCustomer(customer)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        Optional<CartItem> itemToRemove = cart.getCartItems()
                .stream()
                .filter(ci -> ci.getItem().getId().equals(itemId))
                .findFirst();

        if (itemToRemove.isPresent()) {
            CartItem cartItem = itemToRemove.get();
            if (cartItem.getQuantity() > 1) {
                cartItem.setQuantity(cartItem.getQuantity() - 1);
                cartItemRepo.save(cartItem);
            } else {
                cart.getCartItems().remove(cartItem);
                cartItemRepo.delete(cartItem);
            }
        }
    }

    // ✅ Clear all cart items after placing an order or cancelling
    @Transactional
    public void clearCart(UUID customerId) {
        Customer customer = customerRepo.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        Cart cart = cartRepo.findByCustomer(customer)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        for (CartItem item : cart.getCartItems()) {
            cartItemRepo.delete(item);
        }

        cart.getCartItems().clear();
        cartRepo.save(cart);
    }

    // ✅ Get cart items for a customer (e.g., to display in frontend)
    public List<CartItem> getCartItems(UUID customerId) {
        Customer customer = customerRepo.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        Cart cart = cartRepo.findByCustomer(customer)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        return cart.getCartItems();
    }
}
