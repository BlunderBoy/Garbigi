  Inainte de toate am ales Java pentru familiaritate si pentru usurinta de 
a folosi un limbaj orientat pe obiect intr-un proiect asa de mare.

Reprezentarea interna a starilor:
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
pioni albi	    pioni negru    	toate piesele albe  toate negre
. . . . . . . .     . . . . . . . .     . . . . . . . .     # # # # # # # # 
. . . . . . . .     # # # # # # # #     . . . . . . . .     # # # # # # # # 
. . . . . . . .     . . . . . . . .     . . . . . . . .     . . . . . . . .
. . . . . . . .     . . . . . . . .     . . . . . . . .     . . . . . . . .
. . . . . . . .     . . . . . . . .     . . . . . . . .     . . . . . . . .
. . . . . . . .     . . . . . . . .     . . . . . . . .     . . . . . . . .
# # # # # # # #     . . . . . . . .     # # # # # # # #     . . . . . . . .
. . . . . . . .     . . . . . . . .     # # # # # # # #     . . . . . . . .
```
Reprezentarea interna a mutarilor:
======
- In total 4 nibbles pentru a codifica o mutare. En passant se seteaza cand se poate captura un pion en passant.
```
mutare:
	0000 0000 0011 1111 sursa (2^6 64 de valori, de la 0 la 63)
	0000 1111 1100 0000 destinatie (2^6 64 de valori, de la 0 la 63)
	0011 0000 0000 0000 promotie (4 valori - cal 0 (00), nebun 01 (01), tura (10), regina (11))
	1100 0000 0000 0000 flag (4 valori - promotie 1 (01), en passant 2 (10), castling 3 (11))
 ``` 
Indexarile interne:
======
 - Avem 2 tipuri de indexari, una normala, pe 64 de pozitii, care reprezinta 1:1
tabla de joc, si una pe 120 de pozitii, care ne ajuta la bounds checking (sa nu
iasa piesele de pe un perete si sa intre pe altul)

![Indexare pe 64](https://github.com/BlunderBoy/sah/blob/master/readme_resources/64indexes.jpg)

![Indexare pe 120](https://github.com/BlunderBoy/sah/blob/master/readme_resources/120indexes.png)

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

<p align="center">
<code>
rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1 *
</code>
</p>




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
