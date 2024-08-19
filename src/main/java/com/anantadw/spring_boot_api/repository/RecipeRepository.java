package com.anantadw.spring_boot_api.repository;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.anantadw.spring_boot_api.entity.Recipe;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Integer>, JpaSpecificationExecutor<Recipe> {
    boolean existsByName(String name);

    interface Specs {
        static Specification<Recipe> recipeNameContains(String recipeName) {
            return (root, query, criteriaBuilder) -> {
                String lowerCaseRecipeName = "%" + recipeName.toLowerCase() + "%";
                return criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), lowerCaseRecipeName);
            };
        }

        static Specification<Recipe> levelIdEquals(int levelId) {
            return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("level").get("id"), levelId);
        }

        static Specification<Recipe> categoryIdEquals(int categoryId) {
            return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("category").get("id"), categoryId);
        }

        static Specification<Recipe> timeEquals(int time) {
            return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("time"), time);
        }

        static Specification<Recipe> notDeleted() {
            return (root, query, criteriaBuilder) -> criteriaBuilder.isFalse(root.get("isDeleted"));
        }
    }
}
