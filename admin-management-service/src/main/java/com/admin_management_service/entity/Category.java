package com.admin_management_service.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
//@Table
public class Category {
    @Id
    private String categoryId;
    private String categoryName;
    private boolean enabled;
}
