package com.admin_management_service.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table
public class Category {
    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    private String categoryId;
    //@NotNull
    private String categoryName;
    //@Column(nullable = false, columnDefinition = "boolean default true")
    private boolean enabled;
}
