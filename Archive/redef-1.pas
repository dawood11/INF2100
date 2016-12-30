/* Det er lov å redefinere ting i biblioteket,
   så dette programmet er lovlig og skriver ut

   42,-17!   (og så et linjeskift (dvs en ekte EoL) til slutt)
*/

program Redefinisjon;
var Char: Integer;

procedure Boolean (EoL: Integer);
begin
   write(Char, ',', EoL)
end;

begin
   Char := 42;
   Boolean(-17);
   write('!', EoL)
end.
