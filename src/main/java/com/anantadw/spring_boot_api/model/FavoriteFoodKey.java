package com.anantadw.spring_boot_api.model;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class FavoriteFoodKey implements Serializable {
    @Column(name = "user_id")
    private int userId;

    @Column(name = "recipe_id")
    private int recipeId;

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;

        FavoriteFoodKey that = (FavoriteFoodKey) obj;

        return Objects.equals(userId, that.userId) && Objects.equals(recipeId, that.recipeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, recipeId);
    }
}
