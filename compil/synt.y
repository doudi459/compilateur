
%{
#include "bib.h"
#include<stdio.h>
#include <string.h>
#include<stdlib.h>

int temp=0;
int yyerror(char *s);
int yylex();
char T[30][15];
int q=0;
char t[255]= { } ;
int i=0;
char f[10];
Pile *p = NULL;
int iterator=0;
int rs=0;

%}

%union {
  float num;
  char* str;
  struct s{char* val;int type2 ;}s;
}

%token mc_algo  mc_var mc_debut mc_fin  aff opc_diff opc_supEgale opc_inf opc_infEgale
mc_pour mc_jusque mc_faire mc_fait mc_si mc_reel mc_chaine mc_entier
%token<str> ch id cst
%type<s> OPERANDE 
%type<str> TYPE
%left opc_supEgale opc_diff aff opc_inf opc_infEgale
%left '+' '-'
%left '/' '*'



%%
projet: mc_algo id mc_var DECLARATION mc_debut CODE mc_fin {printf("pgm syntaxiquement correct"); return 0;}
;
DECLARATION: DECLARATION  LISTEID ':' TYPE  ';' {
	while(i < q) 
	{
		miseajourtype(T[i],$4);
	     i++;
	}
	

	
}| LISTEID ':' TYPE ';' 
;
LISTEID: id   '|' LISTEID  {if(varDeclaree($1,T,q) == -1){strcpy(T[q],$1);q++;}}
    | id '[' cst ']' '|' LISTEID {if(varDeclaree($1,T,q) == -1){strcpy(T[q],$1);q++;}miseajourtaille($1,atoi($3));}
    | id {if(varDeclaree($1,T,q) == -1){strcpy(T[q],$1);q++;}}
    | id '[' cst ']'  {if(varDeclaree($1,T,q) == -1){strcpy(T[q],$1);q++;}}
;
TYPE: mc_reel{$$ = strdup("reel");}|mc_entier {$$ = strdup("entier");}|mc_chaine {$$ = strdup("chaine");}
;
CODE: INST|INST CODE
;
INST: AFFECTATION|BOUCLE|FAIRESI
;
AFFECTATION: id aff OPERANDE ';'{
	 varNonDeclaree($1,T,q);
	
	     
		 typeComp( typeEntite($1),$3.type2 ); 
		 if (taille($1) > 1)
		 printf("Erreur a la ligne %d,%d ( %s ) est un tableau\n",nbligne,nbcl,$1);
		 
    
		 insererquad("<--",$3.val," ",$1,iterator);
		 iterator++; 
		
		                      }
		
           | id '[' cst ']' aff OPERANDE ';' 
           { 
           	if(varNonDeclaree($1,T,q)!=-1)
           		{ 
           			if(taille($1)> atoi($3)){typeComp(typeEntite($1),$6.type2);  
           		}else 
           		printf("Erreur a la ligne %d,%d depassement de taille du tableau\n",nbligne,nbcl); 
            	} 

            	//insererquad("<--",$6.val," ",$1);
		
           }
           | id aff ch ';' 
           {
           	if(varNonDeclaree($1,T,q)!=-1)
           	{
           		typeComp(typeEntite($1),1);
           	
           	if (taille($1) > 1)
		     printf("Erreur a la ligne %d,%d ( %s ) est un tableau\n",nbligne,nbcl,$1);
}
           	insererquad("<--",$3," ",$1,iterator);
           	iterator++;
		
           }
;
         OPERANDE: 
       cst
        {
        	 $$.val=strdup($1);$$.type2 = typeEntite($1);
	     }
        | id  
        {
        	varNonDeclaree($1,T,q);
		 $$.val=strdup($1); $$.type2 = typeEntite($1);
	}
	|id '[' cst ']'
		{ 

		varNonDeclaree($1,T,q);
		if(taille($1)>1)
			{
				if(taille($1)<= atoi($3)) 
					printf("Erreur a la ligne %d,%d depassement de la taille du tableau\n",nbligne,nbcl);
			}else
			{
				printf("Erreur a la ligne %d,%d ( %s ) n'est pas un tableau\n",nbligne,nbcl,$1);
			} 
			$$.val=strdup($1); $$.type2 = typeEntite($1);
		}  

        |OPERANDE '+' OPERANDE 
        {
        	strcpy(f,"t");
        typeComp($1.type2,$3.type2);
        sprintf(t,"%d",temp);
        strcat(f,t);
        temp++;
        insererquad("+",$1.val,$3.val,f,iterator);
        iterator++;
	    
        $$.val=strdup(f);
        }
     
        |OPERANDE '-' OPERANDE
        {
        	strcpy(f,"t");
        typeComp($1.type2,$3.type2); 
        sprintf(t,"%d",temp);
        strcat(f,t);
        temp++;
         insererquad("-",$1.val,$3.val,f,iterator);
        iterator++;
	    
        $$.val=strdup(f);
        }
       
        |OPERANDE '/' OPERANDE
        {
        	strcpy(f,"t");
        	if (strcmp($3.val,"0") == 0)
        	{
        		printf("Erreur a la ligne %d,%d devision par 0 impossible\n",nbligne,nbcl);
        	}
        typeComp($1.type2,$3.type2);
        sprintf(t,"%d",temp);
        strcat(f,t);
        temp++;
         insererquad("/",$1.val,$3.val,f,iterator);
        iterator++;
        $$.val=strdup(f);
        }
      
        |OPERANDE '*' OPERANDE
        {
        	strcpy(f,"t");
        typeComp($1.type2,$3.type2);
        sprintf(t,"%d",temp);
        strcat(f,t);
        temp++;
         insererquad("*",$1.val,$3.val,f,iterator);
        iterator++;
        $$.val=strdup(f);
        }
       
