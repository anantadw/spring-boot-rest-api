package com.anantadw.spring_boot_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.anantadw.spring_boot_api.entity.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {

}
