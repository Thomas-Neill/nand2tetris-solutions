import Token.*

class Parser(private val tokens:List<Token>) {
    private var index = 0
    private var last: Token? = null
    private fun done() = index == tokens.size
    private fun peek() = if(done()) null else tokens[index]
    private fun advance(): Token {
        index++
        last = tokens[index-1]
        return tokens[index-1]
    }

    private fun expect(t: Token) {
        if(advance() != t)
            throw Exception("Expected token $t but got token $last!")
    }

    private fun expectName():String {
        val adv = advance()
        if(adv is Name)
            return adv.name
        throw Exception("Expected name but got $last")
    }

    private fun accept(vararg tokens: Token): Boolean {
        if(tokens.contains(peek())) {
            advance()
            return true
        }
        return false
    }

    fun parse(): Class {
        return parseClass()
    }

    private fun parseClass(): Class {
        expect(CLASS)
        val name = expectName()
        expect(L_CURLY)
        val decls = parseClassVarDecls()
        val subroutines = parseClassSubDecls()
        expect(R_CURLY)
        return Class(name,decls,subroutines)
    }

    private fun parseClassVarDecls(): List<ClassVarDec> {
        val decls = mutableListOf<ClassVarDec>()
        while(accept(STATIC,FIELD)) {
            val declType = when(last!!) {
                STATIC -> ClassVarDec.STATIC
                FIELD -> ClassVarDec.FIELD
                else -> throw Exception("should not happen")
            }
            val type = parseType()
            val names = mutableListOf<String>()
            names.add(expectName())
            while(accept(COMMA)) {
                names.add(expectName())
            }
            expect(SEMICOLON)
            decls.add(ClassVarDec(declType,type,names))
        }
        return decls
    }

    private fun parseClassSubDecls():List<SubDec> {
        val subroutines = mutableListOf<SubDec>()
        while(accept(CONSTRUCTOR,METHOD,FUNCTION)) {
            val subType = when(last!!) {
                CONSTRUCTOR -> SubDec.CONSTRUCTOR
                METHOD -> SubDec.METHOD
                FUNCTION -> SubDec.FUNCTION
                else -> throw Exception("should not happen")
            }
            var type:Type? = null
            if(!accept(VOID)) {
                type = parseType()
            }
            val subName = expectName()
            expect(L_PAREN)
            val args = mutableListOf<Pair<Type,String>>()
            if(peek() != R_PAREN) {
                args.add(Pair(parseType(),expectName()))
            }
            while(accept(COMMA)) {
                args.add(Pair(parseType(),expectName()))
            }
            expect(R_PAREN)
            expect(L_CURLY)
            val vars = mutableListOf<Pair<Type,List<String>>>()
            while(accept(VAR)) {
                val typ = parseType()
                val names = mutableListOf<String>()
                names.add(expectName())
                while(accept(COMMA)) {
                    names.add(expectName())
                }
                expect(SEMICOLON)
                vars.add(Pair(typ,names))
            }
            val statements = parseStatements()
            expect(R_CURLY)
            subroutines.add(SubDec(subType,type,subName,args,vars,statements))
        }
        return subroutines
    }

    private fun parseStatements():List<Statement> {
        val statements = mutableListOf<Statement>()
        while(peek() != R_CURLY) {
            statements.add(parseStatement())
        }
        return statements
    }

