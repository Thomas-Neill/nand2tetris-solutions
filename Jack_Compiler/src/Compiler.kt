import java.io.File

class Compiler(private val ast:Class) {
    private val statics:Map<String,Pair<Int,Type>>
    private var arguments:Map<String,Pair<Int,Type>> = mapOf()
    private var locals:Map<String,Pair<Int,Type>> = mapOf()
    private var fields:Map<String,Pair<Int,Type>>? = null
    private var output: String = ""
    private var fresh: Int = 0
    init {
        var ctr = 0
        statics = mapOf(*ast.vars
                .filter {it.decType == ClassVarDec.STATIC}
                .map { static -> static.names.map {ctr++; Pair(it,Pair(ctr-1, static.type))}}
                .flatten()
                .toTypedArray())
    }

    private fun write(s:String) {
        output += s + '\n'
    }

    fun compile() {
        for(sub in ast.subroutines) {
            compileSub(sub)
        }
    }

    private fun compileSub(s:SubDec) {
        var ctr = 0
        locals = mapOf(*s.locals
                .map {local -> local.second.map {ctr++;Pair(it,Pair(ctr-1,local.first))}}
                .flatten()
                .toTypedArray())
        write("function " + ast.name + "." + s.name + " " + locals.size)
        when(s.type) {
            SubDec.FUNCTION -> {
                ctr = 0
                arguments = mapOf(*s.args
                        .map {arg -> ctr++;Pair(arg.second,Pair(ctr-1,arg.first))}
                        .toTypedArray())
                for(statement in s.body) {
                    compileStatement(statement)
                }
            }
            SubDec.CONSTRUCTOR -> {
                ctr = 0
                arguments = mapOf(*s.args
                        .map {arg -> ctr++;Pair(arg.second,Pair(ctr-1,arg.first))}
                        .toTypedArray())
                ctr = 0
                fields = mapOf(*ast.vars
                        .filter { it.decType == ClassVarDec.FIELD }
                        .map {field -> field.names.map {ctr++;Pair(it,Pair(ctr-1,field.type))}}
                        .flatten()
                        .toTypedArray())
                write("push constant " + fields!!.size)
                write("call Memory.alloc 1")
                write("pop pointer 0")
                for(statement in s.body) {
                    compileStatement(statement)
                }
                fields = null
            }
            SubDec.METHOD -> {
                ctr = 1
                arguments = mapOf(*s.args
                        .map {arg -> ctr++;Pair(arg.second,Pair(ctr-1,arg.first))}
                        .toTypedArray())
                ctr = 0
                fields = mapOf(*ast.vars
                        .filter { it.decType == ClassVarDec.FIELD }
                        .map {field -> field.names.map {ctr++;Pair(it,Pair(ctr-1,field.type))}}
                        .flatten()
                        .toTypedArray())
                write("push argument 0")
                write("pop pointer 0")
                for(statement in s.body) {
                    compileStatement(statement)
                }
                fields = null
            }
        }
    }

    private fun compileStatement(s:Statement) {
        write("//$s")
        when(s) {
            is Statement.Let -> {
                compileExpr(s.value)
                if(s.index != null) {
                    if(s.name in statics) {
                        write("push static " + statics[s.name]!!.first)
                    }
                    if(s.name in arguments) {
                        write("push argument " + arguments[s.name]!!.first)
                    }
                    if(s.name in locals) {
                        write("push local " + locals[s.name]!!.first)
                    }
                    if(fields != null && s.name in fields!!) {
                        write("push this " + fields!![s.name]!!.first)
                    }
                    compileExpr(s.index)
                    write("add")
                    write("pop pointer 1")
                    write("pop that 0")
                } else {
                    if(s.name in statics) {
                        write("pop static " + statics[s.name]!!.first)
                    }
                    if(s.name in arguments) {
                        write("pop argument " + arguments[s.name]!!.first)
                    }
                    if(s.name in locals) {
                        write("pop local " + locals[s.name]!!.first)
                    }
                    if(fields != null && s.name in fields!!) {
                        write("pop this " + fields!![s.name]!!.first)
                    }
                }
            }
            is Statement.Do -> {
                compileTerm(Term.Call(s.value))
                write("pop temp 0") //kill it
            }
            is Statement.Return -> {
                if(s.value != null) {
                    compileExpr(s.value)
                } else {
                    write("push constant 0")
                }
                write("return")
            }
            is Statement.If -> {
                val fr = fresh++
                compileExpr(s.cond)
                write("if-goto TRUE$fr")
                if(s.els != null) for(st in s.els) compileStatement(st)
                write("goto DONE$fr")
                write("label TRUE$fr")
                for(st in s.body) compileStatement(st)
                write("label DONE$fr")
            }
            is Statement.While -> {
                val fr = fresh++
                write("label WHILE$fr")
                compileExpr(s.cond)
                write("not")
                write("if-goto DONE$fr")
                for(st in s.body) compileStatement(st)
                write("goto WHILE$fr")
                write("label DONE$fr")
            }
        }
    }

