package com.conceptile.entity;

import com.conceptile.constant.NodeConnectionType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "node_connection")
public class NodeConnection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "connection_id", nullable = false)
    Long connectionId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    NodeConnectionType type;

    String condition;

    @Column(name = "created_at", updatable = false)
    LocalDateTime createdAt;

    @Column(name = "updated_at")
    LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "from_node_id", referencedColumnName = "node_id", nullable = false)
    Node fromNode;  /* The node where the connection starts */

    @ManyToOne
    @JoinColumn(name = "to_node_id", referencedColumnName = "node_id", nullable = false)
    Node toNode;  /* The node where the connection ends */

    @ManyToOne
    @JoinColumn(name = "flowchart_id", referencedColumnName = "flow_chart_id", nullable = false)
    Flowchart flowchart;  /* The flowchart this connection belongs to */

}
