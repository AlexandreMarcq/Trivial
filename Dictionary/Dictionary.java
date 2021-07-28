package Trivial.Compiler.Dictionary;

/**
 * The Dictionary enum for the list of our reserved words.
 */
public enum Dictionary {

    CAS ("cas"),
    DEFAUT ("defaut"),
    DEFINIR ("definir"),
    ET ("ET"),
    FAIRE ("faire"),
    FAUX ("faux"),
    FINIR ("finir"),
    FONCTION ("fonction"),
    INCLURE ("inclure"),
    INDEFINI ("indefini"),
    NON ("NON"),
    NUL ("nul"),
    OU ("OU"),
    POUR ("pour"),
    RETOURNE ("retourne"),
    SELON ("selon"),
    SI ("si"),
    SINON ("sinon"),
    TANTQUE ("tantque"),
    VIDE ("vide"),
    VRAI ("vrai");

    /**
     * The name of the keyword.
     */
    private String mName;

    Dictionary(String name){
        this.mName = name;
    }

    public String toString(){
        return mName;
    }
}
