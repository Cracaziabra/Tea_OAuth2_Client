package com.example.oauth2_client.tea;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor(force = true)
public class Tea {

    private Long id;

    private String name;
    private String color;
    private String origin;

}
