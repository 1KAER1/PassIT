# PassIT

PassIT - Aplikacja mobilna służąca do organizowania zadań i obowiązków studenta

Uruchomienie aplikacji po raz pierwszy rozpoczyna proces dodawania nowego profilu
![nowy_profil](https://user-images.githubusercontent.com/60784606/234191536-ce4a9714-da72-450a-92bc-cd8d72d2b7ed.png)

Głównym widokiem po włączeniu aplikacji, w przypadku, gdy aplikacja znajdzie profil w bazie danych, będzie menu główne, w którym znajdziemy istotne informacje, a także przyciski w formie kafelków, dzięki którym możemy przejść do innych aktywności

![Menu](https://user-images.githubusercontent.com/60784606/234199666-6d7b66e7-22d6-4330-8cd0-a0dabbaa0ef4.png)

PRZEDMIOTY

![przedmioty](https://user-images.githubusercontent.com/60784606/234192062-694fbe46-d455-459e-bf9f-dea6306f9553.png)

Widok przedmiotów przedstawia użytkownikowi kafelki z nazwą przedmiotu, przycisk, dzięki któremu może dodać nowy przedmiot a także lupą, która służy do wyszukiwania przedmiotu po nazwie. Na każdym kafelku do dyspozycji użytkownika są 2 przyciski. Pierwszy z nich, w formie znaku potwierdzenia, pozwala zaznaczyć przedmiot jako zaliczony. Po oznaczeniu przedmiotu jako zaliczonego, kafelek na którym wyświetlany jest poszczególny przedmiot, 42 zostanie przyciemniony, nazwa przedmiotu przekreślona, a napis „W trakcie” zamieni się w „Zaliczony”.

Drugi z przycisków służy do usuwania przedmiotu. Należy być jednak uważnym, ponieważ usunięcie przedmiotu usunie również wszystkie przypisane do niego obowiązki. Usunięcie przedmiotu poprzedza wyświetlenie okna dialogowego, na którym wyświetlana jest informacja o konsekwencjach usunięcia przedmiotu. W przypadku zmiany zdania użytkownik może wcisnąć przycisk „Nie”, dzięki czemu przedmiot nie zostanie usunięty.

![usun](https://user-images.githubusercontent.com/60784606/234192705-3fd05da7-1590-4114-b9e2-8e8e7612aac9.png)

Kolejny z przycisków w widoku przedmiotów służy do wyszukiwania przedmiotów po ich nazwie. Znajduje się on w prawym górnym rogu. Po kliknięciu w niego możemy rozpocząć wpisywanie frazy.

![wyszukiwanie_przedmiotu](https://user-images.githubusercontent.com/60784606/234192854-0d594662-a1a5-444c-9e95-36c2cfd976db.png)

Wyświetlone zostaną przedmioty, które będą zawierać podaną przez użytkownika frazę. Aby wyczyścić wpisaną frazę, należy wcisnąć ikonkę „X” znajdującą się po prawej stronie. Ikonka strzałki pozwoli na wyjście z opcji wyszukiwania. Kolejny przycisk znajdujący się w prawym dolnym rogu (zielone kółko z „+” w środku), pozwala użytkownikowi na dodanie nowego przedmiotu. Po kliknięciu przycisku otwiera się nowy widok, na którym użytkownik ma możliwość na podanie poszczególnych informacji.

![nowy_przedmiot](https://user-images.githubusercontent.com/60784606/234192957-fa375050-0c08-4d2b-be11-8e44ff0f62db.png)

Aby dodać nowy przedmiot należy uzupełnić dane o nazwie przedmiotu, zaznaczyć jaki typ zajęć jest odbywany w przypadku dodawanego przedmiotu, wpisać punkty ECTS za przedmiot i wybrać ważność przedmiotu. Jeśli wszystkie dane nie zostaną uzupełnione, a użytkownik spróbuje przejść dalej, wyświetlone zostanie powiadomienie z prośbą o wypełnienie wszystkich danych. W przypadku gdy użytkownik zdecyduje, że jednak nie chce dodawać nowego przedmiotu, może wyjść z widoku dodawania nowego przedmiotu przy pomocy menu nawigacyjnego w swoim telefonie, przy czym pierwsze naciśnięcie przycisku powrotu spowoduje wyświetlenie powiadomienia proszącego o ponowne wciśnięcie przycisku, aby potwierdzić opuszczenie widoku dodawania przedmiotu.

![toast](https://user-images.githubusercontent.com/60784606/234196535-6726bebc-7d41-4444-9ff9-557804f8d511.png)


OBOWIĄZKI

Najważniejszą funkcjonalnością aplikacji jest zakładka „Obowiązki”. Użytkownik może do nich przejść klikając kafelek z taką nazwą w menu głównym. Widok jest podobny do tego, który można zobaczyć w widoku przedmiotów, z tym że tutaj użytkownik ma możliwość sortowania obowiązków według różnych kryteriów

![obowiazki_widok](https://user-images.githubusercontent.com/60784606/234199969-8bb7581c-5009-4388-882a-79794dd03a85.png)

Z istotnych różnic między widokiem znanym z „Przedmiotów”, w obowiązkach możemy dostrzec, że teraz kafelki mają inne tytuły dzielące się na:
• Zaliczenie
• Zadanie
Przy czym oba tytuły otrzymują dopisek „Zaległe” jeśli minie ich termin, ustalony wcześnie przez użytkownika. Oprócz tego mamy tutaj znane już z widoku Przedmiotów przyciski zaznaczania obowiązku jako ukończonego, usuwania obowiązku, wyszukiwania obowiązku po frazie, a także przycisk dodawania nowego obowiązku. Nowością jest natomiast przycisk, dzięki któremu możemy posortować nasze obowiązki według zadanych kryteriów. Kryteria zostały wybrane zgodnie z założeniami macierzy Eisenhower’a, o której zostało wspomniane w rozdziale 2. Obowiązki mogą zostać posegregowane:

  • Od najpilniejszych  
  • Od najmniej pilnych  
  • Od najważniejszych  
  • Od najmniej ważnych  
  • Tylko ważne    
  • Tylko średnio ważne  
  • Tylko mało ważne

Istotną rolę odgrywa tutaj ważność obowiązku, którą użytkownik określa samodzielnie. Została ona podzielona na trzy kolory: czerwony (najważniejszy), żółty (średnio ważny) i zielony (mało ważny). Jest ona brana pod uwagę podczas sortowania obowiązków, tak aby określić, które z nich powinny być wyświetlane na początku.

![sortowanie_eisenhower](https://user-images.githubusercontent.com/60784606/234200213-d998e3d2-8883-4262-8c15-f5557f4b6a48.png)

Sortowanie ma za zadanie usprawnić proces przydzielania poszczególnych obowiązków do konkretnych ćwiartek macierzy Eisehower’a, których jest 4, tak jak to zostało wspomniane w rozdziale 2. Wcześniejsze zaznaczenie ważności zadania oraz dodanie terminu w połączeniu z odpowiednim posortowaniem pomoże użytkownikowi na wybranie obowiązków, którymi powinien się zająć na początku. Pozwoli mu to na efektywniejsze zarządzanie czasem oraz zasobami. Wykonanie zadań, które na macierzy Esienhower’a znajdują się w I ćwiartce, w pierwszej kolejności powinno wpłynąć pozytywnie na dalszy proces wykonywania zadań przez użytkownika. W przypadku Przedmiotów mieliśmy do dyspozycji przycisk do dodawania nowego przedmiotu, tutaj mamy podobny przycisk, z tym że służący do dodawania nowego obowiązku. Po kliknięciu przycisku użytkownik jest przenoszony do widoku, w którym ma możliwość uzupełnienia podstawowych informacji o nowym obowiązku, takich jak:

  • Nazwa obowiązku  
  • Typ obowiązku  
  • Przypisany przedmiot  
  • Typ przypisanego przedmiotu  
  • Termin oddania zadania, bądź termin zaliczenia
  • Ważność obowiązku  
  • Przycisk pozwalający na dodanie ścieżki do pliku pdf z zadaniem, bądź
  ważnymi informacjami o zaliczeniu  
  • Oraz opis, który jest opcjonalny

![obowiazek_widok](https://user-images.githubusercontent.com/60784606/234200380-a58cb915-92eb-4006-b1d4-bd4b0176242b.png)

![spinner_obowiazek](https://user-images.githubusercontent.com/60784606/234200589-d877144d-8d01-4bb1-bc93-7685f875904c.png)

![obowiazek_wybieranie_daty_czasu](https://user-images.githubusercontent.com/60784606/234200466-4e34212d-5cf9-4b34-8100-e78e48e22d3d.png)

![obowiazek_plik](https://user-images.githubusercontent.com/60784606/234200525-27b3d996-f0bc-4bc1-bf66-057898f94a6d.png)


POWIADOMIENIA

Aplikacja będzie powiadamiać użytkownika o zbliżającym się terminie obowiązku zanim ten minie. Dzięki temu użytkownik nie zapomni o tym, że ma zadanie do wykonania, co w dzisiejszych czasach zdarza się notorycznie. Użytkownik zostanie również powiadomiony, jeśli termin obowiązku upłynie

![powiadomienie](https://user-images.githubusercontent.com/60784606/234201764-7457686b-3dcd-4b8a-bb48-fa790e20568b.png)


NOTATKI

Kolejną funkcjonalnością, do której mamy dostęp z poziomu menu głównego są notatki. Służą one przede wszystkim do zapisywania krótkich informacji, przypomnień. Z założenia miały służyć użytkownikowi do zapisywania informacji, które nie były przewidziane jako związane bezpośrednio z zajęciami

![notatki_widok](https://user-images.githubusercontent.com/60784606/234201893-669b5e76-e0f0-4108-b5bb-2a54bb1ed17f.png)


KALENDARZ

Otwierając kalendarz użytkownik zauważyć może, że poszczególne daty będą zaznaczone innym kolorem i będą podkreślone. Będzie to oznaczać, że w danym dni dostępne są obowiązki do zrealizowania. Po kliknięciu w dany dzień, w którym dostępne są zadania, pod kalendarzem pojawią się one w postaci kafelków. Każdy kafelek wygląda dokładnie tak samo jak te, które użytkownik może znaleźć w widoku z Obowiązkami. Użytkownik może swobodnie zaznaczać zadania jako ukończone, usuwać je, a także klikać bezpośrednio na kafelek, aby przejść do szczegółowych informacji o obowiązku

![kalendarz_widok](https://user-images.githubusercontent.com/60784606/234202107-e67cf7df-e979-401a-8cd9-951e97fcdc81.png)
