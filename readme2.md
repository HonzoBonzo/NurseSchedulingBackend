# Sformułowania 
W publikacji prof. Bargiela używa następujących sformułowań
1. Sequence of shifts - sekwencja zmian rozumiany jako 7 dniowy harmonogran zmian dla pojedynczej pielęgniarki.
Sekwencje mogą być wykonalne (ang. feasible), co oznacza, że nie łamią żadnych ograniczeń twardych - np EDLLRRR.
Sekwencje są także klasyfikowane ze względu na koszt jako "sekwencje o koszcie zero", np ELLLRRR i "sekwencje o koszcie niezerowym",
czyli takie które łamią ograniczenia miękkie, np. ERNRLL - o koszcie 1000, bo łamie zasadę o długości zmian nocnych, które powinny być długości 2-3.

2. Schedule - to zbiór sekwencji zmian przypisany do każdej pielęgniarki w taki sposób, że po dodaniu ich do siebie otrzymujemy
wymagania szpitala odnośnie liczby zmian.

# Proponowane rozwiązanie
## w ogólnej ideii jest takie samo jak przy użyciu algorytmu zachłannego
 - konwersja problemu z edlnR na problem DNR
 - rozwiązanie dla problemu DNR
 - konwersja rozwiązanie DNR na rozwiązanie edlNR
gdzie e e (early shift), d (day shift), l (late shift), N (night shift)
and R (rest day) while D (połączenie zmian ypu early, day i late)

## Granulacja typów zmian
Zauważamy, że wszystkie zmiany dzienne, tzn. Early, Day i Late są podobne pod względem godzin i ograniczeń. Możemy zatem
uprościć problem przechodząc na wyższy poziom abtrakcji i niejako złączyć wszystkie zmiany dzienne w jeden typ - "merged Day"
oznaczany po prostu D. Rozwiązanie to i fakt, że bierzemy pod uwagę okres 1 tygodnia znacząco zmiejsza liczbę kombinacji. 

## Granulacja sekwencji zmian na wzorce
- odnotowujemy, że ograniczenia miękkie wyrażone są w związku z karami odnoszącymi się do określonych sekwencji
zmian w okresie 1 tygodnia
- jesteśmy zatem w stanie przygotować gotowe sekwencje spełniające wszystkie warunki lub naliczające określoną karę,
które nazwiemy WZORCAMI
- będą mogły być użyte w budowaniu harmonogramu bez późniejszego sprawdzania warunków miękkich

Przez połączenie granulacji typów zmian i sekwencji zmian na wzorce możemy stworzyć gotowe wzorce w domenie DNR, które będą mogły
być potem skonwertowane w łatwy sposób na domenę EDLNR, np. wzorzec DDNNRRR może być zastąpiony jednym z 9 wzorców z domeny EDLNR.

## Tworzenie wzorców
Zaczynamy od wszystkich możliwych sekwencji zmian i stosujemy się do ograniczeń twardych, by wyeliminować te sekwencje, które 
by je naruszały. Po analizie ograniczeń twardych można dojść do wniosku, że możliwe sa tylko 3 rozwiązania:

|          | M | T | W | T | F | S | S |
|----------|---|---|---|---|---|---|---|
|Pattern A | N | N | R | R | - | - | - |
|Pattern B | - | - | N | N | R | R | - |
|Pattern C | - | - | - | - | N | N | N |

Następnie zaczynamy oceniać te wzorce od najbardziej kosztowych (1000) do najmniej (0). Dla pielęgniarek pracujących 36 i 32 h tygodniowo mamy 18 wzorców o zerowym koszcie. Dla pielęgniarek pracujących 20h mamy 15 takich wzorców. 

![alt tag](https://github.com/HonzoBonzo/NurseSchedulingBackend/commit/a774ac92dc209396f463f524524c6e8cd5e7fbf7)
Jeśli weźmiemy także pod uwagę wzorce o koszcie 10 to uzyskamy 30 wzorców dla FT pielęgniarek i 26 dla PT pielęgniarek. Oczywiście zwiększając możliwe koszty wzorca zwiększymy także znacząco liczbę możliwych wzorców, ale ponieważ chcemy uzyskać jak najefektywniejsze rozwiązanie to nie ma potrzeby rozważać większych kosztów.

## Domain transformation - organizacja planowania
By uprościć problem będziemy rekursywnie rozważać każdy tydzień w następujących krokach:
1. zaplanuj 1 tydzień (DNR)
2. rozszerz plan na N tygodni (DNR)
3. konwertuj plan DNR na EDLNR

## Planowanie pierwszego tygodnia (DNR)
Konstruujemy harmonogram 1 tygodnia w domenie DNR poprzez przydzielenie wzorców o koszcie zerowym do odpowiednich pielęgniarek pracujących na pełny lub niepełny etat - nazwane jest to ZBIOREM HARMONOGRAMU (schedule set). Zbiór ten przechowywany jest w tablicy.
Najważniejszymi zmianami są zdecydowanie zmiany nocne. Mają one także wysoko karane ograniczenia miękkie (1000 jeśli nie są w serii 
2-3 pod rząd). Następnie umieszczamy wzorce dzienne w tablicy wzorców dziennych. 

Zmiany nocne:
  Dla M i T możemy wybrać 1 parę z 3 dostępnych (A1 - A2 i B1)
  DLa W i T wybieramy 1 parę z 3 dostępnych (A3 i B2 - B3)
  Dla F, S i S wybieramy 1 parę z 6 możliwych (A4 - A8 i B4)

Następnie zmiany dzienne dla weekendu : A12 - A18. Mamy 7 możliwości 
Następnie zmiany odpoczynku dla weekendu: A9 - A11 później B5 - B12 
Jeśli nie znajdziemy przypisań o zerowym koszcie wyszukujemy z tych o większym koszcie
Jeśli mamy za dużo pielęgniarek na zmianę to stosujemy zamianę


## Rozszerzamy procedurę dla tygodnia N + 1(DNR)

## Konwertujemy DNR na EDLNR

# Podsumowanie
" Granulacja informacji okazała się dobrym rozwiązaniem redukującym możliwe kombinacje. Metoda wydaje się prosta? (ang. straightforward), ale wysoce efektywna. Czas potrzebny na wygenerowanie wyniósł zaledwie 8 sekund. Ponadto metoda nie jest 
zahardkodowana do żadnych ograniczeń. "