    private fun compileExpr(e:Expr) {
        write("//$e")
        compileTerm(e.first)
        for((op,term) in e.rest) {
            compileTerm(term)
            write(when(op) {
                Expr.ADD -> "add"
                Expr.AND -> "and"
                Expr.DIV -> "call Math.divide 2"
                Expr.EQ -> "eq"
                Expr.GT -> "gt"
                Expr.LT -> "lt"
                Expr.MUL -> "call Math.multiply 2"
                Expr.SUB -> "sub"
                Expr.OR -> "or"
                else -> "ERROR"
            })
        }
    }

    private fun compileTerm(t:Term) {
        write("//$t")
        when(t) {
            is Term.TRUE -> {
                write("push constant 0")
                write("not")
            }
            is Term.FALSE -> write("push constant 0")
            is Term.NULL -> write("push constant 0")
            is Term.THIS -> write("push pointer 0")
            is Term.IntConstant -> write("push constant " + t.value)
            is Term.StrConstant -> {
                write("push constant " + t.value.length)
                write("call String.new 1")
                for(char in t.value) {
                    write("push constant " + char.toInt())
                    write("call String.appendChar 2")
                }
            }
            is Term.Var -> {
                if(t.name in statics) {
                    write("push static " + statics[t.name]!!.first)
                }
                if(t.name in arguments) {
                    write("push argument " + arguments[t.name]!!.first)
                }
                if(t.name in locals) {
                    write("push local " + locals[t.name]!!.first)
                }
                if(fields != null && t.name in fields!!) {
                    write("push this " + fields!![t.name]!!.first)
                }
            }
            is Term.Index -> {
                compileTerm(Term.Var(t.name))
                compileExpr(t.index)
                write("add")
                write("pop pointer 1")
                write("push that 0")
            }
            is Term.Call -> {
                if(t.what.owner == null) {
                    //method
                    write("push pointer 0")
                    for (i in t.what.args) {
                        compileExpr(i)
                    }
                    write("call " + ast.name + "." + t.what.subName + " " + (t.what.args.size + 1))
                } else {
                    var classname:String? = null
                    if(t.what.owner in statics) {
                        classname = (statics[t.what.owner]!!.second as Type.ClassName).name
                        write("push static " + statics[t.what.owner]!!.first)
                    }
                    if(t.what.owner in arguments) {
                        classname = (arguments[t.what.owner]!!.second as Type.ClassName).name
                        write("push argument " + arguments[t.what.owner]!!.first)
                    }
                    if(t.what.owner in locals) {
                        classname = (locals[t.what.owner]!!.second as Type.ClassName).name
                        write("push local " + locals[t.what.owner]!!.first)
                    }
                    if(fields != null && t.what.owner in fields!!) {
                        classname = (fields!![t.what.owner]!!.second as Type.ClassName).name
                        write("push this " + fields!![t.what.owner]!!.first)
                    }
                    for(i in t.what.args) {
                        compileExpr(i)
                    }
                    if(classname == null) {
                        //constructor / function
                        write("call " + t.what.owner + "." + t.what.subName + " " + t.what.args.size)
                    } else {
                        write("call " + classname + "." + t.what.subName + " " + (t.what.args.size + 1))
                    }
                }
            }
            is Term.Unary -> {
                compileTerm(t.term)
                when(t.what) {
                    Term.NEG -> write("neg")
                    Term.NOT -> write("not")
                }
            }
            is Term.Parens -> {
                compileExpr(t.inner)
            }
        }
    }

    fun output(file:String) {
        val f = File(file)
        f.delete()
        f.writeText(output)
    }

}