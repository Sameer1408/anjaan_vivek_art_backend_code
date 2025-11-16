package com.anjaanvivek.artistwebsite.repository;

import com.anjaanvivek.artistwebsite.model.Cart;
import com.anjaanvivek.artistwebsite.model.User;
import com.anjaanvivek.artistwebsite.model.Painting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    List<Cart> findByUser(User user);

    Optional<Cart> findByUserAndPainting(User user, Painting painting);
}