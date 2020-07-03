package com.example.mileage;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

public interface MileageRepository extends CrudRepository<Mileage, Long> {

	Optional<Mileage> findByPurchaseId(Long purchaseId);
}
