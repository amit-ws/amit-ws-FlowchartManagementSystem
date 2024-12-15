package com.conceptile.repository;


import com.conceptile.entity.NodeConnection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NodeConnectionRepository extends JpaRepository<NodeConnection, Long> {
}
