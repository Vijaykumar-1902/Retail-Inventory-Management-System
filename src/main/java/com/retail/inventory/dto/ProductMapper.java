package com.retail.inventory.dto;

import com.retail.inventory.entity.Product;

public class ProductMapper {

    public static ProductDTO toDTO(Product product) {

        ProductDTO dto = new ProductDTO();

        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setQuantity(product.getQuantity());

        if (product.getCategory() != null) {
            dto.setCategoryId(product.getCategory().getId());
        }

        return dto;
    }
}