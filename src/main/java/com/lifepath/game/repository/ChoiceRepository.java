package com.lifepath.game.repository;

import com.lifepath.game.model.Choice;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ChoiceRepository extends JpaRepository<Choice, Long> {
    List<Choice> findByEventId(Long eventId);
}
