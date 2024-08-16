package com.anantadw.spring_boot_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.anantadw.spring_boot_api.entity.FavoriteFood;
import com.anantadw.spring_boot_api.entity.FavoriteFoodKey;

@Repository
public interface FavoriteFoodRepository extends JpaRepository<FavoriteFood, FavoriteFoodKey> {

}
