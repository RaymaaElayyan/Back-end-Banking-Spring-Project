package org.example.itsmybaking.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "user")  // Make sure this matches your actual table name
public class user {  // Renamed to follow Java naming conventions
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonProperty("password")
    private String password;

    @JsonProperty("username")
    private String username;

    @Column(name = "alternativephonenumber")
    private String alternativePhoneNumber;

    @Column(name = "othername")
    private String otherName;

    private String firstName;
    private String lastName;
    private String stateOfOrigin;
    private String email;
    private String phoneNumber;
    private String address;
    private String gender;
    private String accountNumber;
    private BigDecimal accountBalance;
    private String status;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime modifiedAt;
}
