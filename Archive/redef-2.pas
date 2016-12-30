/* Selv om man redefinerer biblioteksdeklarasjoner,
   affiserer det ikke  tidligere forekomster.
   Dette programmet er lovlig og skriver ut

   x=42
*/

program Redefinisjon;
var x: integer;

procedure WriteInt (X: Integer);
begin
   write(X) {Her kaller vi på den originale 'write'.}
end; {WriteInt}

procedure WriteChar (X: Char);
begin 
   write(X) {Det gjoer vi her ogsaa.}
end; {WriteChar}

procedure write (C: char; V: integer);
begin
   WriteChar(C);  WriteChar('=');  WriteInt(V);  WriteChar(eol)
end; {write}

begin
   x := 42;  
   write('x', x) {Her kaller vi på den redefinerte 'write'.}
end.
