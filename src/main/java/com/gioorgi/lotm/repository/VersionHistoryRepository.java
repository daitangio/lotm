package com.gioorgi.lotm.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.gioorgi.lotm.entity.LotmVersionHistory;

public interface VersionHistoryRepository extends CrudRepository<LotmVersionHistory, Long>{

    @Query("from LotmVersionHistory order by id")
    List<LotmVersionHistory> findAllOrdered();
}
