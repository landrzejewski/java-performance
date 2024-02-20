Projekt założenia:

Twoim zadaniem jest stworzenie aplikacji raportującej sprzedaż produktów firmy X.
Dane sprzedażowe udostępnione są w postaci plików csv (próbki o różnej wielkości).
Z uwagi na dużą ilość pracowników firmy X, jednym z kluczowych wymagań jest zapewnienie odpowiedniej wydajności
wspomnianej aplikacji.
W pierwszej iteracji aplikacja powinna umożliwiać wygenerowanie: 
- Rankingu produktów zależnego od uzyskanego profitu (pole Total Profit) w okresie wskazanego roku lub miesiąca 
- Zestawienia całkowitej sprzedaży (pole Units Sold) danego produktu (pole Item Type) per region i kraj (pola Region i Country)

Po wygenerowaniu raportu uzyskane dane powinny zostać zapisane w pliku, aplikacja powinna umożliwiać ich odczyt i wydrukowanie
na konsoli, a także wyszukanie poniższych informacji:
- Dane dotyczące uzyskanego profitu dla wskazanego produktu (pole Item Type) w okresie wskazanego roku lub miesiąca
- Dane dotyczące ilości sprzedanych produktów (pole Units Sold) danego produktu (pole Item Type) w danym regionie lub kraju 

Uwaga na tym etapie dozwolona jest dowolna implementacja rozwiązania, ale istnieją pewne ograniczenia:
- nie wolno korzystać z zewnętrznych bibliotek ani frameworków
- aplikacja jest jednowątkowa
- nie dostarczamy UI, a jedynie api/metody 
- należy umożliwić wymianę implementacji dotyczącej odczytu surowych danych oraz zapisu/odczytu danych wygenerowanych (interfejsy)

Po zaimplementowaniu pierwszej wersji rozwiązania należy zmierzyć wydajność / napisać testy z wykorzystaniem JMH i zapisać je np. na
poziomie Google Excel. 
Po wykonaniu testów wstępnych należy spróbować zoptymalizować istniejącą aplikację, wykorzystując napisane testy oraz profiler.

https://excelbianalytics.com/wp/downloads-18-sample-csv-files-data-sets-for-testing-sales
