program DoubleVar;

var a: char;

procedure p;
begin
   a := 'a';
end; { p }

var b: integer;

function q: integer;
begin
   q := b;
end; { q }

begin
   p;
   b := 177;
   write('b', '=', q, eol)
end.
