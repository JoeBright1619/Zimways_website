package auca.ac.rw.food.delivery.management.service;

import auca.ac.rw.food.delivery.management.model.Cart;
import auca.ac.rw.food.delivery.management.model.Customer;
import auca.ac.rw.food.delivery.management.model.MenuItem;
import auca.ac.rw.food.delivery.management.repository.CartRepository;
import auca.ac.rw.food.delivery.management.repository.CustomerRepository;
import auca.ac.rw.food.delivery.management.repository.MenuItemRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CartService {
    private final CartRepository cartRepository;
    private final CustomerRepository customerRepository;
    private final MenuItemRepository menuRepo;

    public CartService(CartRepository cartRepository, CustomerRepository customerRepository,  MenuItemRepository menuRepo) {
        this.cartRepository = cartRepository;
        this.customerRepository = customerRepository;
        this.menuRepo  =  menuRepo;
    }

    // ✅ Get all carts (mainly for debugging)
    public List<Cart> getAllCarts() {
        return cartRepository.findAll();
    }

    // ✅ Get a cart by its ID (optional, useful for admin purposes)
    public Optional<Cart> getCartById(UUID id) {
        return cartRepository.findById(id);
    }

    // ✅ Get a cart by Customer (most useful)
    public Optional<Cart> getCartByCustomer(Customer customer) {
        return cartRepository.findByCustomer(customer);
    }

    // ✅ Create a new cart for a customer (Only needed when a new customer registers)
    public Cart createCart(Customer customer) {
        Cart cart = new Cart(customer);
        return cartRepository.save(cart);
    }

    // ✅ Add a menu item to the cart
    @Transactional
    public Cart addItemToCart(Customer customer, MenuItem menuItem) {
        // Check if menuItem price is valid (not null, not zero, and not negative)
        
    
        Cart cart = cartRepository.findByCustomer(customer).orElseGet(() -> createCart(customer));
        Optional<MenuItem> item = menuRepo.findById(menuItem.getId());
        // Add the MenuItem to the cart
        if (!item.isPresent()) {
            throw new IllegalArgumentException("MenuItem not found.");
        }

        cart.getMenuItems().add(item.get());
        cart.setQuantity(cart.getQuantity() + 1);
    
        // Print out the MenuItem (optional, for debugging)
        System.out.println(item);
    
        // Update the total amount of the cart
        cart.setAmount(cart.getAmount() + item.get().getPrice());
        System.out.println("Updated cart amount: " + cart.getAmount());
    
        return cartRepository.save(cart);
    }
    

    // ✅ Remove a menu item from the cart
    public Cart removeItemFromCart(Customer customer, MenuItem menuItem) {
        Cart cart = cartRepository.findByCustomer(customer).orElseThrow(() -> new RuntimeException("Cart not found"));
        Optional<MenuItem> item = menuRepo.findById(menuItem.getId());
        
        // Add the MenuItem to the cart
        if (!item.isPresent()) {
            throw new IllegalArgumentException("MenuItem not found.");
        }
    
        MenuItem menuItemFound = item.get(); // Retrieve the MenuItem from the Optional
    
        // Remove the MenuItem from the cart
        if (cart.getMenuItems().remove(menuItemFound)) {
            cart.setQuantity(cart.getQuantity() - 1);
            cart.setAmount(cart.getAmount() - menuItemFound.getPrice()); // Use menuItemFound to get the price
        }
    
        return cartRepository.save(cart);
    }
    

    // ✅ Clear the cart after checkout
    public void clearCart(Customer customer) {
        Cart cart = cartRepository.findByCustomer(customer).orElseThrow(() -> new RuntimeException("Cart not found"));

        cart.getMenuItems().clear();
        cart.setQuantity(0);
        cart.setAmount(0.0);

        cartRepository.save(cart);
    }

    // ✅ Delete cart (mainly for admin purposes)
    public void deleteCart(UUID id) {
        cartRepository.deleteById(id);
    }
}
