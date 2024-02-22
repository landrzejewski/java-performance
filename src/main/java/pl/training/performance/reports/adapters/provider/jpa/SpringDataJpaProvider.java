package pl.training.performance.reports.adapters.provider.jpa;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface SpringDataJpaProvider extends JpaRepository<DataEntryEntity, Long> {

    Page<DataEntryEntity> findByOrderDateAfterAndOrderDateBefore(LocalDate after, LocalDate before, Pageable pageable);
}
