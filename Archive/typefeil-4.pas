program TypeError;

function F (A: Integer): Integer;
begin
   if A mod 2 = 0 then
       F := 'E'
   else
       F := 'O'
end; {F}

begin
   write(F(2)+1, EoL)
end.
