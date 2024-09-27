package com.example.wellnesscoach.service.response;

public record ProductResponse(
        Long productId,
        String targetProductName,
        String productName,
        String productLink,
        Boolean scrap
) {
    public static ProductResponse of(
            Long productId,
            String targetProductName,
            String productName,
            String productLink,
            Boolean scrap
    ) {
        return new ProductResponse(productId, targetProductName, productName, productLink, scrap);
    }
}
