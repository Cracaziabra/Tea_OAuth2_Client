package com.example.oauth2_client.tea;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(force = true)
public class Tea {

    private Long id;

    private String name;
    private String color;
    private String origin;

}
