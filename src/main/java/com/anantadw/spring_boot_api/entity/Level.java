package com.anantadw.spring_boot_api.entity;

import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Table(name = "levels")
public class Level extends Auditable {
    @Id
    @Column(name = "level_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "level_name", nullable = false)
    private String name;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted = false;

    // * Relationships
    @OneToMany(mappedBy = "level")
    private Set<Recipe> recipes;
}
