package com.myproject.BillGenerateSys.Repository;

import com.myproject.BillGenerateSys.Model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepo extends JpaRepository<Customer,Integer> {
}
