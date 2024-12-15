package com.conceptile.repository;


import com.conceptile.entity.Flowchart;
import com.conceptile.entity.Node;
import com.conceptile.entity.NodeConnection;
import com.conceptile.projection.NodeConnectionProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.Flow;

@Repository
public interface NodeConnectionRepository extends JpaRepository<NodeConnection, Long> {
    List<NodeConnection> findAllByFlowchart(Flowchart flowchart);
    Optional<NodeConnection> findByFlowchartAndFromNodeAndToNode(Flowchart flowchart, Node fromNode, Node toNode);
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

    @Query(value =
            "SELECT nc.connection_id, fn.name AS from_node_name, tn.name AS to_node_name, nc.flowchart_id " +
                    "FROM node_connection nc " +
                    "LEFT JOIN node fn ON fn.node_id = nc.from_node_id  " +
                    "LEFT JOIN node tn ON tn.node_id = nc.to_node_id " +
                    "WHERE nc.from_node_id = :fromNodeId " +
                    "  AND nc.flowchart_id = :flowchartId"
            , nativeQuery = true)
    List<NodeConnectionProjection> getAllNodeConnectionUsingFlowchartIdAndFromNodeId(Long flowchartId, Long fromNodeId);


    @Query(value =
            "WITH RECURSIVE connected_edges(connection_id, from_node_name, to_node_name, flowchart_id, to_node_id) AS ( " +
                    "    SELECT nc.connection_id, fn.name AS from_node_name, tn.name AS to_node_name, nc.flowchart_id, nc.to_node_id " +
                    "    FROM node_connection nc " +
                    "    LEFT JOIN node fn ON fn.node_id = nc.from_node_id " +
                    "    LEFT JOIN node tn ON tn.node_id = nc.to_node_id " +
                    "    WHERE nc.from_node_id = :fromNodeId AND nc.flowchart_id = :flowchartId " +
                    "    UNION ALL " +
                    "    SELECT nc.connection_id, fn.name AS from_node_name, tn.name AS to_node_name, nc.flowchart_id,  nc.to_node_id " +
                    "    FROM node_connection nc " +
                    "    LEFT JOIN node fn ON fn.node_id = nc.from_node_id " +
                    "    LEFT JOIN node tn ON tn.node_id = nc.to_node_id  " +
                    "    INNER JOIN connected_edges ce ON nc.from_node_id = ce.to_node_id " +
                    "    WHERE nc.flowchart_id = :flowchartId " +
                    ") " +
                    "SELECT connection_id, from_node_name, to_node_name, flowchart_id " +
                    "FROM connected_edges"
            , nativeQuery = true)
    List<NodeConnectionProjection> getAllDirectAndIndirectNodeConnectionsForNode(Long flowchartId, Long fromNodeId);
}
