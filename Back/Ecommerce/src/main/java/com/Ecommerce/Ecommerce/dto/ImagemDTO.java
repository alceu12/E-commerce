package com.Ecommerce.Ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class ImagemDTO {
    private Long id;
    private String url;
    private byte[] dados;
}
