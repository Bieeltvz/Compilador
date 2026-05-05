import java.util.Stack;

public class Sintatico implements Constants
{
    private Stack stack = new Stack();
    private Token currentToken;
    private Token previousToken;
    private Lexico scanner;
    private Semantico semanticAnalyser;

    private static final boolean isTerminal(int x)
    {
        return x < FIRST_NON_TERMINAL;
    }

    private static final boolean isNonTerminal(int x)
    {
        return x >= FIRST_NON_TERMINAL && x < FIRST_SEMANTIC_ACTION;
    }

    private static final boolean isSemanticAction(int x)
    {
        return x >= FIRST_SEMANTIC_ACTION;
    }

    private boolean step() throws LexicalError, SyntaticError, SemanticError
    {
        if (currentToken == null)
        {
            int pos = 0;
            if (previousToken != null)
                pos = previousToken.getPosition()+previousToken.getLexeme().length();

            currentToken = new Token(DOLLAR, "$", pos);
        }

        int x = ((Integer)stack.pop()).intValue();
        int a = currentToken.getId();

        if (x == EPSILON)
        {
            return false;
        }
        else if (isTerminal(x))
        {
            if (x == a)
            {
                if (stack.empty())
                    return true;
                else
                {
                    previousToken = currentToken;
                    currentToken = scanner.nextToken();
                    return false;
                }
            }
            else
            {
                throw new SyntaticError(buildErrorMessage(currentToken, PARSER_ERROR[x]), currentToken.getPosition());
            }
        }
        else if (isNonTerminal(x))
        {
            if (pushProduction(x, a))
                return false;
            else
                throw new SyntaticError(buildErrorMessage(currentToken, PARSER_ERROR[x]), currentToken.getPosition());
        }
        else // isSemanticAction(x)
        {
            semanticAnalyser.executeAction(x-FIRST_SEMANTIC_ACTION, previousToken);
            return false;
        }
    }

    private String buildErrorMessage(Token token, String expected)
    {
        String found;
        if (token.getId() == DOLLAR)
            found = "EOF";
        else if (token.getId() == t_cte_string)
            found = "constante_string";
        else
            found = escapeLexeme(token.getLexeme());

        return "encontrado " + found + " esperado " + expected;
    }

    private String escapeLexeme(String s)
    {
        if (s == null) return "";
        StringBuilder sb = new StringBuilder(s.length());
        for (int i = 0; i < s.length(); i++)
        {
            char c = s.charAt(i);
            if (c == '\n')      sb.append("\\n");
            else if (c == '\r') sb.append("\\r");
            else if (c == '\t') sb.append("\\t");
            else                sb.append(c);
        }
        return sb.toString();
    }

    private boolean pushProduction(int topStack, int tokenInput)
    {
        int p = PARSER_TABLE[topStack-FIRST_NON_TERMINAL][tokenInput-1];
        if (p >= 0)
        {
            int[] production = PRODUCTIONS[p];
            //empilha a produção em ordem reversa
            for (int i=production.length-1; i>=0; i--)
            {
                stack.push(new Integer(production[i]));
            }
            return true;
        }
        else
            return false;
    }

    public void parse(Lexico scanner, Semantico semanticAnalyser) throws LexicalError, SyntaticError, SemanticError
    {
        this.scanner = scanner;
        this.semanticAnalyser = semanticAnalyser;

        stack.clear();
        stack.push(new Integer(DOLLAR));
        stack.push(new Integer(START_SYMBOL));

        currentToken = scanner.nextToken();

        while ( ! step() )
            ;
    }
}
