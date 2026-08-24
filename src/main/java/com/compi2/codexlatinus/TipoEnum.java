package com.compi2.codexlatinus;

/**
 *
 * @author Usuario
 */
public enum TipoEnum {
    BOOL(1),
    LITTERA(2),
    NUMERUS(3),
    DECIMALIS(4),
    TEXTUM(5),
    DESCONOCIDO(0);

    private final int jerarquia;

    TipoEnum(int jerarquia) {
        this.jerarquia = jerarquia;
    }

    public int getJerarquia() {
        return jerarquia;
    }
    
    public static TipoEnum mayor(TipoEnum a, TipoEnum b) {

        if (a == null) {
            return b;
        }

        if (b == null) {
            return a;
        }

        return a.jerarquia >= b.jerarquia ? a : b;
    }
}
