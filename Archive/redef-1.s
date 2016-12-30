# Code file created by Pascal2016 compiler 2016-12-12 14:00:42
        .globl main                         
main:
        call    prog$redefinisjon_1     # Start program
        movl    $0,%eax                 # Set status 0 and
        ret                             # terminate the program
proc$boolean_2:
        enter   $32,$2                  # Start of boolean
        movl    -4(%ebp),%edx           
        movl    -36(%edx),%eax          # char
        pushl   %eax                    # Push next param.
        call    write_int               
        addl    $4,%esp                 # Pop param.
        movl    $44,%eax                #  ','
        pushl   %eax                    # Push next param.
        call    write_char              
        addl    $4,%esp                 # Pop param.
        movl    -8(%ebp),%edx           
        movl    8(%edx),%eax            # eol
        movl    $10,%eax                # char 10
        pushl   %eax                    # Push next param.
        call    write_char              
        addl    $4,%esp                 # Pop param.
        leave                           
        ret                             
prog$redefinisjon_1:
        enter   $36,$1                  # Start of redefinisjon_1
        movl    $42,%eax                #  42
        movl    -4(%ebp),%edx           
        movl    %eax,-36(%edx)          # char :=
        movl    $17,%eax                #  17
        negl    %eax                    
        pushl   %eax                    # Push next param.
        call    proc$boolean_2          
        addl    $4,%esp                 
        movl    $33,%eax                #  '!'
        pushl   %eax                    # Push next param.
        call    write_char              
        addl    $4,%esp                 # Pop param.
        movl    $10,%eax                # char 10
        pushl   %eax                    # Push next param.
        call    write_char              
        addl    $4,%esp                 # Pop param.
        leave                           
        ret                             
