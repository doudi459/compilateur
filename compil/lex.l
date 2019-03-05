%{
#include <stdio.h>
#include <string.h>
#include "bis.tab.h"
#include "bib.h"

extern YYSTYPE yylval;

%}
idf [a-zA-Z][a-zA-Z0-9_]*
cst [1-9][0-9]*|0
comnt \/\*(.|[\n])*\*\/
comntl \/\/.*
%%
si { nbcl++; inserer(yytext,"motCle","/",0);return mc_si;};
pour { nbcl++; inserer(yytext,"motCle","/",0);return mc_pour;};
jusque { nbcl++; inserer(yytext,"motCle","/",0);return mc_jusque;};
faire { nbcl++; inserer(yytext,"motCle","/",0);return mc_faire;};
fait { nbcl++; inserer(yytext,"motCle","/",0);return mc_fait;};
entier { nbcl++; inserer(yytext,"motCle","/",0);return mc_entier;};
reel { nbcl++; inserer(yytext,"motCle","/",0);return mc_reel;};
chaine {nbcl++; inserer(yytext,"motCle","/",0);return mc_chaine;};
[:;]    return yytext[0]; 
"<--"   return aff; 
DEBUT { nbcl++; inserer(yytext,"motCle","/",0); yylval.str=strdup(yytext); return mc_debut;};
VAR { nbcl++; inserer(yytext,"motCle","/",0); yylval.str=strdup(yytext); return mc_var;};
FIN { nbcl++; inserer(yytext,"motCle","/",0); yylval.str=strdup(yytext); return mc_fin;};
ALGORITHME { nbcl++; inserer(yytext,"motCle","/",0);return mc_algo;};
{idf} {nbcl++; inserer(yytext,"idf","/",1);if(yyleng > 12) printf("ATTENTION !!! la longeur d'un identificateur ne doit pas depasser 12 char "); else {yylval.str=strdup(yytext); return id;}};
{cst} {nbcl++; inserer(yytext,"cst","entier",1);yylval.str=strdup(yytext); return cst;};
"\"".*"\"" { nbcl++; yylval.str=strdup(yytext); return ch;};
{comnt} {sautLigne(yytext,yyleng);}
{comntl} {nbcl=0;}
"<="      return opc_infEgale; 
">="      return opc_supEgale;
!=        return opc_diff; 
[()<>]    return yytext[0]; 
[*=\-+/]  return yytext[0]; 
[\]\[\|]  return yytext[0]; 
[ \t]
\n {nbligne++;nbcl=0;}
. printf("Erreur lexicale a la ligne %d,%d  ",nbligne,nbcl);
%%
int yywrap(void) {
return 0;
}
