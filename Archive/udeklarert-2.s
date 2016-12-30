# Code file created by Pascal2016 compiler 2016-10-27 16:25:09
        .globl  main                    
main:
        call    prog$udeklarert_1       # Start program
        movl    $0,%eax                 # Set status 0 and
        ret                             # terminate the program
proc$p_2:
        enter   $36,$2                  # Start of p
        movl    -4(%ebp),%edx           
        movl    -36(%edx),%eax          #   x
        pushl   %eax                    
        movl    $0,%eax                 #   0
        popl    %ecx                    
        cmpl    %eax,%ecx               
        movl    $0,%eax                 
        sete    %al                     # Test =
        movl    -8(%ebp),%edx           
        movl    %eax,-36(%edx)          # v :=
        leave                           # End of p
        ret                             
prog$udeklarert_1:
        enter   $36,$1                  # Start of udeklarert
        call    proc$p_2                
        movl    $88,%eax                #   'X'
        pushl   %eax                    # Push next param.
        call    write_char              
        addl    $4,%esp                 # Pop param.
        movl    $61,%eax                #   '='
        pushl   %eax                    # Push next param.
        call    write_char              
        addl    $4,%esp                 # Pop param.
        movl    -4(%ebp),%edx           
        movl    -36(%edx),%eax          #   x
        pushl   %eax                    # Push next param.
        call    write_int               
        addl    $4,%esp                 # Pop param.
        movl    $10,%eax                #   10
        pushl   %eax                    # Push next param.
        call    write_char              
        addl    $4,%esp                 # Pop param.
        leave                           # End of udeklarert
        ret                             
