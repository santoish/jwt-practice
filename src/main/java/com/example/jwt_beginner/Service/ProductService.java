package com.example.jwt_beginner.Service;

import com.example.jwt_beginner.DTO.CreateProductRequest;
import com.example.jwt_beginner.DTO.ProductResponse;
import com.example.jwt_beginner.DTO.UpdateProductRequest;
import com.example.jwt_beginner.Entity.Product;
import com.example.jwt_beginner.Exceptions.DuplicateProductNameException;

import com.example.jwt_beginner.Exceptions.ProductNotFoundException;
import com.example.jwt_beginner.Repository.ProductRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.domain.Pageable;
import java.util.List;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ProductService {


    private final ProductRepository productRepo;

    @Transactional
    public ProductResponse createProduct(CreateProductRequest request){
        Optional<Product> existProduct = productRepo.findByName(request.getName());
        if(existProduct.isPresent()){
            throw new DuplicateProductNameException("Product with Name "+request.getName()+" Already Exist");
        }
        Product newProduct = convertCreateDTOToEntity(request);
        Product savedProduct = productRepo.save(newProduct);

        ProductResponse response = convertEntityToDTO(savedProduct);

        return response;

    }

    @Transactional(readOnly = true)
    public ProductResponse getProductById(Long productId){
        Product product = productRepo.findById(productId).orElseThrow(()->new ProductNotFoundException("Product with Id : "+productId+" not Found"));

        ProductResponse response = convertEntityToDTO(product);
        return response;
    }

    @Transactional(readOnly = true)
    public Page<ProductResponse> getAllProducts(Pageable pageable){
        Page<Product> products = productRepo.findAll(pageable);

        List<ProductResponse> responses = products.getContent()
                .stream()
                .map(this::convertEntityToDTO)
                .toList();

        Page<ProductResponse> responsePage = new PageImpl<>(
                responses,
                pageable,
                products.getTotalElements()
        );

        return responsePage;
    }

    @Transactional
    public ProductResponse updateProduct(Long id,UpdateProductRequest request){
        Product product = productRepo.findById(id).orElseThrow(()->new ProductNotFoundException("Product with Id : "+id+"is not Found"));

        if(!product.getName().equals(request.getName())){
            Optional<Product> existingWithNewName = productRepo.findByName(request.getName());

            if(existingWithNewName.isPresent()){
                throw new DuplicateProductNameException("Product with name : "+request.getName()+" Already exist");
            }
        }

        convertUpdateDTOToEntity(request,product);

        Product updatedProduct = productRepo.save(product);

        return convertEntityToDTO(updatedProduct);

    }

    @Transactional
    public void deleteProduct(Long id){
        Product product = productRepo.findById(id).orElseThrow(()->new ProductNotFoundException("Product with Id : "+id+" is not Found"));
        productRepo.delete(product);
    }



    private Product convertCreateDTOToEntity(CreateProductRequest dto){
        Product product = Product.builder()
                .name(dto.getName())
                .price(dto.getPrice())
                .quantity(dto.getQuantity())
                .build();

        return product;
    }

    private ProductResponse convertEntityToDTO(Product product){
        ProductResponse response = ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .quantity(product.getQuantity())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .build();

        return response;
    }

    private void convertUpdateDTOToEntity(UpdateProductRequest request, Product product){
        product.setName(request.getName());
        product.setPrice(request.getPrice());
        product.setQuantity(request.getQuantity());
    }
}


