import Token.*

sealed class Token {
    object CLASS: Token()
    object CONSTRUCTOR: Token()
    object FUNCTION: Token()
    object METHOD: Token()
    object FIELD: Token()
    object STATIC: Token()
    object VAR: Token()
    object INT: Token()
    object CHAR: Token()
    object BOOLEAN: Token()
    object VOID: Token()
    object TRUE: Token()
    object FALSE: Token()
    object NULL: Token()
    object THIS: Token()
    object LET: Token()
    object DO: Token()
    object IF: Token()
    object ELSE: Token()
    object WHILE: Token()
    object RETURN: Token()
    object L_CURLY: Token()
    object R_CURLY: Token()
    object L_PAREN: Token()
    object R_PAREN: Token()
    object L_SQUARE: Token()
    object R_SQUARE: Token()
    object DOT: Token()
    object COMMA: Token()
    object SEMICOLON: Token()
    object PLUS: Token()
    object MINUS: Token()
    object MUL: Token()
    object DIV: Token()
    object AND: Token()
    object OR: Token()
    object LT: Token()
    object GT: Token()
    object EQ: Token()
    object NOT: Token()
    data class ConstInt(val value:Int): Token()
    data class ConstStr(val value:String): Token()
    data class Name(val name:String): Token()
}
const val EOF = '\u0000'
class Tokenizer(private val input:String) {
    private var index = 0
    private var startIndex = 0
    fun tokenize(): List<Token> {
        val tokens = mutableListOf<Token>()
        while(!done()) {
            startIndex = index
            parseToken(tokens)
        }
        return tokens
    }

    private fun peek() = if(!done()) input[index] else EOF

    private fun advance():Char {
        index++
        return input[index - 1]
    }

    private fun done() = index == input.length


    private fun munch():String {
        return input.substring(startIndex until index)
    }

    private fun parseToken(tokens:MutableCollection<Token>) {
        val c = advance()
        val tok: Token? = when {
            c.isWhitespace() -> null
            c.isDigit() -> {
                while(peek() in '0'..'9') advance()
                ConstInt(munch().toInt())
            }
            c == '"' -> {
                while(peek() != '"') {
                    if(peek() == '\n') {
                        throw Exception("Unexpected newline in string literal")
                    }
                    if(peek() == EOF) {
                        throw Exception("Unterminated string literal")
                    }
                    advance()
                }
                advance()
                ConstStr(munch().substring(1 until munch().length-1))
            }
            c.isLetter() -> {
                while(peek().isLetterOrDigit()) advance()
                val m = munch()
                val match = mapOf("class" to CLASS,
                        "constructor" to CONSTRUCTOR,
                        "function" to FUNCTION,
                        "method" to METHOD,
                        "field" to FIELD,
                        "static" to STATIC,
                        "var" to VAR,
                        "int" to INT,
                        "char" to CHAR,
                        "boolean" to BOOLEAN,
                        "void" to VOID,
                        "true" to TRUE,
                        "false" to FALSE,
                        "null" to NULL,
                        "this" to THIS,
                        "let" to LET,
                        "do" to DO,
                        "if" to IF,
                        "else" to ELSE,
                        "while" to WHILE,
                        "return" to RETURN)[m]
                if(match != null) match else Token.Name(m)
            }
            else -> {
                if(c == '/' && peek() == '/') {
                    while (peek() != '\n') advance()
                    advance()
                    return
                }
                if(c == '/' && peek() == '*') {
                    while(advance() != '*' || peek() != '/');
                    advance()
                    advance()
                    return
                }
                val match = mapOf('{' to L_CURLY,
                        '}' to R_CURLY,
                        '(' to L_PAREN,
                        ')' to R_PAREN,
                        '[' to L_SQUARE,
                        ']' to  R_SQUARE,
                        '.' to  DOT,
                        ',' to  COMMA,
                        ';' to SEMICOLON,
                        '+' to PLUS,
                        '-' to MINUS,
                        '*' to MUL,
                        '/' to DIV,
                        '&' to AND,
                        '|' to OR,
                        '~' to NOT,
                        '<' to LT,
                        '>' to GT,
                        '=' to EQ)[c] ?: throw Exception("Unexpected char $c")
                match
            }
        }
        if(tok != null) tokens.add(tok)
    }
}