package com.example.ecommerce.service.impl;

import com.example.ecommerce.domain.Category;
import com.example.ecommerce.domain.Product;
import com.example.ecommerce.domain.Store;
import com.example.ecommerce.dto.request.product.UpdateProductRequest;
import com.example.ecommerce.dto.response.PageResponse;
import com.example.ecommerce.dto.response.ProductBriefInfo;
import com.example.ecommerce.dto.response.ProductDetailedInfo;
import com.example.ecommerce.dto.response.Response;
import com.example.ecommerce.exception.NotFoundException;
import com.example.ecommerce.repository.ProductRepository;
import com.example.ecommerce.repository.StoreRepository;
import com.example.ecommerce.service.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private StoreRepository storeRepository;

    public Product findProductById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Product not found"));
    }

    @Override
    public void save(Product product) {
        productRepository.save(product);
    }

    @Override
    public List<Product> searchProduct(String keyword, Integer pageNumber) {

        return productRepository.findByNameContainingIgnoreCase(keyword);
    }

    @Override
    public ResponseEntity<Response> getReviewByProductId(Long productId) {
        Product product = findProductById(productId);
        return ResponseEntity.ok(Response.builder()
                .status(200)
                .message("Get review by product id successfully")
                .data(product.getReviews())
                .build());
    }




    @Override
    public ResponseEntity<Response> deleteProductById(Long productId) {
        Product product = findProductById(productId); // check if this product exists
        deleteById(productId);
        return ResponseEntity.ok(Response .builder()
                .status(200)
                .message("Delete product successfully")
                .build());
    }

    public void deleteById(Long productId) {
        productRepository.deleteById(productId);
    }
    @Override
    public ResponseEntity<Response> updateProduct(UpdateProductRequest request) {
        //TODO: check if this product belongs to the store
        Product product = findProductById(request.getProductId());


        if (request.getName() != null) product.setName(request.getName());
        if (request.getDescription() != null) product.setDescription(request.getDescription());
        if (request.getCategory() != null) product.setCategory(request.getCategory());
        if (request.getPrice() != null) product.setPrice(request.getPrice());
        if (request.getImages() != null) product.setImages(request.getImages());

        productRepository.save(product);
        return ResponseEntity.ok(Response.builder()
                .status(200)
                .message("Update product successfully")
                .data(null)
                .build());
    }


    @Override
    public ResponseEntity<Response> getProductById(Long productId) {
        Product product = findProductById(productId);
        ProductDetailedInfo productDetailedInfo = new ProductDetailedInfo(product);
        return ResponseEntity.ok(Response.builder()
                .status(200)
                .message("Get product successfully")
                .data(productDetailedInfo)
                .build());
    }

    @Override
    public ResponseEntity<Response> getAllProducts(Integer pageNumber, Integer elementsPerPage, String category, Long storeId, String filter, String sortBy) {

        // Build the pageable
        // Build the specification to filter by category and store


        Pageable pageable = PageRequest.of(pageNumber, elementsPerPage, Sort.by(Sort.Direction.valueOf(sortBy.toUpperCase()), filter));
//        Pageable pageable = PageRequest.of(pageNumber, elementsPerPage);
        boolean isCategory = category != null && !category.equals("all");
        boolean isStore = storeId != null && storeId != 0;
        Product product = new Product();
        if (isCategory) {
            product.setCategory(Category.valueOf(category.toUpperCase()));
        }

        if (isStore) {
            //TODO: custom exception
            Store store = storeRepository.findById(storeId).orElseThrow(() -> new NotFoundException("Store not found"));
            product.setStore(store);
        }

        ExampleMatcher matcher = ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);

        Example<Product> example = Example.of(product, matcher);
        Page<Product> page = productRepository.findAll(example, pageable);
        List<ProductBriefInfo> productBriefInfos = ProductBriefInfo.from(page.getContent());

        PageResponse pageResponse = PageResponse.builder()
                .totalPages(page.getTotalPages())
                .pageNumber(page.getNumber())
                .content(productBriefInfos)
                .build();

        return ResponseEntity.ok(Response.builder()
                .status(200)
                .message("Get all products successfully")
                .data(pageResponse)
                .build());

    }

    //TODO: config the category
    private static class ProductSpecifications {
        public static Specification<Product> categoryEqual(String category) {
            return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("category"), category.toUpperCase());
        }

        public static Specification<Product> storeEqual(Long storeId) {
            return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("store").get("id"), storeId);
        }
    }
}
