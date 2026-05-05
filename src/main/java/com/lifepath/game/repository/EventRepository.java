package com.lifepath.game.repository;

import com.lifepath.game.model.Event;
import com.lifepath.game.model.Stage;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByStage(Stage stage);
}
