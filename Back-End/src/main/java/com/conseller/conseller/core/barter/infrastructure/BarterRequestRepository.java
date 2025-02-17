package com.conseller.conseller.core.barter.infrastructure;

import com.conseller.conseller.core.barter.infrastructure.entity.BarterRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BarterRequestRepository extends JpaRepository<BarterRequestEntity, Long> {
    Optional<BarterRequestEntity> findByBarterRequestIdx(Long barterRequestIdx);

    @Query("SELECT br FROM BarterRequest br WHERE br.barter.barterIdx = ?1")
    List<BarterRequestEntity> findByBarterIdx(Long barterIdx);

    @Query("SELECT br FROM BarterRequest br WHERE br.user.userIdx = ?1")
    List<BarterRequestEntity> findByUserIdx(Long userIdx);

    public void deleteAllByBarterIdx(Long barterId);
}
