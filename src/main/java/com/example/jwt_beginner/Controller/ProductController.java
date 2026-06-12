package com.example.jwt_beginner.Controller;

import com.example.jwt_beginner.DTO.CreateProductRequest;
import com.example.jwt_beginner.DTO.ProductResponse;
import com.example.jwt_beginner.DTO.UpdateProductRequest;
import com.example.jwt_beginner.Service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PageableDefault;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ProductController {

    private final ProductService productService;

    @GetMapping("/products/{id}")
    public ResponseEntity<ProductResponse> getProduct(@PathVariable Long id){
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @PostMapping("/products")
    public ResponseEntity<ProductResponse> createProduct(
            @Valid @RequestBody  CreateProductRequest request
            ){
        ProductResponse response = productService.createProduct(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .header("Location","api/v1/products/"+response.getId())
                .body(response);
    }

    @GetMapping("/products")
    public ResponseEntity<Page<ProductResponse>> getAllProducts(@PageableDefault(size = 20) Pageable pageable){
        return ResponseEntity.ok(productService.getAllProducts(pageable));
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody UpdateProductRequest request
            ){
        return ResponseEntity.ok(productService.updateProduct(id,request));
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id){
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
