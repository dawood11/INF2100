/* Dette programmet er faktisk lovlig! */

program ManyNots;
var b: boolean;
begin
   b := not not True or not not not True and not not not not True;
   write('b', '=', b, EoL)
end.
