Projekt założenia:

Twoim zadaniem jest stworzenie aplikacji raportującej sprzedaż produktów firmy X.
Dane sprzedażowe udostępnione są w postaci plików csv (próbki o różnej wielkości).
Z uwagi na dużą ilość pracowników firmy X, jednym z kluczowych wymagań jest zapewnienie odpowiedniej wydajności
wspomnianej aplikacji.
W pierwszej iteracji aplikacja powinna umożliwiać wygenerowanie: 
- Rankingu produktów zależnego od uzyskanego profitu (pole Total Profit) w okresie wskazanego roku lub miesiąca 
- Zestawienia całkowitej sprzedaży (pole Units Sold) danego produktu (pole Item Type) per region i kraj (pola Region i Country)

Wykorzystaj więdzę z zakresu wielowątkowości w celu dalszej optymalizacji działania kodu
- zrównoleglij wykonanie obliczeń wykorzystując i wybierz najbardziej opłacalną opcję
  - a) pule wątków
  - b) mechanizm fork join
  - c) zrównoleglanie strumieni
  - d) RxJava
- dodaj możliwość wstawiania nowych danych do pliku, wykorzystaj różne strategie synchronizacji w celu zapewnienia
  odpowiedniej wydajności i bezpieczeństwa, wybierz najbardziej opłacalny wariant (monitor, blokady jawne)

- dodaj raport dotyczący całkowitej sprzedaży (dowolna wariacja jdbc templates/jpa/mongo/reactive mongo)
Logikę generowania raportu (filtrowanie, grupowanie) wykonaj na poziomie bazy. Wystaw funkcjonalność generowania raportu
w postaci REST. Zbadaj wydajność bazową, a następnie stosując optymalizacje na różnych poziomach, 
postaraj się jak najbardziej poprawić rezultaty (zapisz wyniki pośrednie z informacją o wprowadzonych zmianach) 


Uwaga na tym etapie dozwolona jest dowolna implementacja rozwiązania, ale istnieją pewne ograniczenia:
- nie wolno korzystać z zewnętrznych bibliotek ani frameworków
- aplikacja jest jednowątkowa
- nie dostarczamy UI, a jedynie api/metody 
- należy umożliwić wymianę implementacji dotyczącej odczytu surowych danych oraz zapisu/odczytu danych wygenerowanych (interfejsy)

https://excelbianalytics.com/wp/downloads-18-sample-csv-files-data-sets-for-testing-sales
