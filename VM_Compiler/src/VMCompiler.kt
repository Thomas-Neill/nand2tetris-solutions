class VMCompiler(private val filename:String) {
    companion object {
        fun getInit(filename:String):String {
            var init = ""
            init += """
                @256
                D=A
                @SP
                M=D
            """.trimIndent()
            val tempCompiler = VMCompiler(filename)
            tempCompiler.accept(Call("Sys.init",0))
            init += tempCompiler.print()
            return init
        }
    }

    private val output: MutableList<String> = mutableListOf()
    private var currentFn: String? = null
    private var freshCounter = 0
    private fun push(a:String) {
        output.add(a)
    }
    fun print(): String {
        var ret = ""
        for(i in output) {
            ret += i + "\n"
        }
        return ret
    }

    fun accept(a: Instruction) {
        push("//" + a.toString())
        when(a) {
            is StackOperation -> {
                fun popStackToD() {
                    push("@SP")
                    push("M=M-1")
                    push("A=M")
                    push("D=M")
                    push("M=0")
                }
                when (a.type) {
                    StackOp.ADD -> {
                        popStackToD()
                        push("A=A-1")
                        push("M=D+M")
                    }
                    StackOp.SUB -> {
                        popStackToD()
                        push("A=A-1")
                        push("M=M-D")
                    }
                    StackOp.NEG -> {
                        push("@SP")
                        push("A=M-1")
                        push("M=-M")
                    }
                    StackOp.EQ -> {
                        popStackToD()
                        push("@R13")
                        push("M=D")
                        popStackToD()
                        push("@R13")
                        push("D=D-M")
                        push("M=0")

                        val fresh = freshCounter++

                        push("@EQ" + fresh)
                        push("D;JEQ")
                        push("@DONE$fresh")
                        push("0;JMP")

                        push("(EQ$fresh)")
                        push("@R13")
                        push("M=-1")

                        push("(DONE$fresh)")
                        push("@R13")
                        push("D=M")
                        push("@SP")
                        push("M=M+1")
                        push("A=M-1")
                        push("M=D")
                    }
                    StackOp.GT -> {
                        popStackToD()
                        push("@R13")
                        push("M=D")
                        popStackToD()
                        push("@R13")
                        push("D=D-M")
                        push("M=0")

                        val fresh = freshCounter++

                        push("@GT" + fresh)
                        push("D;JGT")
                        push("@DONE$fresh")
                        push("0;JMP")

                        push("(GT$fresh)")
                        push("@R13")
                        push("M=-1")

                        push("(DONE$fresh)")
                        push("@R13")
                        push("D=M")
                        push("@SP")
                        push("M=M+1")
                        push("A=M-1")
                        push("M=D")
                    }
                    StackOp.LT -> {
                        popStackToD()
                        push("@R13")
                        push("M=D")
                        popStackToD()
                        push("@R13")
                        push("D=D-M")
                        push("M=0")

                        val fresh = freshCounter++

                        push("@LT$fresh")
                        push("D;JLT")
                        push("@DONE$fresh")
                        push("0;JMP")

                        push("(LT$fresh)")
                        push("@R13")
                        push("M=-1")

                        push("(DONE$fresh)")
                        push("@R13")
                        push("D=M")
                        push("@SP")
                        push("M=M+1")
                        push("A=M-1")
                        push("M=D")
                    }
                    StackOp.AND -> {
                        popStackToD()
                        push("A=A-1")
                        push("M=M&D")
                    }
                    StackOp.OR -> {
                        popStackToD()
                        push("A=A-1")
                        push("M=M|D")
                    }
                    StackOp.NOT -> {
                        push("@SP")
                        push("A=M-1")
                        push("M=!M")
                    }
                }
            }
            is Push -> {
                val (source,where) = a
                fun pushFrom(src:String) {
                    push("@$where")
                    push("D=A")
                    push("@$src")
                    push("A=D+M")
                    push("D=M")
                    push("@SP")
                    push("M=M+1")
                    push("A=M-1")
                    push("M=D")
                }

                fun offPush(offset:Int) {
                    push("@" + (where + offset))
                    push("D=M")
                    push("@SP")
                    push("M=M+1")
                    push("A=M-1")
                    push("M=D")
                }

                when(source) {
                    Segment.CONSTANT -> {
                        push("@$where")
                        push("D=A")
                        push("@SP")
                        push("M=M+1")
                        push("A=M-1")
                        push("M=D")
                    }

                    Segment.LOCAL -> pushFrom("LCL")

                    Segment.ARGUMENT -> pushFrom("ARG")

                    Segment.THIS -> pushFrom("THIS")

                    Segment.THAT -> pushFrom("THAT")

                    Segment.POINTER  -> offPush(3)

                    Segment.TEMP -> offPush(5)

                    Segment.STATIC -> {
                        push("@$filename.$where")
                        push("D=M")
                        push("@SP")
                        push("M=M+1")
                        push("A=M-1")
                        push("M=D")
                    }
                }
            }
            is Pop -> {
                val (source, where) = a
                fun popTo(src:String) {
                    push("@SP")
                    push("M=M-1")
                    push("A=M")
                    push("D=M")
                    push("M=0")
                    push("@$src")
                    push("A=M")
                    for(i in 1..where) {
                        push("A=A+1")
                    }
                    push("M=D")
                }
                fun offPop(offset:Int) {
                    push("@SP")
                    push("M=M-1")
                    push("A=M")
                    push("D=M")
                    push("M=0")
                    push("@" + (where + offset))
                    push("M=D")
                }

                when(source) {
                    Segment.CONSTANT -> throw Exception("Can't pop to constant")

                    Segment.LOCAL -> popTo("LCL")

                    Segment.ARGUMENT -> popTo("ARG")

                    Segment.THIS -> popTo("THIS")

                    Segment.THAT -> popTo("THAT")

                    Segment.POINTER -> offPop(3)

                    Segment.TEMP -> offPop(5)

                    Segment.STATIC -> {
                        push("@SP")
                        push("M=M-1")
                        push("A=M")
                        push("D=M")
                        push("M=0")
                        push("@$filename.$where")
                        push("M=D")
                    }
                }
            }
            is Label -> {
                if(currentFn == null) {
                    push("(" + a.name + ")")
                } else {
                    push("(" + currentFn + ":" + a.name + ")")
                }
            }
            is Goto -> {
                if(currentFn == null) {
                    push("@" + a.label)
                } else {
                    push("@" + currentFn + ":" + a.label)
                }
                push("0;JMP")
            }
            is IfGoto -> {
                push("@SP")
                push("M=M-1")
                push("A=M")
                push("D=M")
                push("M=0")
                if(currentFn == null) {
                    push("@" + a.label)
                } else {
                    push("@" + currentFn + ":" + a.label)
                }
                push("D;JNE")
            }
            is Function -> {
                val (name,locals) = a
                push("($name)")
                for(unused in 1..locals) {
                    push("@SP")
                    push("M=M+1")
                    push("A=M-1")
                    push("M=0")
                }
                currentFn = name
            }
            is Call -> {
                fun pushReg(register:String) {
                    push("@$register")
                    push("D=M")
                    push("@SP")
                    push("M=M+1")
                    push("A=M-1")
                    push("M=D")
                }

                val (name, args) = a
                val fresh = freshCounter++
                push("@RETURN$fresh")
                push("D=A")
                push("@SP")
                push("M=M+1")
                push("A=M-1")
                push("M=D")
                pushReg("LCL")
                pushReg("ARG")
                pushReg("THIS")
                pushReg("THAT")
                push("@" + (args + 5))
                push("D=A")
                push("@SP")
                push("D=M-D")
                push("@ARG")
                push("M=D")
                push("@SP")
                push("D=M")
                push("@LCL")
                push("M=D")
                push("@$name")
                push("0;JMP")

                push("(RETURN$fresh)")
            }
            is Return -> {
                push("@5")
                push("D=A")
                push("@LCL")
                push("A=M-D")
                push("D=M")
                push("@R13")
                push("M=D") //cache return address

                push("@SP")
                push("A=M-1")
                push("D=M")
                push("@ARG")
                push("A=M")
                push("M=D") //push return value to stack

                push("@ARG")
                push("D=M")
                push("@SP")
                push("M=D+1") //retrieve SP

                push("@LCL")
                push("A=M-1")
                push("D=M")
                push("@THAT")
                push("M=D") //retrieve THAT

                push("@LCL")
                push("A=M-1")
                push("A=A-1")
                push("D=M")
                push("@THIS")
                push("M=D") //retrieve THIS

                push("@LCL")
                push("A=M-1")
                push("A=A-1")
                push("A=A-1")
                push("D=M")
                push("@ARG")
                push("M=D") //retrieve ARG

                push("@LCL")
                push("A=M-1")
                push("A=A-1")
                push("A=A-1")
                push("A=A-1")
                push("D=M")
                push("@LCL")
                push("M=D") //retrieve LCL

                push("@R13")
                push("A=M")
                push("0;JMP") //leave
            }
        }
    }
}