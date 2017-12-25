import re,sys
filename = sys.argv[1]
with open(filename) as f:
    lines = f.read().split('\n')

symtab = \
    {
        "SP":0,
        "LCL":1,
        "ARG":2,
        "THIS":3,
        "THAT":4,
        "SCREEN":16384,
        "KBD":24576,
    }
for i in range(16):
    symtab[f"R{i}"] = i
new_location=16
instrs = 0

for line in lines:
    line = line.strip()
    line = re.sub("//.+","",line)
    if(line == ''):
        continue
    if(line[0] == '('):
        symtab[line[1:-1]] = instrs
        continue
    instrs += 1

for line in lines:
    line = line.strip()
    line = re.sub("//.+","",line)
    if(line == '' or line[0] == '('):
        continue
    if(line[0] == '@'):
        val = None
        try:
            val = int(line[1:])
        except:
            try:
                val = symtab[line[1:]]
            except:
                val = new_location
                symtab[line[1:]] = new_location
                new_location += 1
        print("0{0:0>15b}".format(val))
    else:
        compdict= \
            {
                "0":"0101010",
                "1":"0111111",
                "-1":"0111010",
                "D":"0001100",
                "A":"0110000",
                "!D":"0001101",
                "!A":"0110001",
                "-D":"0001111",
                "-A":"0110011",
                "D+1":"0011111",
                "A+1":"0110111",
                "D-1":"0001110",
                "A-1":"0110010",
                "D+A":"0000010",
                "D-A":"0010011",
                "A-D":"0000111",
                "D&A":"0000000",
                "D|A":"0010101"
            }
        additions = {}
        for key,val in compdict.items():
            if("A" in key):
                additions[key.replace("A","M")] = "1" + val[1:]
        compdict.update(additions)
        destdict = \
            {
                "null":"000",
                "M":"001",
                "D":"010",
                "MD":"011",
                "A":"100",
                "AM":"101",
                "AD":"110",
                "AMD":"111"
            }
        jmpdict = \
            {
                "null":"000",
                "JGT":"001",
                "JEQ":"010",
                "JGE":"011",
                "JLT":"100",
                "JNE":"101",
                "JLE":"110",
                "JMP":"111"
            }
        dest,comp,jmp = "null",None,"null"
        if("=" not in line and ";" not in line):
            comp = line
        if("=" not in line and ";" in line):
            comp,jmp = line.split(";")
        if("=" in line and ";" not in line):
            dest,comp = line.split("=")
        if("=" in line and ";" in line):
            dest,rest = line.split("=")
            comp,jmp = line.split(";")
        print("111" + compdict[comp] + destdict[dest] + jmpdict[jmp])
