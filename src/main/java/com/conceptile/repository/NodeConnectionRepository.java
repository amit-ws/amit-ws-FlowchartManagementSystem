package com.conceptile.repository;


import com.conceptile.entity.Flowchart;
import com.conceptile.entity.NodeConnection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NodeConnectionRepository extends JpaRepository<NodeConnection, Long> {
    void deleteAllByFlowchartAndConnectionIdIn(Flowchart flowchart, List<Long> connectionIds);

    @Query(value =
            "SELECT COUNT(DISTINCT node_id) FROM (" +
                    "    SELECT from_node_id AS node_id " +
                    "    FROM node_connection " +
                    "    WHERE flowchart_id = :flowchartId " +
                    "    UNION " +
                    "    SELECT to_node_id AS node_id " +
                    "    FROM node_connection " +
                    "    WHERE flowchart_id = :flowchartId " +
                    ") AS unique_nodes",
            nativeQuery = true)
    Long countUniqueNodesForFlowchartWhichAreConnected(Long flowchartId);

    @Modifying
    @Query(value =
            "DELETE FROM node_connection " +
                    "WHERE (from_node_id = :nodeId OR to_node_id = :nodeId) " +
                    "  AND flowchart_id = :flowchartId"
            , nativeQuery = true)
    void deleteAllConnectionsFROMandTOnodes(Long flowchartId, Long nodeId);
}
