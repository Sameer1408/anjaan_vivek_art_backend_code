package com.anjaanvivek.artistwebsite.controller;

import com.anjaanvivek.artistwebsite.model.Offer;
import com.anjaanvivek.artistwebsite.service.OfferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/offers")
@CrossOrigin(origins = "*")
public class OfferController {

    @Autowired
    private OfferService offerService;

    // ✅ Make or Counter an Offer
    @PostMapping("/make/{paintingId}")
    public ResponseEntity<?> makeOffer(
            @PathVariable Long paintingId,
            @RequestBody OfferRequest request,
            @RequestHeader("Authorization") String token) {

        Offer offer = offerService.makeOffer(
                token,
                paintingId,
                request.getOfferAmount(),
                request.getMessage(),
                request.getRole(),
                request.getOfferId() // ✅ pass offerId to service
        );

        return ResponseEntity.ok(offer);
    }

    // ✅ Get all offers of the logged-in user (BUYER)
    @GetMapping("/my")
    public ResponseEntity<List<Offer>> getUserOffers(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(offerService.getUserOffers(token));
    }

    // ✅ Get all offers (ARTIST only)
    @GetMapping("/all")
    public ResponseEntity<?> getAllOffersForArtist(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(offerService.getAllOffersForArtist(token));
    }
}

// ✅ DTO
class OfferRequest {
    private Double offerAmount;
    private String message;
    private String role; // "BUYER" or "ARTIST"
    private Long offerId; 

    public Double getOfferAmount() { return offerAmount; }
    public void setOfferAmount(Double offerAmount) { this.offerAmount = offerAmount; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    
	public Long getOfferId() {
		return offerId;
	}
	public void setOfferId(Long offerId) {
		this.offerId = offerId;
	}
}
