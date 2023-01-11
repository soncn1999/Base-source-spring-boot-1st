package com.caoson.kma.controllers;

import com.caoson.kma.models.Product;
import com.caoson.kma.models.ResponseObject;
import com.caoson.kma.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "api/v1/Products")
public class productController {

    @Autowired
    private ProductRepository repository;

    //Get data function: Get all, Get data by id.
    @GetMapping("")
        List<Product> getAllProduct() {
            return repository.findAll();
        }

    @GetMapping("/{id}")
//    Optional<Product> findById(@PathVariable Long id) { //Co the null
//        return repository.findById(id);
//    }
//    Product findById(@PathVariable Long id) { //Co the null
//        return repository.findById(id).orElseThrow(() -> new RuntimeException("Cannot find product with id = " + id));
//    }
    ResponseEntity<ResponseObject> findById(@PathVariable Long id) {
        Optional<Product> foundProduct = repository.findById(id);
        if(foundProduct.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK","Query product successfully",foundProduct)
            );
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("failed","Cannot find product with id: " + id, "")
            );
        }
    }

    //Post data.
    //param: Raw, JSON
    @RequestMapping(method = RequestMethod.POST)
    ResponseEntity<ResponseObject> insertProduct(@RequestBody Product newProduct) {
        List<Product> foundProducts = repository.findByProductName(newProduct.getProductName().trim());
        if(foundProducts.size() > 0) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("failed","Product name already taken","")
            );
        }
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok","Insert data successfully",repository.save(newProduct))
        );
    }

    //Update data
    @PutMapping("/{id}")
    ResponseEntity<ResponseObject> updateProduct(@RequestBody Product newProduct, @PathVariable Long id) {
        Product updatedProduct = repository.findById(id).map(product -> {
            product.setProductName(newProduct.getProductName());
            product.setYear(newProduct.getYear());
            product.setPrice(newProduct.getPrice());
            return repository.save(product);
        }).orElseGet(() -> {
            newProduct.setId(id);
            return repository.save(newProduct);
        });
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok","Insert data successfully",updatedProduct)
        );
    }

    //Delete data
    @DeleteMapping("/{id}")
    ResponseEntity<ResponseObject> deleteProduct(@PathVariable Long id) {
        boolean exists = repository.existsById(id);
        if(exists) {
            repository.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK","Delete product successfully","")
            );
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("failed","Cannot delete product!","")
        );
    }
}
