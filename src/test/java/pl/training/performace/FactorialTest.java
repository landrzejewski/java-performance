package pl.training.performace;

import org.junit.jupiter.api.Test;
import pl.training.performance.Factorial;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FactorialTest {

    private static final long EXPECTED_N_FACTORIAL_RESULT = 2 * 3 * 4 * 5;
    private static final int N = 900_000_000;

    private final Factorial factorial = new Factorial();

    @Test
    void given_n_when_execute_loop_variant_then_returns_valid_factorial_result() {
        assertEquals(EXPECTED_N_FACTORIAL_RESULT, factorial.factorialWithLoop(N));
    }

    @Test
    void given_n_when_execute_stream_variant_then_returns_valid_factorial_result() {
        assertEquals(EXPECTED_N_FACTORIAL_RESULT, factorial.factorialWithStreams(N));
    }

    @Test
    void given_n_when_execute_recursion_variant_then_returns_valid_factorial_result() {
        assertEquals(EXPECTED_N_FACTORIAL_RESULT, factorial.factorialWithRecursion(N));
    }

    /*
       Microbenchmarks - testy umożliwiające pomiar wydajności małej jednostki kodu. Ułatwiają podejmowanie decyzji dotyczących wyboru
       algorytmu, implementacji czy podejścia do rozwiązywania problemu np.tworzenie wątków vs. użycie puli wątków,
       algorytm a vs algorytm b.

       Microbenchmarks mogą wydawać się dobrym pomysłem, ale kompilacja just-in-time i garbage collection
       utrudniają ich poprawne tworzenie.
    */

    /*
       Kod Javy jest interpretowany podczas pierwszych uruchomień i staje się szybszy/optymalny im dłużej jest
       wykonywany (kompilacja). Z tego powodu wszystkie testy porównawcze (nie tylko Microbenchmarks) powinny obejmować
       okres rozgrzewki, podczas którego maszyna JVM może zoptymalizować wykonywany kod.

       Microbenchmarks muszą wykorzystywać swoje wyniki.
       Ze względu na to, że wartość zmiennej result nie jest nigdzie odczytywana, kompilator może całkowicie pominąć
       jej obliczanie. Rozwiązaniem może być użycie zmiennej instancyjnej z modyfikatorem voliatile.
    */

    volatile long factorial_result;

    @Test
    void factorial_warmup_and_variables() {
        // warmup
        for (int index = 0; index < 1_000_000; index++) {
            factorial_result = factorial.factorialWithLoop(50);
        }
        measure("Factorial", () -> {
            for (int index = 0; index < 200; index++) {
                factorial_result = factorial.factorialWithLoop(10_000_000);
            }
        });
    }

    /*
        - elementy kodu, które są niepotrzebne lub nieużywane mogą zostać usunięte przez kompilator (volatile)
        - bez rozgrzewki szybkość wykonywania kodu może być znacząco mniejsza (interpretacja vs. kompilacja)
        - tworzenie testów tego typu jest bardzo trudne - zależność od dużej ilości zmienny mogących fałszować ostateczne rezultaty
    */

     /*
         Microbenchmarks muszą testować sensowny zakres danych wejściowych.
         W przykładzie obliczamy tylko jedną wartość (silnia 20). Kompilator może to wykryć i wykonać pętlę tylko
         raz lub przynajmniej odrzucić niektóre iteracje. Sama wartość wejściowa ma także duże znaczenie -
         wyznaczanie silni dla liczby 20 różni się wydajnościowo od wyznaczania silni dla liczby 100 czy 200.
         Jakimś pomysłem jest losowanie wartości wejściowych, ale w poniższym przykładzie mierzony czas będzie
         powiększony/zaburzony, czasem potrzebnym do wygenerowania liczb pseudolosowych. Dlatego dobrym pomysłem
         jest wyznaczyć wartości wejściowe wcześniej. Dodatkową zaletą takiego podejścia jest potencjalna powtarzalność testu.
         Kolejnym aspektem jest kwestia poprawności danych wejściowych oraz ich walidacji, a także to czy są one realne
         (odpowiadają temu, co będzie się działo w docelowej aplikacji).
    */

    @Test
    void factorial_and_input() {
        var random = new Random();
        /*for (int index = 0; index < 1_000_000; index++) {
            factorial_result = factorial.factorialWithLoop(50);
        }
        measure("Factorial with warmup", () -> {
            for (int index = 0; index < 200; index++) {
                factorial_result = factorial.factorialWithLoop(random.nextInt(9_500_000, 10_000_000));
            }
        });*/

        var iterations = 10_000_000 - 9_500_000;
        int[] input = new int[iterations];
        for (int index = 0; index < iterations; index++) {
            input[index] = random.nextInt(9_500_000, 10_000_000);
        }
        measure("Factorial", () -> {
            for (int index = 0; index < 200; index++) {
                factorial_result = factorial.factorialWithLoop(input[index]);
            }
        });

    }

     /*
        Microbenchmarks mogą zachowywać się inaczej w środowisku produkcyjnym.
        Bazując m.in. na częstości wykonywania metod, złożoności stosu wykonania i wielu innych czynnikach
        kompilator JIT stara się "dobrać" najlepsze możliwe optymalizacje. W związku z tym testowanie fragmentu kodu
        oraz całej aplikacji przy użyciu tych samych Microbenchmarks może dawać różne rezultaty.
        Microbenchmarks mogą również wykazywać bardzo różne zachowanie w zakresie odśmiecania pamięci.
        Wpływ na nie może też mieć pozostała część systemu/inne procesy.
     */

    /*
           - powinniśmy zwracać uwagę, aby dane testowe były zróżnicowane (cachowanie wyników), ale także realne/produkcyjne
           - przygotowanie danych oraz i ich walidacja nie powinny być częścią właściwego testu, ponieważ mogą na niego znacząco wpływać
           - dobrze, aby kolejne iteracje testów były powtarzalne (te same dane)
    */

    /*
        Macrobenchmarks
        Polegają na testowaniu wydajności całej aplikacji, z wykorzystaniem jej zależności jak baza danych.
        Takie testy uwzględniają realną infrastrukturę, zużycie zasobów, obciążenie itd.
    */

    /*
        Mesobenchmarks to testy pośrednie między Microbenchmarks a Macrobenchmarks.
        Badają wydajność na poziomie modułu lub wybranego przypadku użycia.
        Stanowią rozsądną alternatywę dla testowania pełnowymiarowej aplikacji; ich charakterystyka
        wydajności jest znacznie bardziej zbliżona do rzeczywistej aplikacji niż charakterystyka wydajności Microbenchmarks.
    */

    /*
        Miary:
        Wydajność może być mierzona jako:
        - Elapsed Time (Batch) - czas potrzebny na wykonanie zadania
        - Throughput - ilości pracy, którą można wykonać w określonym czasie np. liczba operacji, żądań, transakcji na sekundę
        - Response-Time - czas, który upływa między wysłaniem żądania a otrzymaniem odpowiedzi
    */

    /*
        Zmienność warunków w czasie
        Programy przetwarzające ten sam zestaw danych mogą dawać inne wyniki pomiarów bo np. procesy działające w tle
        będą miały wpływ na testowaną aplikację, sieć będzie mniej lub bardziej obciążona, ilość dostępnej pamięci operacyjnej
        będzie się zmieniać w czasie itd.
        Dobre benchmarki nie powinny opierać pomiarów o ten sam zestaw danych wejściowych.
        Jednak takie podejście stwarza problem: czy porównując wynik z jednego uruchomienia z
        wynikiem z innego uruchomienia, różnica wynika z regresji, czy z losowej zmienności testu?
        Problem ten można rozwiązać poprzez wielokrotne przeprowadzenie testu i uśrednienie wyników.
        Gdy porównuje się średnie wyniki benchmarków, nie można z całkowitą pewnością stwierdzić, czy różnica w średnich
        jest rzeczywista, czy wynika z przypadkowych wahań, ale można to zrobić z pewnym prawdopodobieństwem.
        Testowanie kodu pod kątem takich zmian nazywa się testowaniem regresyjnym.
        W teście regresji oryginalny kod jest znany jako linia bazowa, a nowy kod jest nazywany próbką.

        Prawidłowe określenie tego, czy wyniki dwóch testów różnią się od siebie, wymaga analizy statystycznej,
        aby upewnić się, że dostrzeżone różnice nie są wynikiem losowego przypadku.

        W idealnym świecie testy wydajności powinny być uruchamiane jako część procesu wytwarzania oprogramowania.
        Dodatkowo testy wydajności powinny być zautomatyzowane i wykonywane w realnym środowisku
    */

    /*
        JHM: https://github.com/openjdk/jmh
        Wygenerowanie projektu: mvn archetype:generate -DinteractiveMode=false -DarchetypeGroupId=org.openjdk.jmh -DarchetypeArtifactId=jmh-java-benchmark-archetype -DgroupId=org.sample -DartifactId=test -Dversion=1.0
     */

    private void measure(String taskName, Runnable task) {
        long startTime = System.currentTimeMillis();
        task.run();
        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        System.out.printf("%s: %dms%n", taskName, totalTime);
    }

     /*
        Profilery to najważniejsze narzędzie w zestawie analizy wydajności aplikacji. Pozwalają określić wąskie gardła
        i zastosować najbardziej opłacalne optymalizacje. Najczęściej są w stanie zobrazować takie aspekty
        jak zużycie pamięci w poszczególnych obszarach programu, czas i częstotliwość wykonywania danego fragmentu kodu,
        czy ilość i charakterystykę pracy wątków.
        Wiele popularnych narzędzi do profilowania jest napisanych w języku Java i działa poprzez „dołączanie”
        się do aplikacji poprzez socket lub natywny interfejs JVMTI (wymiana danych/statystyk - może mieć wpływ na pomiar).

        Profilowanie odbywa się w jednym z dwóch trybów: w trybie próbkowania lub w trybie instrumented.
        Tryb próbkowania jest podstawowym trybem profilowania i wiąże się z najmniejszym obciążeniem.
        To ważne, ponieważ jedną z pułapek profilowania jest to, że prowadząc pomiary do aplikacji,
        zmieniasz jej charakterystykę działania.
        Niestety tryb próbkowania może być obarczony różnego rodzaju błędami. W takim trybie profiler
        sprawdza okresowo każdy wątek i określa, która metoda jest przez niego wykonywana. Częstotliwość próbkowania
        i występujące zależności czasowe mogą powodować przekłamania np. możemy nie zauważyć/zliczyć wykonania metody.
        Innym ograniczeniem jest możliwość pobrania informacji o stack trace wątku tylko w określonych momentach. Na szczęście
        od Java 8 ograniczenie to zostało wyeliminowane (odczyt asynchroniczny).

        Instrumented profilers działają przez modyfikację kodu bajtowego w czasie jego ładowania np. wstawianie kodu w celu
        zliczania wywołań metod. Mogą istotnie wpływać na pomiar wydajności, powodując np. że kompilator nie będzie mógł
        wprowadzić określonych optymalizacji, a tym samym zaburzać pomiar właściwy.
        Ze względu na zmiany wprowadzane do kodu poprzez instrumentację, najlepiej ograniczyć użycie tego trybu do wybranych klas.
        Profiler próbkujący może wskazać pakiet lub sekcję kodu, a następnie w razie potrzeby można użyć profilera instrumentalnego
        do dalszej analizy kodu.

        Większość profilerów nie pokazuje/uwzględnia wywołania metod, kiedy wątek jest zablokowany.
        Zablokowane wątki mogą, ale nie muszą, być źródłem problemów z wydajnością; należy sprawdzić, dlaczego są zablokowane.
        Zablokowane wątki można zidentyfikować na podstawie metody, w której następuje blokowanie lub analizy stanu wątku na osi czasu.

        // Java monitoring tools
        // jcmd https://docs.oracle.com/javase/8/docs/technotes/guides/troubleshoot/tooldescr006.html
        // jconsole https://docs.oracle.com/javase/8/docs/technotes/guides/management/jconsole.html
        // jmap https://docs.oracle.com/javase/8/docs/technotes/guides/troubleshoot/tooldescr014.html
        // jinfo https://docs.oracle.com/javase/8/docs/technotes/tools/unix/jinfo.html
        // jstack https://docs.oracle.com/javase/8/docs/technotes/tools/unix/jstack.html
        // jstat https://docs.oracle.com/javase/8/docs/technotes/tools/windows/jstat.html
        // jvisualvm https://visualvm.github.io

        // Hot Spot configuration - specyficzna i zależna od aplikacji, raczej powinna być realizowana po wyczerpaniu innych
        // wariantów optymalizacji, albo w sytuacji, kiedy mamy jasną informację o tym, że aktualna konfiguracja jest nieoptymalna (warnings)

        // Cache dla kompilatora/dla skompilowanych instrukcji
        // -XX:ReservedCodeCacheSize=N (default 2,5 KB - 240 MB)
        // -XX:InitialCodeCacheSize=N

        // Logowanie informacji na temat procesu kompilacji
        // -XX:+PrintCompilation

        // Flagi bardziej do analizy niż tunningu

        // Poziom (ilość wywołań czy iteracji=) po którym następuje kompilacja danego fragmentu kodu (default 10k dla metod, 8k iteracji)
        // -XX:CompileThreshold=N
        // Ilość wątków odpowiedzialnych za kompilację kodu
        // -XX:CICompilerCount=N
        // Włączenie/wyłączenie method inlining
        // -XX:-Inline=boolean
        // -XX:MaxInlineSize=N
        // Wsparcie nowych instrukcji procesorów Intel
        // -XX:UseAVX=N (level 0 - 3)

        // String
        // Od Java 11 wprowadzono Compact Strings (kodowanie domyślne jako 8-bit jak trzeba jako 16-bit)
        // -XX:+CompactStrings=boolean (default true)

        // Duplikacja Stringów
        // Opcja 1 dodatkowy wątek usuwający duplikaty w trakcie odśmiecania: -XX:+UseStringDeduplication=boolean (default false)
        // Opcja 2 użycie metody intern() typy String

        // Konkatenacja Stringów
        // Domyślnie "a" + "b" + "x" kompilator zamieni na wykorzystanie StringBuilder: -XX:+OptimizeStringConcat=boolean (default true)



    */

}
