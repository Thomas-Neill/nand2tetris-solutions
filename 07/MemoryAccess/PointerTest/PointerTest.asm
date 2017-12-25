//Push(source=CONSTANT, where=3030)
@3030
D=A
@SP
M=M+1
A=M-1
M=D
//Pop(source=POINTER, where=0)
@SP
M=M-1
A=M
D=M
M=0
@3
M=D
//Push(source=CONSTANT, where=3040)
@3040
D=A
@SP
M=M+1
A=M-1
M=D
//Pop(source=POINTER, where=1)
@SP
M=M-1
A=M
D=M
M=0
@4
M=D
//Push(source=CONSTANT, where=32)
@32
D=A
@SP
M=M+1
A=M-1
M=D
//Pop(source=THIS, where=2)
@SP
M=M-1
A=M
D=M
M=0
@THIS
A=M
A=A+1
A=A+1
M=D
//Push(source=CONSTANT, where=46)
@46
D=A
@SP
M=M+1
A=M-1
M=D
//Pop(source=THAT, where=6)
@SP
M=M-1
A=M
D=M
M=0
@THAT
A=M
A=A+1
A=A+1
A=A+1
A=A+1
A=A+1
A=A+1
M=D
//Push(source=POINTER, where=0)
@3
D=M
@SP
M=M+1
A=M-1
M=D
//Push(source=POINTER, where=1)
@4
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
//Push(source=THIS, where=2)
@2
D=A
@THIS
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
//Push(source=THAT, where=6)
@6
D=A
@THAT
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
