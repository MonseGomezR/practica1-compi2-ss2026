package com.compi2.codexlatinus.errores;
        
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

@Getter
public class ErrorListener extends BaseErrorListener {

    private final List<Error> errores;
    
    public ErrorListener() {
        errores = new ArrayList<>();
    }
            
    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
        errores.add(new Error(msg, line, charPositionInLine, "sintactico"));
    }
}