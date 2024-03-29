package com.example.ecommerce.domain;

import com.example.ecommerce.dto.response.CustomerBriefInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cascade;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private double rating;
    @Column(columnDefinition="LONGTEXT")
    private String comment;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    @ElementCollection
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    List<String> images;

    @JsonIgnore
    @ManyToOne
    private Product product;

    @ManyToOne
    private Customer customer;

    public CustomerBriefInfo getCustomer() {
        return new CustomerBriefInfo(customer); // only return necessary information, hide sensitive information
    }

}
