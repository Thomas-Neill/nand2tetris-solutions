import java.io.File

fun compiled(filename:String): String {
    val (moduleName) = Regex(""".+/(.+)\.vm""").find(filename)!!.destructured
    val compiler = VMCompiler(moduleName)
    File(filename).useLines {
        for (line in it) {
            val fixed = Regex("//.*").replace(line,"")
            if(fixed != "") {
                compiler.accept(Instruction.from(fixed))
            }
        }
    }
    return compiler.print()
}

fun main(args: Array<String>) {
    val filename = readLine()!!
    if(Regex("""\.vm""").containsMatchIn(filename)) {
        val (moduleName) = Regex(""".+/(.+)\.vm""").find(filename)!!.destructured
        File(Regex("""\.vm""").replace(filename, ".asm")).writeText(VMCompiler.getInit(moduleName) + compiled(filename))
    } else {
        var final = ""
        for(file in File(filename).listFiles()) {
            val absolute = file.toString()
            println(absolute)
            if(Regex(""".+/(.+)\.vm""").containsMatchIn(absolute))
                final += compiled(absolute)
        }
        File(filename + ".asm").writeText(VMCompiler.getInit(filename) + final)
    }
}