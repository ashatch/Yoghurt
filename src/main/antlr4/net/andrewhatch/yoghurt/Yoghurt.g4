grammar Yoghurt;

compilationUnit
   : (statement LineEnding)+
   ;

statement
    : portRedirect
    | portBinding
    ;

portBinding
    : PortBindStart IntegerNumber PortBindEnd BindOp functionDefinition
    ;

portRedirect
    : PortBindStart IntegerNumber PortBindEnd BindOp PortBindStart ipAndPort PortBindEnd
    ;

functionDefinition
    : Identifier '(' funcArg ')'
    ;

funcArg
    : variable (',' funcArg)*
    | variable
    ;

variable
    : StringConstant
    | Identifier
    ;

ipAndPort
    : ipAddress ':' portNumber
    ;

ipAddress
    : IntegerNumber '.' IntegerNumber '.' IntegerNumber '.' IntegerNumber
    ;

portNumber
    : IntegerNumber
    ;

StringConstant : '\''  ~[\\'\n]+? '\'' ;
Identifier : [a-z][a-z]*;
IntegerNumber : [0-9]+;
LineEnding : ';';
PortBindStart : '[';
PortBindEnd : ']';
BindOp : '→';
WS
   : ( '\t' | ' ' | '\r' | '\n'| '\u000C' )+ -> skip
   ;