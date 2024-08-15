package com.anantadw.spring_boot_api.model;

import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "recipes")
public class Recipe extends Auditable {
    @Id
    @Column(name = "recipe_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "recipe_name", nullable = false)
    private String name;

    @Column(name = "image_filename")
    private String image;

    @Column(name = "time_cook", nullable = false)
    private int timeCook;

    @Column(nullable = false)
    private String ingredient;

    @Column(name = "how_to_cook", nullable = false)
    private String howToCook;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted = false;

    // * Relationships
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "level_id")
    private Level level;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "recipe")
    private Set<FavoriteFood> favoriteFoods;
}
