# Code file created by Pascal2016 compiler 2016-12-12 14:04:20
        .globl main                         
main:
        call    prog$redefinisjon_1     # Start program
        movl    $0,%eax                 # Set status 0 and
        ret                             # terminate the program
proc$writeint_2:
        enter   $32,$2                  # Start of writeint
        movl    -8(%ebp),%edx           
        movl    8(%edx),%eax            # x
        pushl   %eax                    # Push next param.
        call    write_int               
        addl    $4,%esp                 # Pop param.
        leave                           
        ret                             
proc$writechar_3:
        enter   $32,$2                  # Start of writechar
        movl    -8(%ebp),%edx           
        movl    8(%edx),%eax            # x
        pushl   %eax                    # Push next param.
        call    write_char              
        addl    $4,%esp                 # Pop param.
        leave                           
        ret                             
proc$write_4:
        enter   $32,$2                  # Start of write
        movl    -8(%ebp),%edx           
        movl    8(%edx),%eax            # c
        pushl   %eax                    # Push next param.
        call    proc$writechar_3        
        addl    $4,%esp                 
        movl    $61,%eax                #  '='
        pushl   %eax                    # Push next param.
        call    proc$writechar_3        
        addl    $4,%esp                 
        movl    -8(%ebp),%edx           
        movl    12(%edx),%eax           # v
        pushl   %eax                    # Push next param.
        call    proc$writeint_2         
        addl    $4,%esp                 
        pushl   %eax                    # Push next param.
        call    proc$writechar_3        
        addl    $4,%esp                 
        leave                           
        ret                             
prog$redefinisjon_1:
        enter   $36,$1                  # Start of redefinisjon_1
        movl    $42,%eax                #  42
        movl    -4(%ebp),%edx           
        movl    %eax,-36(%edx)          # x :=
        movl    -4(%ebp),%edx           
        movl    -36(%edx),%eax          # x
        pushl   %eax                    # Push next param.
        movl    $120,%eax               #  'x'
        pushl   %eax                    # Push next param.
        call    proc$write_4            
        addl    $8,%esp                 
        leave                           
        ret                             
