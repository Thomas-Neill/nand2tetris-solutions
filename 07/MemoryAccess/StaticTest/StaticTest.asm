//Push(source=CONSTANT, where=111)
@111
D=A
@SP
M=M+1
A=M-1
M=D
//Push(source=CONSTANT, where=333)
@333
D=A
@SP
M=M+1
A=M-1
M=D
//Push(source=CONSTANT, where=888)
@888
D=A
@SP
M=M+1
A=M-1
M=D
//Pop(source=STATIC, where=8)
@SP
M=M-1
A=M
D=M
M=0
@StaticTest.8
M=D
//Pop(source=STATIC, where=3)
@SP
M=M-1
A=M
D=M
M=0
@StaticTest.3
M=D
//Pop(source=STATIC, where=1)
@SP
M=M-1
A=M
D=M
M=0
@StaticTest.1
M=D
//Push(source=STATIC, where=3)
@StaticTest.3
D=M
@SP
M=M+1
A=M-1
M=D
//Push(source=STATIC, where=1)
@StaticTest.1
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
//Push(source=STATIC, where=8)
@StaticTest.8
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
