grammar CodexLatinus;

// =========================================================
// Gramatica
// =========================================================

programa : seccionVariables? seccionFunciones? seccionPrincipal EOF ; //listo en visitor

// ---------- Secciones ----------
seccionVariables : 'VARIABILES>' declaracion* ; //listo en visitor

seccionFunciones : 'MUNERA>' funcion* ; //listo en visitor

seccionPrincipal : 'MAIOR>' instruccion* FINIS_PROG PUNTOCOMA ; //listo en visitor

// ---------- Declaraciones ----------
declaracion :   declaracionVariable //listo en visitor
    |           declaracionArray //listo en visitor
    |           declaracionEstructuraDef
    |           declaracionEstructuraUso
    ;

declaracionVariable :   ESTO ID DOSPUNTOS tipo expresion? PUNTOCOMA         // esto "un id": littera |'a'|; Puede o no ir el valor
    |                   ESTO ID DOSPUNTOS (VERUM | FALSUS) PUNTOCOMA        // esto "un id": verum / falsus; Exclusivo para booleanos
    ;

declaracionArray :  SERIES ID LCORCHETE expresion RCORCHETE DOSPUNTOS tipo ( LLLAVE listaExpresiones RLLAVE )? PUNTOCOMA
    ;

listaExpresiones :  expresion (COMA expresion)*
    ;

declaracionEstructuraDef :  STRUCTURA ID LLLAVE atributoEstructura ((COMA | PUNTOCOMA) atributoEstructura)* RLLAVE FINIS PUNTOCOMA
    ;

atributoEstructura :    (ESTO | SERIES) ID DOSPUNTOS tipo
    ;

declaracionEstructuraUso :  ESTO ID DOSPUNTOS ID LLLAVE asignacionAtributo (COMA asignacionAtributo)* RLLAVE PUNTOCOMA?
    ;

asignacionAtributo :    ID DOSPUNTOS valorAtributo
    ;

valorAtributo : expresion
    |           LLLAVE asignacionAtributo (COMA asignacionAtributo)* RLLAVE
    ;

// ---------- Tipos ----------
tipo :  NUMERUS //listo en visitor
    |   TEXTUM
    |   DECIMALIS
    |   LITTERA
    |   BOOL
    |   ID
    ;

// ---------- Funciones ----------
funcion :   funcionSinRetorno //listo en visitor
    |       funcionConRetorno //listo en visitor
    ;

funcionSinRetorno : ACTIO ID LPAREN listaParametros? RPAREN LLLAVE bloqueVariables? instruccion* RLLAVE FINIS PUNTOCOMA //listo en visitor
    ;

funcionConRetorno : RATIO tipo ID LPAREN listaParametros? RPAREN LLLAVE bloqueVariables? instruccion* RLLAVE FINIS PUNTOCOMA //listo en visitor
    ;

listaParametros : parametro (COMA parametro)* //listo en visitor
    ;

parametro : ESTO ID DOSPUNTOS tipo //listo en visitor
    ;

bloqueVariables :   'VARIABILES' LCORCHETE declaracion* RCORCHETE
    ;

// ---------- Instrucciones ----------
instruccion :   declaracionVariable //listo en visitor
    |           declaracionArray //listo en visitor
    |           declaracionEstructuraUso
    |           asignacion //listo en visitor
    |           llamadaFuncionStmt
    |           condicional
    |           cicloDum
    |           cicloFacere
    |           cicloPer
    |           actualizacion
    |           instruccionRetorno //listo en visitor
    |           instruccionLectura
    |           instruccionEscritura
    |           PERGE PUNTOCOMA
    |           INTERRUMPE PUNTOCOMA
    ;

asignacion :    objetivoAsignacion IGUAL expresion PUNTOCOMA
    |           objetivoAsignacion IGUAL LLLAVE asignacionAtributo (COMA asignacionAtributo)* RLLAVE PUNTOCOMA
    ;

objetivoAsignacion : ID (LCORCHETE expresion RCORCHETE)? segmentoAcceso* ;

segmentoAcceso : PUNTO ID (LCORCHETE expresion RCORCHETE)?;

llamadaFuncionStmt :    llamadaFuncion PUNTOCOMA
    ;

llamadaFuncion :    ID LPAREN listaExpresiones? RPAREN
    ;

instruccionRetorno :    REDDERE expresion PUNTOCOMA
    ;

instruccionLectura :    ID LECTURA PUNTOCOMA?
    |                   LECTURA ID? PUNTOCOMA?
    ;

instruccionEscritura :  ESCRITURA expresion (ESCRITURA expresion)* PUNTOCOMA
    ;

// ---------- Condicionales ----------
condicional :   ramaSi ramaAliter* ramaElse? FINIS PUNTOCOMA
    ;

ramaSi :    SI LPAREN expresion RPAREN LLLAVE instruccion* RLLAVE
    ;

ramaAliter :    ALITER LPAREN expresion RPAREN LLLAVE instruccion* RLLAVE
    ;

ramaElse :  ALITER LLLAVE instruccion* RLLAVE
    ;

