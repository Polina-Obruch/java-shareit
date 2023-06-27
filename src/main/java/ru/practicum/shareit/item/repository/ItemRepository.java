package ru.practicum.shareit.item.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

import java.util.List;


public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findAllByOwnerId(Long userId, Pageable pageable);

    List<Item> findAllByOwnerId(Long userId);

    List<Item> findAllByRequestId(Long requestId);

    @Query(value = "SELECT * FROM ITEMS i " +
            "WHERE LOWER(i.NAME) LIKE ?1 OR LOWER(i.DESCRIPTION) LIKE ?1 " +
            "AND i.AVAILABLE IS TRUE", nativeQuery = true)
    List<Item> findByText(String text, Pageable pageable);
}
