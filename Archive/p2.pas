
program P2;

const N = 6;

var A: array [1..N] of Integer;

function F: Integer;
var I: Integer;  X: Integer;
begin
   X := A[1];
   I := 2;
   while I <= N do begin
      if A[I] > X then X := A[I];
      I := I+1
   end;
   F := X
end; {F}

begin
   A[1] := 17;  A[2] := -2;  A[3] := 22;
   A[4] := 12;  A[5] :=  0;  A[6] :=  9;

   write('S', F)
end.