// ---------- Ciclos ----------
cicloDum :  DUM LPAREN expresion RPAREN LLLAVE instruccion* RLLAVE FINIS PUNTOCOMA
    ;

cicloFacere :   FACERE LLLAVE instruccion* RLLAVE DUM LPAREN expresion RPAREN PUNTOCOMA
    ;

cicloPer :  PER LPAREN cicloPerInit PUNTOCOMA expresion PUNTOCOMA cicloPerActualizacion RPAREN LLLAVE instruccion* RLLAVE
    ;

cicloPerInit :  declaracionVariableSinPuntoComa
    |           asignacionSinPuntoComa
    ;

declaracionVariableSinPuntoComa :   ESTO ID DOSPUNTOS tipo expresion?
    |                               ESTO ID DOSPUNTOS (VERUM | FALSUS)
    ;

asignacionSinPuntoComa :    objetivoAsignacion IGUAL expresion
    ;

cicloPerActualizacion : ID (INCREMENTO | DECREMENTO)
    |                   asignacionSinPuntoComa
    ;

actualizacion : ID (INCREMENTO | DECREMENTO) PUNTOCOMA
    ;
// ---------- Expresiones ----------
expresion : expresionOr ;

expresionOr :   expresionAnd (OR expresionAnd)* ; //listo en visitor

expresionAnd :  expresionIgualdad (AND expresionIgualdad)* ; //listo en visitor

expresionIgualdad : expresionRelacional ((IGUALA | DIFERENTE) expresionRelacional)* ; //listo en visitor

expresionRelacional :   expresionAditiva ((MENOR | MAYOR | MENORIGUAL | MAYORIGUAL) expresionAditiva)* ;

expresionAditiva :  expresionMultiplicativa ((MAS | MENOS) expresionMultiplicativa)* ;

expresionMultiplicativa :   expresionUnaria ((POR | DIV) expresionUnaria)* ;

expresionUnaria :   NON expresionUnaria
    |               MENOS expresionUnaria
    |               expresionPostfija
    ;

expresionPostfija : expresionPrimaria (INCREMENTO | DECREMENTO)? ;

expresionPrimaria : exp_nativa //listo en visitor
    |               llamadaFuncion
    |               accesoVariable //listo en visitor
    |               LPAREN expresion RPAREN //listo en visitor
    ;

accesoVariable : ID segmentoAcceso* (LCORCHETE expresion RCORCHETE)?
;

exp_nativa :    NUMERO
    |           DECIMAL
    |           CADENA
    |           CARACTER
    |           VERUM
    |           FALSUS
    ;

// =========================================================
// Lexer
// =========================================================

// --  Palabras Reservadas  --
ESTO        : 'esto';
SERIES      : 'series';
STRUCTURA   : 'structura';
FINIS       : 'finis';    // cierre de bloque: si/finis; dum/finis; 
FINIS_PROG  : 'FINIS';    // cierre de la sección MAIOR> 
SI          : 'si';
ALITER      : 'aliter';
DUM         : 'dum';
FACERE      : 'facere';
PER         : 'per';
PERGE       : 'perge';
INTERRUMPE  : 'interrumpe';
RATIO       : 'ratio';
ACTIO       : 'actio';
REDDERE     : 'reddere';
NON         : 'non';

VERUM       : 'verum';
FALSUS      : 'falsus';
NUMERUS     : 'numerus';
TEXTUM      : 'textum';
DECIMALIS   : 'decimalis';
LITTERA     : 'littera';
BOOL        : 'bool';

// --  Operadores de Suma Abreviada  --
INCREMENTO  : '++';
DECREMENTO  : '--';

// --  Operadores Aritmeticos  --
MAS         : '+';
MENOS       : '-';
POR         : '*';
DIV         : '/';

// --  Operadores Relacionales  --
IGUALA      : '==';
DIFERENTE   : '!=';
MENORIGUAL  : '<=';
MAYORIGUAL  : '>=';
MENOR       : '<';
MAYOR       : '>';

// --  Operadores Lógicos  --
AND         : '&&';
OR          : '||';

// --  Símbolos  --
IGUAL       : '=';
DOSPUNTOS   : ':';
PUNTOCOMA   : ';';
COMA        : ',';
PUNTO       : '.';
LPAREN      : '(';
RPAREN      : ')';
LLLAVE      : '{';
RLLAVE      : '}';
LCORCHETE   : '[';
RCORCHETE   : ']';
LECTURA     : '<<';
ESCRITURA   : '>>';

// --  Datos Primitivos  --
NUMERO      : [0-9]+;
DECIMAL     : [0-9]+ '.' [0-9]+;
CADENA      : '"' (~["\r\n])* '"';
CARACTER    : '\'' . '\'';

// --  Identificador  --
ID          : [a-zA-Z_][a-zA-Z0-9_]*;

// --  Espacios en blanco y comentarios --
WS                  : [ \t\r\n]+ -> channel(HIDDEN);
COMENTARIO_LINEA    : '//' ~[\r\n]* -> channel(HIDDEN);
COMENTARIO_BLOQUE   : '##' .*? '##' -> channel(HIDDEN);