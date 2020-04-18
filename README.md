  Inainte de toate am ales Java pentru familiaritate si pentru usurinta de 
a folosi un limbaj orientat pe obiect intr-un proiect asa de mare.

Reprezentarea interna a mutarilor :
======

Am pornit de la idea ca vom reprezenta harta de sah cu numere pe 64 de biti
numite Biboards. Intern avem un numar de 12 bitboard-uri pentru fiecare tip
de piese (alb sau negru) si cateva bitboarduri helpere cum ar fi toate piesele
albe sau toate piesele negre. Fiecare bitboard are o valoare (int in care se
stocheaza valoarea piesei fata de un pion), un numar de piese (in care se
stocheaza cate piese de acel tip mai sunt pe tabla) si reprezentare 
(reprezentarea propriu-zisa care este un long pe 64 de biti, ce are in locul
in care ar fi o piese un bit setat ca 1). De exemplu la inceputul jocului
bitboardurile pentru pioni albi, pioni negri, toate piesele albe si toate
piesele negre arata asa:
```
pioni albi	    pioni negru    	toate piesele albe   toate negre
. . . . . . . .     . . . . . . . .     . . . . . . . .     # # # # # # # # 
. . . . . . . .     # # # # # # # #     . . . . . . . .     # # # # # # # # 
. . . . . . . .     . . . . . . . .     . . . . . . . .     . . . . . . . .
. . . . . . . .     . . . . . . . .     . . . . . . . .     . . . . . . . .
. . . . . . . .     . . . . . . . .     . . . . . . . .     . . . . . . . .
. . . . . . . .     . . . . . . . .     . . . . . . . .     . . . . . . . .
# # # # # # # #     . . . . . . . .     # # # # # # # #     . . . . . . . .
. . . . . . . .     . . . . . . . .     # # # # # # # #     . . . . . . . .

```
Operatiile cu bitboardurile:
======

 - Biti sunt setati la 1 sau la 0 in O(1) folosing masti pentru clear si masti
pentru set care sunt matrice pline cu 1 sau pline cu 0.
 - Un array de 120 si respectiv 64 de valori de la 1 la 120 si de la 1 la 64.
 Acestea vor fi folosite pentru bounds checking in urmatoarele etape ale 
 proiectului.
 - BoardStateul este un Singleton care este accesat global in metodele care au
nevoie de el pentru simplitate si pentru faptul ca o sa avem un singur 
BoardState la o rulare a unui meci de sah.
 - Bitboard-urile sunt generate din FEN-uri (notatii standard pentru sah), 
putem introduce orice FEN la functia init game si se va porni jocul dintr-o 
pozitie predefinita de FEN. Daca functia nu este apelata cu FEN aceasta va
folosi FEN-ul care codifica inceputul meciului: 
```
                       rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1 *
```

Parsarea si interpretarea comenzilor :
======

 - Comenzile sunt citite de la stdin intr-un loop infinit, singurul break
este atunci cand programul da quit.
 - Comenzile sunt implementate dupa documentatia de pe site-ul xboard-ului si
fac ce ar trebui ele sa faca intuitiv (white seteaza engine-ul ca white,
new incepe un joc nou, etc).
 - Clasa Database este un singleton este o clasa in care avem stocate 
informatii si metode care nu tin de starea pieselor ci de starea jocului(cine 
e la mutare, cate mutari s-au facut, cine urmeaza sa mute, ce culoare joaca
engine-ul etc.)

Feature :
======

 - usermove = 0, pentru a fi mai usor de parsat usermove
 - sigterm si sigint sunt pentru ca xboard sa nu ne crape programul cu semnale
 - reuse = 0, xboard deschide o instanta noua a programului la fiecare new (
 probabil nu o sa folosim reuse in build-ul final)
 - time = 0, urmeaza sa fie implementat, momentan nu avem treaba cu timpul
 
 Build and run :
 ======
 - xboard -fcp "java -jar hopefully.jar" -debug -nameOfDebugFile "plm.txt" (pentru noi cand suntem prosti si uitam)
 - make build (pentru a crea jar-ul si a compila codul sursa)
 - xboard -cp -fcp "make run" (pentru a rula xboard cu enginul nostru)
 - make clean pentru a sterge jar-ul si pentru a putea recompila sursele
