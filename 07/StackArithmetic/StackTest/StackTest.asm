@17
D=A
@SP
M=M+1
A=M
A=A-1
M=D
@17
D=A
@SP
M=M+1
A=M
A=A-1
M=D
@SP
M=M-1
A=M
D=M
M=0
@R13
M=D
@SP
M=M-1
A=M
D=M
M=0
@R13
D=D-M
M=0
@EQ0
D;JEQ
@DONE0
0;JMP
(EQ0)
@R13
M=-1
(DONE0)
@R13
D=M
@SP
M=M+1
A=M-1
M=D
@17
D=A
@SP
M=M+1
A=M
A=A-1
M=D
@16
D=A
@SP
M=M+1
A=M
A=A-1
M=D
@SP
M=M-1
A=M
D=M
M=0
@R13
M=D
@SP
M=M-1
A=M
D=M
M=0
@R13
D=D-M
M=0
@EQ1
D;JEQ
@DONE1
0;JMP
(EQ1)
@R13
M=-1
(DONE1)
@R13
D=M
@SP
M=M+1
A=M-1
M=D
@16
D=A
@SP
M=M+1
A=M
A=A-1
M=D
@17
D=A
@SP
M=M+1
A=M
A=A-1
M=D
@SP
M=M-1
A=M
D=M
M=0
@R13
M=D
@SP
M=M-1
A=M
D=M
M=0
@R13
D=D-M
M=0
@EQ2
D;JEQ
@DONE2
0;JMP
(EQ2)
@R13
M=-1
(DONE2)
@R13
D=M
@SP
M=M+1
A=M-1
M=D
@892
D=A
@SP
M=M+1
A=M
A=A-1
M=D
@891
D=A
@SP
M=M+1
A=M
A=A-1
M=D
@SP
M=M-1
A=M
D=M
M=0
@R13
M=D
@SP
M=M-1
A=M
D=M
M=0
@R13
D=D-M
M=0
@LT3
D;JLT
@DONE3
0;JMP
(LT3)
@R13
M=-1
(DONE3)
@R13
D=M
@SP
M=M+1
A=M-1
M=D
@891
D=A
@SP
M=M+1
A=M
A=A-1
M=D
@892
D=A
@SP
M=M+1
A=M
A=A-1
M=D
@SP
M=M-1
A=M
D=M
M=0
@R13
M=D
@SP
M=M-1
A=M
D=M
M=0
@R13
D=D-M
M=0
@LT4
D;JLT
@DONE4
0;JMP
(LT4)
@R13
M=-1
(DONE4)
@R13
D=M
@SP
M=M+1
A=M-1
M=D
@891
D=A
@SP
M=M+1
A=M
A=A-1
M=D
@891
D=A
@SP
M=M+1
A=M
A=A-1
M=D
@SP
M=M-1
A=M
D=M
M=0
@R13
M=D
@SP
M=M-1
A=M
D=M
M=0
@R13
D=D-M
M=0
@LT5
D;JLT
@DONE5
0;JMP
(LT5)
@R13
M=-1
(DONE5)
@R13
D=M
@SP
M=M+1
A=M-1
M=D
@32767
D=A
@SP
M=M+1
A=M
A=A-1
M=D
@32766
D=A
@SP
M=M+1
A=M
A=A-1
M=D
@SP
M=M-1
A=M
D=M
M=0
@R13
M=D
@SP
M=M-1
A=M
D=M
M=0
@R13
D=D-M
M=0
@GT6
D;JGT
@DONE6
0;JMP
(GT6)
@R13
M=-1
(DONE6)
@R13
D=M
@SP
M=M+1
A=M-1
M=D
@32766
D=A
@SP
M=M+1
A=M
A=A-1
M=D
@32767
D=A
@SP
M=M+1
A=M
A=A-1
M=D
@SP
M=M-1
A=M
D=M
M=0
@R13
M=D
@SP
M=M-1
A=M
D=M
M=0
@R13
D=D-M
M=0
@GT7
D;JGT
@DONE7
0;JMP
(GT7)
@R13
M=-1
(DONE7)
@R13
D=M
@SP
M=M+1
A=M-1
M=D
@32766
D=A
@SP
M=M+1
A=M
A=A-1
M=D
@32766
D=A
@SP
M=M+1
A=M
A=A-1
M=D
@SP
M=M-1
A=M
D=M
M=0
@R13
M=D
@SP
M=M-1
A=M
D=M
M=0
@R13
D=D-M
M=0
@GT8
D;JGT
@DONE8
0;JMP
(GT8)
@R13
M=-1
(DONE8)
@R13
D=M
@SP
M=M+1
A=M-1
M=D
@57
D=A
@SP
M=M+1
A=M
A=A-1
M=D
@31
D=A
@SP
M=M+1
A=M
A=A-1
M=D
@53
D=A
@SP
M=M+1
A=M
A=A-1
M=D
@SP
M=M-1
A=M
D=M
M=0
A=A-1
M=D+M
@112
D=A
@SP
M=M+1
A=M
A=A-1
M=D
@SP
M=M-1
A=M
D=M
M=0
A=A-1
M=M-D
@SP
A=M-1
M=-M
@SP
M=M-1
A=M
D=M
M=0
A=A-1
M=M&D
@82
D=A
@SP
M=M+1
A=M
A=A-1
M=D
@SP
M=M-1
A=M
D=M
M=0
A=A-1
M=M|D
@SP
A=M-1
M=!M