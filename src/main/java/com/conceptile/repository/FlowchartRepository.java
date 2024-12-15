package com.conceptile.repository;


import com.conceptile.entity.Flowchart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FlowchartRepository extends JpaRepository<Flowchart, Long> {
    Optional<Flowchart> findByFlowChartId(Long id);
    void deleteByFlowChartId(Long id);

}
