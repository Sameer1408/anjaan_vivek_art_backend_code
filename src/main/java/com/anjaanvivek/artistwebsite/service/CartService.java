package com.anjaanvivek.artistwebsite.service;

import com.anjaanvivek.artistwebsite.model.Cart;
import com.anjaanvivek.artistwebsite.model.Painting;
import com.anjaanvivek.artistwebsite.model.User;
import com.anjaanvivek.artistwebsite.repository.CartRepository;
import com.anjaanvivek.artistwebsite.repository.PaintingRepository;
import com.anjaanvivek.artistwebsite.repository.UserRepository;
import com.anjaanvivek.artistwebsite.security.JwtUtil;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final PaintingRepository paintingRepository;
    private final JwtUtil jwtUtil;

    public CartService(CartRepository cartRepository,
                       UserRepository userRepository,
                       PaintingRepository paintingRepository,
                       JwtUtil jwtUtil) {
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
        this.paintingRepository = paintingRepository;
        this.jwtUtil = jwtUtil;
    }

    // Helper: Extract user from token
    private User getUserFromToken(String token) {
        String email = jwtUtil.validateToken(token);
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
    }

    // Add to cart
    public String addToCart(String token, Long paintingId) {
        User user = getUserFromToken(token);
        Painting painting = paintingRepository.findById(paintingId)
                .orElseThrow(() -> new RuntimeException("Painting not found with ID: " + paintingId));

        Optional<Cart> existing = cartRepository.findByUserAndPainting(user, painting);
        if (existing.isPresent()) {
            return "Already in cart";
        }

        Cart cart = new Cart(user, painting);
        cartRepository.save(cart);
        return "Added to cart";
    }

    // Remove from cart
    public String removeFromCart(String token, Long paintingId) {
        User user = getUserFromToken(token);
        Painting painting = paintingRepository.findById(paintingId)
                .orElseThrow(() -> new RuntimeException("Painting not found with ID: " + paintingId));

        Optional<Cart> existing = cartRepository.findByUserAndPainting(user, painting);
        if (existing.isEmpty()) {
            throw new RuntimeException("Item not found in cart");
        }

        cartRepository.delete(existing.get());
        return "Removed from cart";
    }

    // Get all paintings in user's cart
    public List<Painting> getUserCart(String token) {
        User user = getUserFromToken(token);

        return cartRepository.findByUser(user)
                .stream()
                .map(Cart::getPainting)
                .collect(Collectors.toList());
    }
}
