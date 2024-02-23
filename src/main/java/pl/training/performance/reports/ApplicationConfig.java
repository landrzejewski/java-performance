package pl.training.performance.reports;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import pl.training.performance.reports.domain.ReportGenerator;
import pl.training.performance.reports.ports.DataProvider;

@EnableReactiveMongoRepositories
@Configuration
public class ApplicationConfig {

    @Bean
    public ReportGenerator reportGenerator(DataProvider dataProvider) {
        return new ReportGenerator(dataProvider);
    }

}
/*
 - G1 GC is currently the better algorithm to choose for a majority of applications
 - The serial collector makes sense when running CPU-bound applications on a machine with a single CPU,
   even if that single CPU is hyper-threaded. G1 GC will still be better on such hardware for jobs that are not CPU-bound
-  The throughput collector makes sense on multi-CPU machines running jobs that are CPU bound
   Even for jobs that are not CPU bound, the throughput collector can be the better choice
   if it does relatively few full GCs or if the old generation is generally full
 */

// java -XX:+UseZGC -XX:+ZGenerational
// ZGC
// jdbc: Total time: 116 498ms

// G1                                          -Xms8G -Xmx8G              -Xms4G -Xmx4G
// jdbc: Total time: 116 112ms                 Total time: 125 124ms      Total time: 125 882ms
// jpa: Total time: 129 237ms                  Total time: 161 625ms      Total time: 145 781ms
// mongo: Total time: 114 410ms

// Serial -XX:+UseSerialGC
// jdbc: Total time: 100 860ms                 Total time: 90 653ms       Total time: 90 372ms
// jpa:
// mongo:

// Parallel(Throughput) -XX:+UseParallelGC
// jdbc:  Total time: 124 075ms                Total time: 110 127ms      exception
// jpa:
// mongo:

// OS                            Initial heap size (Xms)                       Maximum heap size (Xmx)
// Linux                         Min (512 MB, 1/64 of physical memory)         Min (32 GB, 1/4 of physical memory)
// macOS                         64 MB                                         Min (1 GB, 1/4 of physical memory)
// Windows 32-bit                16 MB                                         256 MB
// Windows 64-bit                64 MB                                         Min (1 GB, 1/4 of physical memory)

// Sizing the Generations
// -XX:NewRatio=N - Set the ratio of the young generation to the old generation
// -XX:NewSize=N - Set the initial size of the young generation
// -XX:MaxNewSize=N - Set the maximum size of the young generation
// -XmnN - Shorthand for setting both NewSize and MaxNewSize to the same value

// Sizing Metaspace (default min. about 20MB, max. unlimited)
// -XX:MetaspaceSize=N
// -XX:MaxMetaspaceSize=N

// Zmiana ilości wątków GC
// -XX:ParallelGCThreads=N

// https://belief-driven-design.com/looking-at-java-21-generational-zgc-e5c1c
