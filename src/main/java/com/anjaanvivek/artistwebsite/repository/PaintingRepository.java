package com.anjaanvivek.artistwebsite.repository;

import com.anjaanvivek.artistwebsite.model.Painting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaintingRepository extends JpaRepository<Painting, Long> {
}