    private fun parseStatement():Statement {
        if(accept(LET)) {
            val name = expectName()
            var inside:Expr? = null
            if(accept(L_SQUARE)) {
                inside = parseExpr()
                expect(R_SQUARE)
            }
            expect(EQ)
            val expr = parseExpr()
            expect(SEMICOLON)
            return Statement.Let(name,inside,expr)
        }
        if(accept(IF)) {
            expect(L_PAREN)
            val cond = parseExpr()
            expect(R_PAREN)
            expect(L_CURLY)
            val body = parseStatements()
            expect(R_CURLY)
            var els:List<Statement>? = null
            if(accept(ELSE)) {
                expect(L_CURLY)
                els = parseStatements()
                expect(R_CURLY)
            }
            return Statement.If(cond,body,els)
        }
        if(accept(WHILE)) {
            expect(L_PAREN)
            val cond = parseExpr()
            expect(R_PAREN)
            expect(L_CURLY)
            val body = parseStatements()
            expect(R_CURLY)
            return Statement.While(cond,body)
        }
        if(accept(DO)) {
            val name = expectName()
            var subName:String? = null
            if(accept(DOT)) {
                subName = expectName()
            }
            expect(L_PAREN)
            val args = mutableListOf<Expr>()
            if(peek() != R_PAREN) {
                args.add(parseExpr())
            }
            while(accept(COMMA)) {
                args.add(parseExpr())
            }
            expect(R_PAREN)
            expect(SEMICOLON)
            if(subName != null)
                return Statement.Do(SubCall(name,subName,args))
            return Statement.Do(SubCall(null,name,args))
        }
        if(accept(RETURN)) {
            if(accept(SEMICOLON)) {
                return Statement.Return(null)
            }
            val ret = parseExpr()
            expect(SEMICOLON)
            return Statement.Return(ret)
        }
        throw Exception("Expected start of statement but got" + peek())
    }

    private fun parseExpr():Expr {
        val first = parseTerm()
        val rest = mutableListOf<Pair<Expr.Op,Term>>()
        while(accept(PLUS,MINUS,MUL,DIV,AND,OR,GT,LT,EQ)) {
            val op = when(last!!) {
                PLUS -> Expr.ADD
                MINUS -> Expr.SUB
                MUL -> Expr.MUL
                DIV -> Expr.DIV
                AND -> Expr.AND
                OR -> Expr.OR
                GT -> Expr.GT
                LT -> Expr.LT
                EQ -> Expr.EQ
                else -> throw Exception("will not happen")
            }
            val term = parseTerm()
            rest.add(Pair(op,term))
        }
        return Expr(first,rest)
    }

    private fun parseTerm():Term {
        if(peek() is ConstInt) {
            return Term.IntConstant((advance() as ConstInt).value)
        }
        if(peek() is ConstStr) {
            return Term.StrConstant((advance() as ConstStr).value)
        }
        if(accept(TRUE)) {
            return Term.TRUE
        }
        if(accept(FALSE)) {
            return Term.FALSE
        }
        if(accept(THIS)) {
            return Term.THIS
        }
        if(accept(NULL)) {
            return Term.NULL
        }
        if(accept(L_PAREN)) {
            val ret = parseExpr()
            expect(R_PAREN)
            return Term.Parens(ret)
        }
        if(accept(NOT,MINUS)) {
            val op = when(last!!) {
                NOT -> Term.NOT
                MINUS -> Term.NEG
                else -> throw Exception("Should not occur")
            }
            val rest = parseTerm()
            return Term.Unary(op,rest)
        }
        val name = expectName()
        if(accept(L_SQUARE)) {
            val ind = parseExpr()
            expect(R_SQUARE)
            return Term.Index(name,ind)
        }
        var subName:String? = null
        if(accept(DOT)) {
            subName = expectName()
        }
        if(accept(L_PAREN)) {
            val args = mutableListOf<Expr>()
            if(peek() != R_PAREN) {
                args.add(parseExpr())
            }
            while(accept(COMMA)) {
                args.add(parseExpr())
            }
            expect(R_PAREN)
            if(subName == null)
                return Term.Call(SubCall(null,name,args))
            return Term.Call(SubCall(name,subName,args))
        }
        return Term.Var(name)
    }

    private fun parseType(): Type {
        val adv = advance()
        return when(adv) {
            INT -> Type.INT
            BOOLEAN -> Type.BOOLEAN
            CHAR -> Type.CHAR
            is Name -> Type.ClassName(adv.name)
            else -> throw Exception("$adv does not represent a type!")
        }
    }

}