;
BOUCLE: mc_pour id {varNonDeclaree($2,T,q);typeComp(typeEntite($2),0);} aff cst mc_jusque cst mc_faire CODE mc_fait
;
FAIRESI: T mc_si '(' COND ')'  {
	
	sprintf(t,"%d",rs+1);
	insererquad("BMZ",t,"inst"," ",iterator);
    iterator++;
    rs=depiler(&p);
	routin(rs,iterator);

};
T:I CODE{insererquad("BR"," "," "," ",iterator);rs=depiler(&p);routin(rs,iterator+1);empiler(&p,iterator);iterator++;};
I:mc_faire {insererquad("BR"," "," "," ",iterator);empiler(&p,iterator);iterator++;};

COND: COMP|DIFF|SUPEGALE|
INFEGALE
;
COMP : OPERANDE '=' OPERANDE {if(varNonDeclaree($1.val,T,q)!=-1 && varNonDeclaree($3.val,T,q)!=-1){      	
	strcpy(f,"t");
        typeComp($1.type2,$3.type2); 
        sprintf(t,"%d",temp);
        strcat(f,t);
        temp++;
        insererquad("-",$1.val,$3.val,f,iterator);
        iterator++; 
    }} 
	 | OPERANDE '>' OPERANDE {if(varNonDeclaree($1.val,T,q)!=-1 && varNonDeclaree($3.val,T,q)!=-1){      	
	 	strcpy(f,"t");
        typeComp($1.type2,$3.type2); 
        sprintf(t,"%d",temp);
        strcat(f,t);
        temp++;
        insererquad("-",$1.val,$3.val,f,iterator);
        iterator++; }} 
	 | OPERANDE '<' OPERANDE {if(varNonDeclaree($1.val,T,q)!=-1 && varNonDeclaree($3.val,T,q)!=-1){      	
	 	strcpy(f,"t");
        typeComp($1.type2,$3.type2); 
        sprintf(t,"%d",temp);
        strcat(f,t);
        temp++;
        insererquad("-",$1.val,$3.val,f,iterator);
        iterator++;
         }}
;
DIFF: OPERANDE opc_diff OPERANDE { if(varNonDeclaree($1.val,T,q)!=-1 && varNonDeclaree($3.val,T,q)!=-1){     	
	    strcpy(f,"t");
        typeComp($1.type2,$3.type2); 
        sprintf(t,"%d",temp);
        strcat(f,t);
        temp++;
        insererquad("-",$1.val,$3.val,f,iterator);
        iterator++; } }
;
SUPEGALE: OPERANDE opc_supEgale OPERANDE { if(varNonDeclaree($1.val,T,q)!=-1 && varNonDeclaree($3.val,T,q)!=-1){      	
	    strcpy(f,"t");
        typeComp($1.type2,$3.type2); 
        sprintf(t,"%d",temp);
        strcat(f,t);
        temp++;
        insererquad("-",$1.val,$3.val,f,iterator);
        iterator++;
         } }
;
INFEGALE :OPERANDE opc_infEgale OPERANDE {  if(varNonDeclaree($1.val,T,q)!=-1 && varNonDeclaree($3.val,T,q)!=-1){     	
	    strcpy(f,"t");
        typeComp($1.type2,$3.type2); 
        sprintf(t,"%d",temp);
        strcat(f,t);
        temp++;
        insererquad("-",$1.val,$3.val,f,iterator);
        iterator++; } 

    }
;
%%
int yyerror(char *s)
{printf("%s a la ligne %d,%d \n",s,nbligne,nbcl);return 1;}
int main()
{
yyparse();
afficher();
afficherquad(iterator);

return 0;
}

