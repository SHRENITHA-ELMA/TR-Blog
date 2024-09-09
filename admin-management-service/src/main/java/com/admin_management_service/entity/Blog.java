package com.admin_management_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "blogs")
public class Blog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Lob
    private String content;

    @ElementCollection
    private List<String> images;

    @Column(nullable = false)
    private boolean isEnabled;

    @CreationTimestamp
    private Date createdAt;

    @UpdateTimestamp
    private Date updatedAt;

    @Column(nullable = false)
    private double rating;

    @Column(nullable = false)
    private String userName;

    @Column(nullable = true)
    private String userProfileImage;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String country;

//    @OneToMany(mappedBy = "blog", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Review> reviews;
}

