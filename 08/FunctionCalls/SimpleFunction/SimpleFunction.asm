//Function(name=SimpleFunction.test, locals=2)
(SimpleFunction.test)
@SP
M=M+1
A=M-1
M=0
@SP
M=M+1
A=M-1
M=0
//Push(source=LOCAL, where=0)
@0
D=A
@LCL
A=D+M
D=M
@SP
M=M+1
A=M-1
M=D
//Push(source=LOCAL, where=1)
@1
D=A
@LCL
A=D+M
D=M
@SP
M=M+1
A=M-1
M=D
//StackOperation(type=ADD)
@SP
M=M-1
A=M
D=M
M=0
A=A-1
M=D+M
//StackOperation(type=NOT)
@SP
A=M-1
M=!M
//Push(source=ARGUMENT, where=0)
@0
D=A
@ARG
A=D+M
D=M
@SP
M=M+1
A=M-1
M=D
//StackOperation(type=ADD)
@SP
M=M-1
A=M
D=M
M=0
A=A-1
M=D+M
//Push(source=ARGUMENT, where=1)
@1
D=A
@ARG
A=D+M
D=M
@SP
M=M+1
A=M-1
M=D
//StackOperation(type=SUB)
@SP
M=M-1
A=M
D=M
M=0
A=A-1
M=M-D
//Return@5d6f64b1
@5
D=A
@LCL
A=M-D
D=M
@R13
M=D
@SP
A=M-1
D=M
@ARG
A=M
M=D
@ARG
D=M
@SP
M=D+1
@LCL
A=M-1
D=M
@THAT
M=D
@LCL
A=M-1
A=A-1
D=M
@THIS
M=D
@LCL
A=M-1
A=A-1
A=A-1
D=M
@ARG
M=D
@LCL
A=M-1
A=A-1
A=A-1
A=A-1
D=M
@LCL
M=D
@R13
A=M
0;JMP
