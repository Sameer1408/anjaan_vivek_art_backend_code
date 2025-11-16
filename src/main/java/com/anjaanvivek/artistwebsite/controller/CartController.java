package com.anjaanvivek.artistwebsite.controller;

import com.anjaanvivek.artistwebsite.model.Painting;
import com.anjaanvivek.artistwebsite.service.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    // ✅ Add to cart
    @PostMapping("/add/{paintingId}")
    public ResponseEntity<String> addToCart(
            @RequestHeader("Authorization") String token,
            @PathVariable Long paintingId) {

        String jwt = token.replace("Bearer ", "");
        String result = cartService.addToCart(jwt, paintingId);
        return ResponseEntity.ok(result);
    }

    // ✅ Remove from cart
    @DeleteMapping("/remove/{paintingId}")
    public ResponseEntity<String> removeFromCart(
            @RequestHeader("Authorization") String token,
            @PathVariable Long paintingId) {

        String jwt = token.replace("Bearer ", "");
        String result = cartService.removeFromCart(jwt, paintingId);
        return ResponseEntity.ok(result);
    }

    // ✅ Get all paintings in cart
    @GetMapping("/my")
    public ResponseEntity<List<Painting>> getMyCart(
            @RequestHeader("Authorization") String token) {

        String jwt = token.replace("Bearer ", "");
        List<Painting> paintings = cartService.getUserCart(jwt);
        return ResponseEntity.ok(paintings);
    }
}