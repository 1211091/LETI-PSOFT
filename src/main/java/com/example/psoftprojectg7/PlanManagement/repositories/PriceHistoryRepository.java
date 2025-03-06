package com.example.psoftprojectg7.PlanManagement.repositories;

import com.example.psoftprojectg7.PlanManagement.model.Plan;
import com.example.psoftprojectg7.PlanManagement.model.PriceHistory;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface PriceHistoryRepository extends CrudRepository<PriceHistory, Long>{
        @Query("SELECT ph FROM PriceHistory ph WHERE ph.planName = :planName ORDER BY ph.lastPriceChangeDate DESC")
        List<PriceHistory> findByPlanNameOrderByLastPriceChangeDateDesc(@Param("planName") String planName);
}
