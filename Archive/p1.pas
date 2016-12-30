program p1;
var i: integer;
    p: integer;
begin
   i := 0;  p := 1;
   while i <= 10 do begin
      write(2+2, '^', i, '=', p, eol);
      i := i+1;  p := 2*p;
   end;
end.