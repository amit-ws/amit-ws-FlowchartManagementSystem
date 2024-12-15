package com.conceptile.repository;


import com.conceptile.entity.Flowchart;
import com.conceptile.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FlowchartRepository extends JpaRepository<Flowchart, Long> {
    Optional<Flowchart> findByFlowChartId(Long id);
    void deleteByFlowChartId(Long id);
    List<Flowchart> findAllByUser(User user);

    Optional<Flowchart> findByFlowChartIdAndUser(Long id, User user);

}
