# Krotki	opis	problemu:	
Problem	dotyczy	 optymalizacji	harmonogramu	pracy	personelu	medycznego	w	szpitalu.	
Konkretny	przyklad	jest	wziety	z	literatury	technicznej	i dotyczy	szczegolnych	wymogow	
w	 szpitalach	 Holenderskich.	 Nalezy	 zoptymalizowac	 harmonogram	 pracy	 dla	 16	
pielegniarek,	zatrudnionych	na	roznych	kontraktach	godzinowych.	Harmonogram	ma	byc	
opracowany	 na	 35	 dni	 (5	 tygodni)	 i	 ma	 zapewnic	 obsade	 personelu	 na	 wszystkich	
zmianach	 zgodnie	 z	 wymaganiami	 klinicznymi	 oraz	 zgodnie	 z	 wymogami	 ustaw	 o	
godzinach	 pracy.	 Te	 podstawowe	 wymagania	 musza	 zawsze	 byc	 spelnione.	 Dodatkowe	
wymagania	powinny	bys	spelnione	w	jak	najwyzszyn	stopniu	ale	nie	sa	one	konieczne	do	
tego	 aby	 harmonogram	 mogl	 byc	 zaakceptowany.	 Jakosc	 harmonogramu	 jest	 okreslona	
przez	stopien	spelnienia	tych	dodatkowych	warunkow.






# Analiza algorytmu genetycznego
```
Dane: tablica 16 x [(28 + 35 = 63)x4]
czyli: p x z, gdzie p=16, z=252
Dla 9 tygodni mamy tablice z 4032 polami.

p - pielegniarka,
z - zmiana
x - true
o - false
? - to ma ustalic algorytm: czy true? czy false?

Pola na osi OZ 0 - 111 są narzucone z poprzedniego grafiku
Pola na osi OZ 112 - 251 musi wyliczyć algorytm

o - tablica
o = p x z
```

![alt tag](https://github.com/HonzoBonzo/NurseSchedulingBackend/blob/kbysiek/table.jpg)

```
switch(z % 4){
	case 0: "zmiana Day(D): 8:00 - 17:00"
	case 1: "zmiana Early(E): 7:00 - 17:00"
	case 2: "zmiana Late(L): 14:00 - 23:00"
	case 3: "zmiana Night(N): 23:00 - 7:00"
}
```
## Osobnik?
To będzie jedna taka tabela:
```
typedef bool osobnik[NURSE_NUMBER][SHIFTS_NUMBER];
```
## Populacja?
```
osobnik populacja[POPULATION_SIZE];
```
## Chromosom?
jedna cała tabela z wartściami: false(0), true(1)


## PSEUDOKOD
```
doneTablePart = import from "stary schedule"; // pobieramy skads 4 ostatnie tygodnie

#define POPULATION_SIZE 100; //liczba takich tabel jak powyżej
#define NURSE_NUMBER 16; 
#define SHIFTS_NUMBER 252;
#define EPOCH_NUMBER 100; //przykladowa ilosc generacji
#define WEEKS_NUMBER 9;
#define WEEKS_TO_SCHEDULE 5; //liczba tygodni do ustalenia
#define DAY_SHIFT 0;
#define EARLY_SHIFT 1;
#define LATE_SHIFT 2;
#define NIGHT_SHIFT 3;

int hard_constraint_failed_number = 0; //waga za kazde +1
int soft_constraint_failed_number = 0; //wagi jak w zadaniu

//funkcje
void setDonePartOfTable(osobnik *os, doneTablePart) {};
bool losuj() { return bool.rand(); }
int checkHardConstraints(osobnik os) { return liczba; }
int checkSoftConstraints(osobnik os) { return liczba; }
void sortPopulation(populacja *pop, scores[][]) {}
osobnik crossUnits() { return crossedUnit; }
population mutatePopulation(populacja *pop, ratio) {}
```
## ETAPY ALGORYTMU
### 1. Losowanie populacji
```
foreach(osobnik in populacja) {
	setDonePartOfTable(&osobnik, doneTablePart);
	for (int i=0; i<NURSE_NUMBER; i++) {
		for (int j=0; j<SHIFTS_NUMBER; j++)
			osobnik[i][j] = losuj();
	}
}
```
### 2. Ocena osobników
```
if(isDefined(crossedPopulation)) populacja = crossedPopulation;

int scores[populacja.size][2] = {};
int n=0; //indeks w tabeli popoulacja

foreach(osobnik in populacja) {
	scores[n][0] = checkHardConstraints(osobnik);
	scores[n][1] = checkSoftConstraints(osobnik);
	++n;
}

sortPopulation(&populacja, scores);
```
### 3. Selekcja
```
osobnik newPopulation[POPULATION_SIZE];
for (int i; i<population.size/2; i++) {
	newPopulation.push(population[i]); //nowa populacja bedzie zawierac tylko najlepsze osobniki
}
```
### 4. Krzyżowanie
```
osobnik crossedPopulation[POPULATION_SIZE*2]; //nowa tablica ze skrosowanymi i starymi


for (int i=0; i<POPULATION_SIZE; i++) {
	//wez polowe z pierwszego i polowe ostatniego i zlacz;
	//albo wez parzyste zmiany z pierwszego i zlacz z nieparzystymi ostatniego osobnika
	// tu są różne kombinacje można kilka przemyśleć i przetestować
	
	osobnik crossedUnit = crossUnits(newPopulation[i], newPopulation(POPULATION_SIZE-1));
	crossedPopulation.push(crossedUnit); // dodaj nowego
	crossedPopulation.push(newPopulation[i]); // i starego tez, bo dobry byl w miare xD
}
```
### 5. Mutacja
```
int ratio = POPULATION_SIZE/4; //np mutujemy tylko 1/8 naszych osobników
mutatePopulation(&crossedPopulation, ratio);
```
### 6. Powrót do punktu 2


## KONIEC ANALIZY