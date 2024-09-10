package org.example.itsmybaking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {
    private Long id;
    private String firstName;
    private String lastName;  // Ensure the field name matches the getter and setter
    private String otherName;
    private String stateOfOrigin;
    private String email;
    private String phoneNumber;
    private String address;
    private String gender;
    private String alternativePhoneNumber;
}
