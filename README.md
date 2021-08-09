# Trivial

Trivial is a programming language in French, designed for an Erasmus course project. It compile a `.fr` code into MIPS assembly. No executable is generated.

It is quite simple (hence the name), and currently supports:

- Variable declaration and assignation
- Basic arithmetic (`+`, `-`, `*`, `/`)
- `if/else` and `while` control flow

The compiler has a scanner, two parsers (syntactic and semantic) and two generators (TAC ans MIPS). There is no optimization yet.

Here are the keyword for the Trivial language :

Trivial|Other
:--:|:--:
booleen|boolean
cas|case
decimal|float
defaut|default
definir| ?
entier|int
ET|AND
faire|do
faux|false
finir|end
fonction|function
inclure|include
indefini|undefined
NON|NOT
nul|null
OU|OR
phrase|string
pour|for
retourne|return
selon|switch
tableau|array
tantque|while
vide|void
vrai|true

## TODO

- [ ] Comment the mess
- [ ] Refactor the code to have all parts in `Main.java`
- [ ] Change the constructors' signatures
- [ ] Have the user define the path for the code in the main function
- [ ] Add more features to the language
- [ ] Document the grammar
- [ ] Document the automata
- [ ] Create the MIPS file (create executable ?)
- [ ] Fix the `SyntaxParser` black magic
