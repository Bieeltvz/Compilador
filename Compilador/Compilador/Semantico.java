import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class Semantico implements Constants {

   //registros semanticos
   private StringBuilder        codigoObjeto         = new StringBuilder();
   private Stack<String>        pilhaTipos           = new Stack<>();
   private Stack<String>        pilhaRotulos         = new Stack<>();
   private List<String>         listaIdentificadores = new ArrayList<>();
   private Map<String, String>  tabelaSimbolos       = new HashMap<>();
   private String               operadorRelacional   = "";
   private String               tipo                 = "";
   private int                  contadorRotulos      = 0;

   public void executeAction(int action, Token token) throws SemanticError {
      switch (action) {
         case  1: acao1(); break;
         case  2: acao2(); break;
         case  3: acao3(); break;
         case  4: acao4(); break;
         case  5: acao5(token); break;
         case  6: acao6(token); break;
         case  7: acao7(); break;
         case  8: acao8(); break;
         case  9: acao9(token); break;
         case 10: acao10(); break;
         case 11: acao11(); break;
         case 12: acao12(); break;
         case 13: acao13(); break;
         case 14: acao14(); break;
         case 15: acao15(); break;
         case 16: acao16(); break;
         case 17: acao17(); break;
         case 18: acao18(token); break;
         case 19: acao19(token); break;
         case 20: acao20(); break;
         case 21: acao21(); break;
         case 22: acao22(token); break;
         case 23: acao23(); break;
         case 24: acao24(token); break;
         case 25: acao25(); break;
         case 26: acao26(token); break;
         case 27: acao27(); break;
         case 28: acao28(); break;
         case 29: acao29(); break;
         case 30: acao30(); break;
         case 31: acao31(token); break;
         case 32: acao32(); break;
         case 33: acao33(); break;
         case 34: acao34(); break;
         default:
                  throw new SemanticError("Acao semantica nao implementada: " + action);
      }
   }

   //cria um novo rotulo sequencial para os comandos de selecao e repeticao
   private String novoRotulo() {
      contadorRotulos++;
      return "rotulo" + contadorRotulos;
   }

   //operador aritmetico binario +
   private void acao1() {
      String tipo1 = pilhaTipos.pop();
      String tipo2 = pilhaTipos.pop();
      if ("int64".equals(tipo1) && "int64".equals(tipo2)) {
         pilhaTipos.push("int64");
      } else {
         pilhaTipos.push("float64");
      }
      codigoObjeto.append("add\n");
   }

   //operador aritmetico binario -
   private void acao2() {
      String tipo1 = pilhaTipos.pop();
      String tipo2 = pilhaTipos.pop();
      if ("int64".equals(tipo1) && "int64".equals(tipo2)) {
         pilhaTipos.push("int64");
      } else {
         pilhaTipos.push("float64");
      }
      codigoObjeto.append("sub\n");
   }

   //operador aritmetico binario *
   private void acao3() {
      String tipo1 = pilhaTipos.pop();
      String tipo2 = pilhaTipos.pop();
      if ("int64".equals(tipo1) && "int64".equals(tipo2)) {
         pilhaTipos.push("int64");
      } else {
         pilhaTipos.push("float64");
      }
      codigoObjeto.append("mul\n");
   }

   //operador aritmetico binario /
   private void acao4() {
      pilhaTipos.pop();
      pilhaTipos.pop();
      pilhaTipos.push("float64");
      codigoObjeto.append("div\n");
   }

   //constante_int
   private void acao5(Token token) {
      pilhaTipos.push("int64");
      codigoObjeto.append("ldc.i8 " + token.getLexeme() + "\n");
      codigoObjeto.append("conv.r8\n");
   }

   //constante_float
   private void acao6(Token token) {
      pilhaTipos.push("float64");
      codigoObjeto.append("ldc.r8 " + token.getLexeme() + "\n");
   }

   //operador aritmetico unario +
   private void acao7() {
      String tipo = pilhaTipos.pop();
      if ("int64".equals(tipo)) {
         pilhaTipos.push("int64");
      } else {
         pilhaTipos.push("float64");
      }
   }

   //operador aritmetico unario -
   private void acao8() {
      String tipo = pilhaTipos.pop();
      if ("int64".equals(tipo)) {
         pilhaTipos.push("int64");
      } else {
         pilhaTipos.push("float64");
      }
      codigoObjeto.append("ldc.i8 -1\n");
      codigoObjeto.append("conv.r8\n");
      codigoObjeto.append("mul\n");
   }

   //guarda o operador relacional reconhecido para uso posterior na acao 10
   private void acao9(Token token) {
      operadorRelacional = token.getLexeme();
   }

   //gera codigo para o operador relacional armazenado na acao 9
   private void acao10() {
      String tipo1 = pilhaTipos.pop();
      String tipo2 = pilhaTipos.pop();
      pilhaTipos.push("bool");
      boolean strings = "string".equals(tipo1) && "string".equals(tipo2);
      switch (operadorRelacional) {
         case "==":
            if (strings) {
               codigoObjeto.append("call bool [mscorlib]System.String::op_Equality(string, string)\n");
            } else {
               codigoObjeto.append("ceq\n");
            }
            break;
         case "!=":
            if (strings) {
               codigoObjeto.append("call bool [mscorlib]System.String::op_Inequality(string, string)\n");
            } else {
               codigoObjeto.append("ceq\n");
               codigoObjeto.append("ldc.i4.1\n");
               codigoObjeto.append("xor\n");
            }
            break;
         case "<":
            if (strings) {
               codigoObjeto.append("call int32 [mscorlib]System.String::Compare(string, string)\n");
               codigoObjeto.append("ldc.i4.0\n");
            }
            codigoObjeto.append("clt\n");
            break;
         case ">":
            if (strings) {
               codigoObjeto.append("call int32 [mscorlib]System.String::Compare(string, string)\n");
               codigoObjeto.append("ldc.i4.0\n");
            }
            codigoObjeto.append("cgt\n");
            break;
         case "<=":
            if (strings) {
               codigoObjeto.append("call int32 [mscorlib]System.String::Compare(string, string)\n");
               codigoObjeto.append("ldc.i4.0\n");
            }
            codigoObjeto.append("cgt\n");
            codigoObjeto.append("ldc.i4.1\n");
            codigoObjeto.append("xor\n");
            break;
         case ">=":
            if (strings) {
               codigoObjeto.append("call int32 [mscorlib]System.String::Compare(string, string)\n");
               codigoObjeto.append("ldc.i4.0\n");
            }
            codigoObjeto.append("clt\n");
            codigoObjeto.append("ldc.i4.1\n");
            codigoObjeto.append("xor\n");
            break;
      }
      operadorRelacional = "";
   }

   //constante true
   private void acao11() {
      pilhaTipos.push("bool");
      codigoObjeto.append("ldc.i4.1\n");
   }

   //constante false
   private void acao12() {
      pilhaTipos.push("bool");
      codigoObjeto.append("ldc.i4.0\n");
   }

   //operador logico unario ! (not)
   private void acao13() {
      pilhaTipos.pop();
      pilhaTipos.push("bool");
      codigoObjeto.append("ldc.i4.1\n");
      codigoObjeto.append("xor\n");
   }

   //comando de saida (tell)
   private void acao14() {
      String tipo = pilhaTipos.pop();
      if ("int64".equals(tipo)) {
         codigoObjeto.append("conv.i8\n");
      }
      codigoObjeto.append("call void [mscorlib]System.Console::Write(" + tipo + ")\n");
   }

   //operador logico binario && (and)
   private void acao15() {
      pilhaTipos.pop();
      pilhaTipos.pop();
      pilhaTipos.push("bool");
      codigoObjeto.append("and\n");
   }

   //operador logico binario || (or)
   private void acao16() {
      pilhaTipos.pop();
      pilhaTipos.pop();
      pilhaTipos.push("bool");
      codigoObjeto.append("or\n");
   }

   //operador aritmetico binario ^ (potenciacao)
   private void acao17() {
      String tipo1 = pilhaTipos.pop();
      String tipo2 = pilhaTipos.pop();
      if ("int64".equals(tipo1) && "int64".equals(tipo2)) {
         pilhaTipos.push("int64");
      } else {
         pilhaTipos.push("float64");
      }
      codigoObjeto.append("call float64 [mscorlib]System.Math::Pow(float64, float64)\n");
   }

   //constante_char: \n \t \s
   private void acao18(Token token) {
      pilhaTipos.push("char");
      int valor;
      switch (token.getLexeme()) {
         case "\\n": valor = 10; break;
         case "\\t": valor = 9;  break;
         default:    valor = 32; break;   // \s (espaco)
      }
      codigoObjeto.append("ldc.i4 " + valor + "\n");
   }

   //constante_string
   private void acao19(Token token) {
      pilhaTipos.push("string");
      codigoObjeto.append("ldstr " + token.getLexeme() + "\n");
   }

   //cabecalho do programa objeto
   private void acao20() {
      codigoObjeto.append(".assembly extern mscorlib {}\n");
      codigoObjeto.append(".assembly _programa{}\n");
      codigoObjeto.append(".module _programa.exe\n");
      codigoObjeto.append("\n");
      codigoObjeto.append(".class public _unica{\n");
      codigoObjeto.append(".method static public void _principal(){\n");
      codigoObjeto.append(".entrypoint\n");
   }

   //fim do programa objeto
   private void acao21() {
      codigoObjeto.append("ret\n");
      codigoObjeto.append("}\n");
      codigoObjeto.append("}");
   }

   //guarda o tipo reconhecido, ja convertido para o tipo IL, para uso na acao 23
   private void acao22(Token token) {
      switch (token.getLexeme()) {
         case "int":   tipo = "int64";   break;
         case "float": tipo = "float64"; break;
         default:      tipo = token.getLexeme();   // bool, char, string
      }
   }

   //declaracao de variaveis: insere na tabela de simbolos e gera .locals
   private void acao23() {
      for (String id : listaIdentificadores) {
         tabelaSimbolos.put(id, tipo);
         codigoObjeto.append(".locals(" + tipo + " " + id + ")\n");
      }
      listaIdentificadores.clear();
   }

   //guarda o identificador reconhecido para uso posterior nas acoes 23 e 25
   private void acao24(Token token) {
      listaIdentificadores.add(token.getLexeme());
   }

   //comando de atribuicao
   private void acao25() {
      String tipoExpressao = pilhaTipos.pop();
      if ("int64".equals(tipoExpressao)) {
         codigoObjeto.append("conv.i8\n");
      }
      for (int i = 0; i < listaIdentificadores.size() - 1; i++) {
         codigoObjeto.append("dup\n");
      }
      for (String id : listaIdentificadores) {
         codigoObjeto.append("stloc " + id + "\n");
      }
      listaIdentificadores.clear();
   }

   //comando de entrada (ask)
   private void acao26(Token token) throws SemanticError {
      String id = token.getLexeme();
      String tipoId = tabelaSimbolos.get(id);
      if ("bool".equals(tipoId) || "char".equals(tipoId)) {
         throw new SemanticError(id + " - identificador inválido para comando de entrada", token.getPosition());
      }
      codigoObjeto.append("call string [mscorlib]System.Console::ReadLine()\n");
      if ("int64".equals(tipoId)) {
         codigoObjeto.append("call int64 [mscorlib]System.Int64::Parse(string)\n");
      } else if ("float64".equals(tipoId)) {
         codigoObjeto.append("call float64 [mscorlib]System.Double::Parse(string)\n");
      }
      codigoObjeto.append("stloc " + id + "\n");
   }

   //comando de selecao (if): desvia para o else/elif/end se a condicao for falsa
   private void acao27() {
      pilhaTipos.pop();
      String novoRotulo1 = novoRotulo();
      pilhaRotulos.push(novoRotulo1);
      String novoRotulo2 = novoRotulo();
      codigoObjeto.append("brfalse " + novoRotulo2 + "\n");
      pilhaRotulos.push(novoRotulo2);
   }

   //fim dos comandos de uma clausula if/elif: desvia para o fim do comando de selecao
   private void acao28() {
      String rotuloDesempilhado1 = pilhaRotulos.pop();
      String rotuloDesempilhado2 = pilhaRotulos.pop();
      codigoObjeto.append("br " + rotuloDesempilhado2 + "\n");
      pilhaRotulos.push(rotuloDesempilhado2);
      codigoObjeto.append(rotuloDesempilhado1 + ":\n");
   }

   //fim do comando de selecao: rotula a primeira instrucao apos o end
   private void acao29() {
      String rotuloDesempilhado = pilhaRotulos.pop();
      codigoObjeto.append(rotuloDesempilhado + ":\n");
   }

   //clausula elif: desvia para o proximo elif/else/end se a condicao for falsa
   private void acao30() {
      pilhaTipos.pop();
      String novoRotulo = novoRotulo();
      codigoObjeto.append("brfalse " + novoRotulo + "\n");
      pilhaRotulos.push(novoRotulo);
   }

   //identificador em expressao: carrega o valor da variavel
   private void acao31(Token token) {
      String id = token.getLexeme();
      String tipoId = tabelaSimbolos.get(id);
      pilhaTipos.push(tipoId);
      codigoObjeto.append("ldloc " + id + "\n");
      if ("int64".equals(tipoId)) {
         codigoObjeto.append("conv.r8\n");
      }
   }

   //comando de repeticao (repeat): rotula o primeiro comando da repeticao
   private void acao32() {
      String novoRotulo = novoRotulo();
      codigoObjeto.append(novoRotulo + ":\n");
      pilhaRotulos.push(novoRotulo);
   }

   //repeat ... while: repete enquanto a condicao for verdadeira
   private void acao33() {
      pilhaTipos.pop();
      String rotuloDesempilhado = pilhaRotulos.pop();
      codigoObjeto.append("brtrue " + rotuloDesempilhado + "\n");
   }

   //repeat ... until: repete ate a condicao ser verdadeira
   private void acao34() {
      pilhaTipos.pop();
      String rotuloDesempilhado = pilhaRotulos.pop();
      codigoObjeto.append("brfalse " + rotuloDesempilhado + "\n");
   }

   public String getCodigoObjeto() {
      return codigoObjeto.toString();
   }
}
