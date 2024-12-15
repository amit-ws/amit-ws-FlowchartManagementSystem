package com.conceptile.repository;


import com.conceptile.entity.Flowchart;
import com.conceptile.entity.Node;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NodeRepository extends JpaRepository<Node, Long> {
    List<Node> findAllByFlowchart(Flowchart flowchart);
}
