package com.anantadw.spring_boot_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.anantadw.spring_boot_api.model.Level;

@Repository
public interface LevelRepository extends JpaRepository<Level, Integer> {

}
