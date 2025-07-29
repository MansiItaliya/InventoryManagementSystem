package com.myproject.BillGenerateSys.Repository;

import com.myproject.BillGenerateSys.Model.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BillRepo extends JpaRepository<Bill,Integer> {
    List<Bill> findByCustomerCustomerIdAndPaymentConfirmed(int customerId, boolean paymentConfirmed);
}
