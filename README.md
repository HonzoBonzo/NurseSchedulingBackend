# Domain transformation
podejście do rozwiązywania złożonych problemów, które obejmuje:
  -rozsądne uproszczenie oryginalnego problemu
  -rozwiązanie uproszczenia
  -transformacja rozwiązania 
  
# Opinia o algorytmie z publikacji prof. Bargieli
``` 
" The greedy algorithm outperforms in most cases when tested with the different demands and number of nurses.
The results facilitated the development of a cost–benefit analysis across different levels of staffing. "

" Taken as a whole, the proposed approach has a number of distinct advantages. The greedy algorithm is easy
to implement, which ensures a good solution is obtained in real time. "
```

# W naszym przypadku 3 główne etapy to:
 - konwersja problemu z edlnR na problem DNR
 - rozwiązanie dla problemu DNR
 - konwersja rozwiązanie DNR na rozwiązanie edlNR
gdzie e e (early shift), d (day shift), l (late shift), N (night shift)
and R (rest day) while D (połączenie zmian ypu early, day i late)

# Co potrzeba przed przystąpieniem do algorytmu zachłannego?
Do rozwiązania problemu tym algorytmem należy wygenerować jednotygodniowe wzorce spełniające wymagania twarde (hard constraints) i spełniające lub łamiące określoną liczbę wymagań miękkich (soft constraints).

W projektowaniu sekewncji zmian najpierw bierzemy pod uwagę zmiany, które są najważniejsze lub te najtrudniejsze do spełnienia. W naszym przypadku są to zmiany nocne. Optymalnym rozwiązaniem są trzy wzorce tygodniowych zmian nocnych:

|          | M | T | W | T | F | S | S |
|----------|---|---|---|---|---|---|---|
|Pattern A | N | N | - | - | - | - | - |
|Pattern B | - | - | N | N | - | - | - |
|Pattern C | - | - | - | - | N | N | N |

Dlaczego akurat tak? Ze względu na dwa pierwsze (najbardziej kosztowne, bo warte 1000 każde ograniczenia miękkie)
- Od piątku 22:00 do poniedziałku 0:00 pielęgniarka powinna albo nie mieć żadnej zmiany, albo mieć przynajmniej 2
- Dla pielęgniarek pracujących od 30-48 h tygodniowo (czyli wszystkich?) długość zmian nocnych powinna wynosić 2-3. 

W publikacji pisze, że tylko te 3 wzorce spełniają ograniczenia twarde, chociaż nie do końca jest to dla mnie jasne. 
![alt tag](https://github.com/HonzoBonzo/NurseSchedulingBackend/blob/mboryczko/paterns.PNG)

Gdy mamy już wzorce użyjemy ich do wygenerowania harmonogramu dla jednego tygodnia w domenie DNR. Grupujemy wzorce do 3 kategorii wg kosztu: 0, 5 i 10. Wyszczególniamy wszystkie wzorce nocne (które mogą, ale nie muszą mieć zmian dziennych) i zmiany dzienne (które zawierają tylko zmiany dzienne) oraz wszystkie 


# W skrócie:
W tygodniu pierwszym używamy wzorców o koszcie 0
1. do każdej pielęgniarki przydzielamy odpowiedni wzorzec (czyli od razu cały plan pierwszego tygodnia)
  zgodnie z zasadami Algorytmu Zachłannego(staramy się najpierw, jeśli to możliwe, przydzielić najlepiej dopasowany wzorzec nocny -   jeśli jest jeszcze dostępny lub możliwy do użycia - jeśli nie to najlepiej dopasowany wzorzec dzienny)  
2. Po wstępnym przydzieleniu jeśli jest za dużo zmian nocnych w harmonogramie - zastępujemy je dniami wolnymi
3. Dla każdego dnia tygodnia sprawdzamy czy spełnione są warunki 
  -liczby zmian dziennych -> jeśli nie to zamieniamy pewne zmiany na dni wolne 
  -liczby zmian nocnych -> jeśli nie to zamieniamy pewne zmiany na dni wolne 
4. poprawiamy przydzielone wzorce - tak, że jeśli wyszło więcej zmian niż jest zapotrzebowanie to zastępujemy je dniami wolnymi
5. poprawiamy przydzielone wzorce - tak, że jeśli wyszło mniej zmian niż jest zapotrzebowanie to zastępujemy je

6. To samo wykonujemy dla następnych tygodni uwzględniając ograniczenia z poprzednich tygodni i używając wzorców o koszcie
niekoniecznie zerowym
7. konwertujemy problem z domeny DNR na domenę edlNR

