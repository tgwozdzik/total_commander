# total_commander

Celem projektu jest napisanie w Javie aplikacji wzorowanej na programie
Total Commander Christiana Ghislera. Projekt i implementacja interfejsu
użytkownika aplikacji powinien zostać wykonany w dowolnej technologii/
frameworku.
Minimalny zakres funkcji realizowanych przez program sprowadza się do
operacji kopiowania, przesuwania i usuwania wykonywanych na zaznaczonych
plikach i folderach.

Uwagi dotyczące projektu i implementacji programu:

- [X] wygląd i funkcjonalność interfejsu użytkownika powinien być w maksymalnym
  stopniu wzorowany na programie Total Commander (układ okien, menu,
  listwa narzędziowa, panele z zakładkami wyświetlającymi listy pliki
  i folderów, sposób sortowania plików i folderów na listach, sposób
  "poruszania się" po strukturze systemu plików itp.)

- [X] aplikacja powinna być zaprojektowana w taki sposób, aby interfejs
  użytkownika był niezależny od kodu realizujacego wybrane operacje na
  plikach i folderach

- [X] komponent przedstawiający listę plików i folderów powinien wyświetlać
  w poszczególnych kolumnach: nazwę, rozmiar (tylko dla plików) i datę
  utworzenia (dodatkowe kolumny są opcjonalne)

- [X] komponent pokazujący listy plików i folderów powinien działać prawidłowo
  zarówno pod systemem Windows jak i Linux (m.in. usuń napęd A: pod
  Windowsami, użyj metod File.listFiles() i File.listRoots() do pobierania
  list plików i korzeni systemów plików)

- [X] prezentowane w panelach listy plików i folderów powinny być niezależnie
  sortowane wg wskazanych komumn

- [X] aplikacja powinna umożliwiać zaznaczanie plików i folderów za pomocą
  myszki (opcjonalnie także klawiatury)

- [X] przesyłanie informacji między komponentami aplikacji należy realizować
  z użyciem zdarzeń i listenerów

- [X] należy zwrócić uwagę, że operacje wykonywane na plikach mogą być
  długotrwałe, lecz nie powinny one powodować "zakłóceń" w funkcjonowaniu
  interfejsu użytkownika

- [X] przed usunięciem plików (folderów) lub jeśli zajdzie taka konieczność
  podczas kopiowania lub przesuwania plików należy zażądać od użytkownika
  potwierdzenia wykonania operacji

- [X] podczas wykonywania operacji na plikach należy wyświetlić okno z paskiem
  postępu i przyciskiem pozwalającym na anulowanie bieżącej operacji

- [X] po wykonaniu operacji na plikach należy zaktualizować listy wyświetlanych
  plików i folderów

- [X] aplikacja powinna umożliwiać dynamiczną zmianę wersji językowej interfejsu
  (zmiana wyświetlanych tekstów każdego elementu interfejsu użytkownika,
  dostosowanie sposobu wyświetlania daty i czasu utworzenia plików) 

Wymagania opcjonalne:

~~- [ ] dodanie opcji pozwalającej na wykonywanie operacji na plikach w tle
  ("move to background")~~

~~- [ ] implementacja asynchronicznego mechanizmu powiadamiającego o zmianach
  w systemie plików i odświeżającego listy wyświetlanych plików~~

~~- [ ] implementacja podglądu zawartości plików (np. na bazie komponentu
  z programu JEdit), itp.~~
