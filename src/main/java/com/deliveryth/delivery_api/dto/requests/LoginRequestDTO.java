package com.deliveryth.delivery_api.dto.requests;

import com.deliveryth.delivery_api.enums.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequestDTO {
    private String email;
    private String senha;
    private Role role;
}
