package pl.training.performance.reports.adapters.provider.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataJpaProvider extends JpaRepository<DataEntryEntity, Long> {
}
