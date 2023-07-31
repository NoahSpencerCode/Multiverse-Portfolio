package com.NoahSpencerCode.KingScriptDocumentation.repository;

import com.NoahSpencerCode.KingScriptDocumentation.model.Addition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdditionRepository extends JpaRepository<Addition,Integer> {
}
