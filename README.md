<a name="br1"></a> 

Etap 4

Etalon Pierwotny Sigmy

Michał Musielak 281092

Mikołaj Bożejko 281163

<https://github.com/ComprexEu/EcosystemSimulation>

Temat: Symulacja ekosystemu (w języku Java) - Mapa, będzie składała się z

elementów klasy WaterTile oraz GrassTile (dziedziczących po Tile). Na polach Gras-

sTile zostaną losowo wygenerowani agenci (gatunki różnych roślin, roślinożerców oraz

drapieżników, które będą dziedziczyć kolejno po klasach: Plant, Herbivore i Preda-

tor). Rośliny na początku symulacji będą wyrośnięte i w momencie, gdy zostaną

zjedzone przez roślinożercę, będą odrastać przez ustalony czas. Poszczególne gatunki

roślin będą różniły się czasem rośnięcia oraz efektywnością zaspokajania głodu ro-

ślinożerców. Głównym celem zwierząt będzie przedłużenie gatunku. Aby zwierzęta

rozmnożyły się, muszą zostać spełnione 2 warunki:

• muszą zaspokoić swoje potrzeby, którymi są spragnienie oraz głód

• musi się spotkać dwójka zwierząt różnych płci

Wszystkie zwierzęta będą zaspokajać spragnienie poprzez podejście do WaterTile

na mapie. Na początku symulacji oraz po każdym rozmnożeniu potrzeby agentów

resetują się.

Roślinożercy:

• będą zaspakajać głód poprzez jedzenie roślin

• gdy jakiś drapieżnik wejdzie w ich pole widzenia będą od niego uciekać

Drapieżnicy:

• ich pożywieniem są roślinożercy

• zwiększona prędkość poruszania się

• zwiększone pole widzenia

Symulacja będzie się kończyła po upływie ustalonego czasu lub gdy wszystkie zwie-

rzęta wymrą.

Celem symulacji będzie zbadanie wpływu różnych parametrów (na przykład: szyb-

kość, pole widzenia, liczność gatunku przy inicjalizacji) na zmianę populacji zwierząt

i pokazanie wyników na wykresach.

Symulacja zostanie zwizualizowana za pomocą JFrame lub konsoli.

1



<a name="br2"></a> 

Poniżej przedstawiono odpowiednio diagram przypadków użycia oraz diagramy

obiektów. Dla prostszego zrozumienia przypadków, diagramy zostały zrobione dla

jednego typu drapieżnika, roślinożercy i rośliny. Każdy kolejny typ, który wystąpi w

symulacji będzie miał taką samą logikę, lecz różne wartości zmiennych.

Rysunek 1: Diagram przypadków użcia

Decyzje podejmowane przez zwierzęta będą miały pewną hierarchię. W każdej

iteracji zwierzę będzie się poruszało. Jeśli w polu widzenia nie będzie nic co potrzebuje

zwierzę, poruszy się ono w sposób losowy. W przeciwnym wypadku, poruszy się ono

w stronę zapotrzebowania, bądź w stronę przeciwną w przypadku uciekania. Ucieczka

przed drapieżnikiem zawsze będzie miała największy priorytet (tzn. jeśli drapieżnik

znajduje się w polu widzenia roślinożercy to roślinożerca ucieka). Do kolejnych ruchów

możemy przyjąć następującą hierarchię:

• Szukanie roślinożerców przez drapieżnika - jeśli wartość saturacji mniejsza od

spragnienia oraz poniżej 40

2



<a name="br3"></a> 

• Szukanie roślin przez roślinożercę - jeśli wartość saturacji mniejsza lub równa

(żeby uniknąć przypadku, w którym nie szukają niczego, a powoli umierają)od

spragnienia oraz poniżej 40

• Szukanie wody - jeśli wartość spragnienia mniejsza od saturacji oraz poniżej 40

• Szukanie gatunku płci przeciwnej - jeśli saturacja i spragnienie większe od 40

Dodatkowo, co każdą iterację, wartości saturacji i spragnienia zwierząt zmniejszają

się o 2. Zmiany statystyk (zmiana satuarcji i spragnienia, a także atakowanie, jedzenie

itp.) dzieją się w tej samej iteracji co ruch, ale za ruch dodatkowo poza zmianą pozycji

uznajemy rozmnażenie się.

Logika roślin będzie znacznie prostsza. Jeśli zostaną zjedzone przez roślinożercę -

jego saturacja zostanie zwiększona o określoną wartość, a roślina zacznie spowrotem

wyrastać przez określoną ilość iteracji.

Poniżej przedstawiono wyżej opisane przypadki na diagramach obiektów, dla uła-

twienia, zakładamy, że pomiędzy obiektami jest trawa, więc mogą się swobodnie

poruszać.

Rysunek 2: Diagram obiektów - ucieczka przed drapieżnikiem

Według hierarchii, dla roślinożercy został spełniony warunek ucieczki, za to dla

drapieżnika, warunek szukania (gonienia) roślinożercy. Na kolejnej stronie przedsta-

wiono sytuację po 2 iteracjach symulacji.

3



<a name="br4"></a> 

Rysunek 3: Diagram obiektów - ucieczka przed drapieżnikiem - drapieżnik atakuje

Można zauważyć, że wartość zmiennej health zmniejszyła się o 10. Jest to spo-

wodowane tym, że jeśli drapieżnik znajduje się obok roślinożercy, którego atakuje,

zadaje mu obrażenia (health -= damage). Drapieżnik dalej goni roślinożercę, lecz

ponieważ jest on szybszy, roślinożercy nie uda się uciec.

Następnym przypadkiem jest szukanie rośliny przez roślinożercę:

Rysunek 4: Diagram obiektów - szukanie rośliny

4



<a name="br5"></a> 

Wartość saturacji jest mniejsza niż wartość spragnienia oraz jest ona mniejsza

od 40. Jedzenie jest w polu widzenia, więc roślinożerca zbliża się do niego. Poniżej

przedstawiona została sytuacja po jednej iteracji, a co za tym stoi, przy zjedzeniu

rzepy przez owcę (saturacja zwiększyła się o 30, ale jest to więcej niż maxSaturation,

więc zwiększyła się do wartości maxSaturation). Wartość zmiennej growthState dla

Turnipa zwiększyła się do 3. W każdej następnej iteracji będzie się zmniejszała co 1,

aż osiągnie 0, wtedy roślina będzie wyrośnięta.

Rysunek 5: Diagram obiektów - szukanie rośliny - roślina zjedzona

Przypadek szukania wody jest analogiczny. Pierwotny pomysł zrobienia terenu

jako dwuwymiarowej tablicy obiektów GrassTile i WaterTile dziedziczących po Tile

zmienił się. Zostanie ona zaimplementowana jako tablica enumów (enum Terrain z

polami WATER oraz GRASS). Z tego powodu, przedstawienie tego przypadku na

diagramie obiektów nie jest możliwe.

Ostatnim przypadkiem jest szukanie płci przeciwnej w celu rozmnożenia się:

Rysunek 6: Diagram obiektów - szukanie gatunku płci przeciwnej

5



<a name="br6"></a> 

Zostały spełnione warunki do rozmnożenia się. W tym przypadku owce zbliżają

się do siebie i jak do siebie dojdą (po dwóch iteracjach), obok owcy płci żeńskiej

pojawi się nowa owca (po kolejnej iteracji).

Rysunek 7: Diagram obiektów - szukanie gatunku płci przeciwnej - nowo urodzona

owca

Poniżej dodatkowo przedstawiono bardziej złożony przypadek:

Rysunek 8: Diagram obiektów - złożony przypadek

6

