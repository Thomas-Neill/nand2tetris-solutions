// This file is part of www.nand2tetris.org
// and the book "The Elements of Computing Systems"
// by Nisan and Schocken, MIT Press.
// File name: projects/04/Fill.asm

// Runs an infinite loop that listens to the keyboard input.
// When a key is pressed (any key), the program blackens the screen,
// i.e. writes "black" in every pixel;
// the screen should remain fully black as long as the key is pressed.
// When no key is pressed, the program clears the screen, i.e. writes
// "white" in every pixel;
// the screen should remain fully clear as long as no key is pressed.

// Put your code here.
(WAIT)
@24576
D=M
@BLACK
D;JNE
@WAIT
0;JMP

(BLACK)
@SCREEN
D=A
@0
M=D
//M[0] = SCREEN

(FILLA)
@0
D=M
@24576
D=A-D
@DONE
D;JEQ

@0
A=M
M=-1

@0
M=M+1
@FILLA
0;JMP

(DONE)
@24576
D=M
@CLEAR
D;JEQ
@DONE
0;JMP

(CLEAR)
@SCREEN
D=A
@0
M=D

(FILLB)
@0
D=M
@24576
D=D-A
@WAIT
D;JEQ

@0
A=M
M=0

@0
M=M+1
@FILLB
0;JMP
