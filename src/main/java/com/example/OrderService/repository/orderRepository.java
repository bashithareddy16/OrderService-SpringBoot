package com.example.OrderService.repository;

import com.example.OrderService.entity.order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface orderRepository extends JpaRepository<order,Long> {

}
