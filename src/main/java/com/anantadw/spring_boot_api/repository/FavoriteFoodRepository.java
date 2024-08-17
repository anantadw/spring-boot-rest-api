package com.anantadw.spring_boot_api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.anantadw.spring_boot_api.entity.FavoriteFood;
import com.anantadw.spring_boot_api.entity.FavoriteFoodKey;

@Repository
public interface FavoriteFoodRepository extends JpaRepository<FavoriteFood, FavoriteFoodKey> {
    Optional<FavoriteFood> findByUserIdAndRecipeId(int userId, int recipeId);

    List<FavoriteFood> findByUserId(int userId);
}
