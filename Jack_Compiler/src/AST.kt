data class Class(val name: String, val vars:List<ClassVarDec>, val subroutines:List<SubDec>)

sealed class Type {
    object INT: Type()
    object BOOLEAN: Type()
    object CHAR: Type()
    data class ClassName(val name:String): Type()
}

data class ClassVarDec
    (val decType:ClassVarDecType,
     val type:Type,
     val names:List<String>) {
    abstract class ClassVarDecType
    object STATIC: ClassVarDecType()
    object FIELD: ClassVarDecType()
}
data class SubDec
    (val type: SubType,
     val ret:Type?,
     val name:String,
     val args:List<Pair<Type,String>>,
     val locals:List<Pair<Type,List<String>>>,
     val body:List<Statement>) {
    abstract class SubType
    object CONSTRUCTOR: SubType()
    object FUNCTION: SubType()
    object METHOD: SubType()
}

sealed class Statement {
    data class Let(val name:String,val index:Expr?,val value:Expr): Statement()
    data class If(val cond:Expr,val body:List<Statement>,val els:List<Statement>?): Statement()
    data class While(val cond:Expr,val body:List<Statement>): Statement()
    data class Do(val value:SubCall): Statement()
    data class Return(val value:Expr?): Statement()
}
data class SubCall(val owner:String?,val subName:String,val args:List<Expr>)

data class Expr(val first:Term,val rest: List<Pair<Op,Term>>) {
    abstract class Op
    object ADD:Op()
    object SUB:Op()
    object MUL:Op()
    object DIV:Op()
    object AND:Op()
    object OR:Op()
    object LT:Op()
    object GT:Op()
    object EQ:Op()
}

sealed class Term {
    object TRUE: Term()
    object FALSE: Term()
    object NULL: Term()
    object THIS: Term()
    data class IntConstant(val value:Int): Term()
    data class StrConstant(val value:String): Term()
    data class Var(val name:String): Term()
    data class Index(val name:String,val index: Expr): Term()
    data class Call(val what:SubCall): Term()
    data class Unary(val what:UnaryOperator,val term:Term): Term()
    data class Parens(val inner:Expr):Term()
    abstract class UnaryOperator
    object NEG: UnaryOperator()
    object NOT: UnaryOperator()
}
