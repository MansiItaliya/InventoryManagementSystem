package com.myproject.BillGenerateSys.Repository;

import com.myproject.BillGenerateSys.Model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepo extends JpaRepository <Product,Integer>{
    Optional<Product> findByProductNameIgnoreCase(String productName);
}
