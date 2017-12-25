abstract class Instruction {
    companion object {
        fun from(s: String): Instruction {
            if(Regex("^push").containsMatchIn(s)) {
                val (_,source,where) = s.split(" ")
                return Push(Segment.from(source.trim()),where.trim().toInt())
            }

            if(Regex("^pop").containsMatchIn(s)) {
                val (_,source,where) = s.split(" ")
                return Pop(Segment.from(source.trim()),where.trim().toInt())
            }

            if(Regex("^label").containsMatchIn(s)) {
                val (_,name) = s.split(" ")
                return Label(name.trim())
            }

            if(Regex("^goto").containsMatchIn(s)) {
                val (_,label) = s.split(" ")
                return Goto(label.trim())
            }

            if(Regex("^if-goto").containsMatchIn(s)) {
                val (_,label) = s.split(" ")
                return IfGoto(label.trim())
            }

            if(Regex("^function").containsMatchIn(s)) {
                val (_,name,locals) = s.split(" ")
                return Function(name.trim(),locals.trim().toInt())
            }

            if(Regex("^return").containsMatchIn(s)) {
                return Return()
            }

            if(Regex("^call").containsMatchIn(s)) {
                val (_,name,args) = s.split(" ")
                return Call(name.trim(),args.trim().toInt())
            }

            return StackOperation(StackOp.from(s.trim()))
        }
    }
}

enum class StackOp {
    ADD,SUB,NEG,EQ,GT,LT,AND,OR,NOT;
    companion object {
        fun from(s:String): StackOp {
            return when(s) {
                "add" -> ADD
                "sub" -> SUB
                "neg" -> NEG
                "eq" -> EQ
                "gt" -> GT
                "lt" -> LT
                "and" -> AND
                "or" -> OR
                "not" -> NOT
                else -> throw Exception("String '$s' not recognized")
            }
        }
    }
}

data class StackOperation(val type: StackOp) : Instruction()

enum class Segment {
    CONSTANT,LOCAL,ARGUMENT,THIS,THAT,POINTER,TEMP,STATIC;
    companion object {
        fun from(s:String): Segment {
            return when(s) {
                "constant" -> CONSTANT
                "local" -> LOCAL
                "argument" -> ARGUMENT
                "this" -> THIS
                "that" -> THAT
                "pointer" -> POINTER
                "temp" -> TEMP
                "static" -> STATIC
                else -> throw Exception("Segment '$s' not recognized")
            }
        }
    }
}

data class Push(val source:Segment,val where:Int) : Instruction()

data class Pop(val source:Segment,val where:Int) : Instruction()

data class Label(val name:String) : Instruction()

data class Goto(val label:String) : Instruction()

data class IfGoto(val label:String) : Instruction()

data class Function(val name:String,val locals:Int): Instruction()

class Return: Instruction()

data class Call(val name:String,val args:Int): Instruction()