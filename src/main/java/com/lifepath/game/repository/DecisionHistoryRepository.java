package com.lifepath.game.repository;

import com.lifepath.game.model.DecisionHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DecisionHistoryRepository extends JpaRepository<DecisionHistory, Long> {
    List<DecisionHistory> findByGameIdOrderByDecidedAtAsc(Long gameId);
    long countByGameId(Long gameId);
}
