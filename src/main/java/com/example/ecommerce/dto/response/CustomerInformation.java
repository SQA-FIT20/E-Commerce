package com.example.ecommerce.dto.response;

import com.example.ecommerce.domain.Customer;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class CustomerInformation {
    private Long id;
    private String name;
    private String email;
    private List<String> addresses;
    private String phoneNumber;

    private String avatar;

    public CustomerInformation(Customer customer) {
        this.id = customer.getId();
        this.name = customer.getName();
        this.email = customer.getEmail();
        this.addresses = customer.getAddresses();
        this.phoneNumber = customer.getPhoneNumber();
        this.avatar = customer.getAvatar();
    }

    public CustomerInformation(Long id, String name, String email, List<String> addresses, String phoneNumber, String avatar) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.addresses = addresses;
        this.phoneNumber = phoneNumber;
        this.avatar = avatar;
    }
}
