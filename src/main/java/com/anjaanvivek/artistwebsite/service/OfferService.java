package com.anjaanvivek.artistwebsite.service;

import com.anjaanvivek.artistwebsite.model.*;
import com.anjaanvivek.artistwebsite.repository.*;
import com.anjaanvivek.artistwebsite.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OfferService {

    @Autowired
    private OfferRepository offerRepository;

    @Autowired
    private PaintingRepository paintingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OfferHistoryRepository offerHistoryRepository;

    @Autowired
    private JwtUtil jwtUtil;

    // ✅ Make or Counter Offer (used by both Buyer and Artist)
    public Offer makeOffer(String token, Long paintingId, Double offerAmount, String message, String role, Long offerId) {
    String email = jwtUtil.validateToken(token.replace("Bearer ", "").trim());
    User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found for token"));
    Painting painting = paintingRepository.findById(paintingId)
            .orElseThrow(() -> new RuntimeException("Painting not found"));

    Offer offerToSave;

    if ("ARTIST".equalsIgnoreCase(role)) {
        // 🔹 Artist is responding — find by offerId
        if (offerId == null) {
            throw new RuntimeException("Offer ID is required when artist responds");
        }

        offerToSave = offerRepository.findById(offerId)
                .orElseThrow(() -> new RuntimeException("Offer not found for given ID"));

        offerToSave.setOfferAmount(offerAmount);
        offerToSave.setMessage(message);
        offerToSave.setStatus("COUNTERED"); // or any artist-specific status
        offerRepository.save(offerToSave);
    } 
    else {
        // 🔹 Buyer is making or updating their offer
        Offer existingOffer = offerRepository.findByUserAndPainting(user, painting).orElse(null);

        if (existingOffer != null) {
            existingOffer.setOfferAmount(offerAmount);
            existingOffer.setMessage(message);
            existingOffer.setStatus("PENDING");
            offerToSave = offerRepository.save(existingOffer);
        } else {
            Offer newOffer = new Offer();
            newOffer.setPainting(painting);
            newOffer.setUser(user);
            newOffer.setOfferAmount(offerAmount);
            newOffer.setMessage(message);
            newOffer.setStatus("PENDING");
            offerToSave = offerRepository.save(newOffer);
        }
    }

    // 🔹 Always log history
    OfferHistory history = new OfferHistory();
    history.setOffer(offerToSave);
    history.setRole(role != null ? role : "BUYER");
    history.setPrice(offerAmount);
    history.setNote(message);
    offerHistoryRepository.save(history);

    return offerToSave;
}

    // ✅ For Buyers — Get their own offers
    public List<Offer> getUserOffers(String token) {
        String email = jwtUtil.validateToken(token.replace("Bearer ", "").trim());
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return offerRepository.findByUser(user);
    }

    // ✅ For Artist — Get All Offers (with user details)
    public List<Offer> getAllOffersForArtist(String token) {
        String email = jwtUtil.validateToken(token.replace("Bearer ", "").trim());
        User artist = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Artist not found"));

        if (!artist.getRole().equalsIgnoreCase("ARTIST")) {
            throw new RuntimeException("Access denied — Only artist can view all offers");
        }

        // ✅ Return all offers (each offer has user and painting info)
        return offerRepository.findAll();
    }

    // ✅ Add to History (for counter or reply)
    public OfferHistory addOfferHistory(Long offerId, String role, Double price, String note) {
        Offer offer = offerRepository.findById(offerId)
                .orElseThrow(() -> new RuntimeException("Offer not found"));

        // When artist replies, update main offer status
        if ("ARTIST".equalsIgnoreCase(role)) {
            offer.setStatus("COUNTERED");
        } else if ("BUYER".equalsIgnoreCase(role)) {
            offer.setStatus("PENDING");
        }

        offerRepository.save(offer);

        OfferHistory history = new OfferHistory();
        history.setOffer(offer);
        history.setRole(role);
        history.setPrice(price);
        history.setNote(note);

        return offerHistoryRepository.save(history);
    }

    // ✅ Get all histories for an offer
    public List<OfferHistory> getOfferHistory(Long offerId) {
        return offerHistoryRepository.findByOfferId(offerId);
    }

}
