import java.io.File

fun main(args:Array<String>) {
    val dirname = readLine()!!
    for(filename in File(dirname).list()) {
        if(Regex(""".+\.vm""").matches(filename)) continue
        println(filename)
        val text = File(dirname + '/' + filename).readText()
        val tokenizer = Tokenizer(text)
        val parser = Parser(tokenizer.tokenize())
        val compiler = Compiler(parser.parse())
        compiler.compile()
        compiler.output(Regex("""\.jack""").replace(dirname + '/' + filename, ".vm"))
    }
}