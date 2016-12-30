program NameError;
var X: Integer;

function Doble (V: Integer): Integer;
begin
   Doble := 2*V;  X := X + 2*V
end; {Doble}

begin
   Doble(6);
   write('X', '=', X, EoL)
end.
