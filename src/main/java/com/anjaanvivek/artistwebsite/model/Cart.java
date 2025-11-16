package com.anjaanvivek.artistwebsite.model;

import jakarta.persistence.*;

@Entity
@Table(name = "cart")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "painting_id")
    private Painting painting;

    public Cart() {}

    public Cart(User user, Painting painting) {
        this.user = user;
        this.painting = painting;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Painting getPainting() { return painting; }
    public void setPainting(Painting painting) { this.painting = painting; }
}