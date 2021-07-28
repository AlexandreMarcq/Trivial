package Trivial.Compiler.Token;

/**
 * Token class to store a token.
 */

public class Token {
    /**
     * The token that got scanned.
     *
     * @see Token#getToken()
     */
    private String mToken;
    /**
     * The category of the token.
     *
     * @see Token#getCategory()
     */
    private String mCategory;
    /**
     * The line where the token was scanned.
     */
    private int mLine;

    /**
     * Token constructor.
     *
     * @param token
     *                  The token that got scanned
     * @param category
     *                  The category of the token
     * @param line
     *                  The line of the token
     *
     * @see Token#mToken
     * @see Token#mCategory
     * @see Token#mLine
     */
    public Token(String token, String category, int line) {
        this.mToken = token;
        this.mCategory = category;
        this.mLine = line;
    }

    /**
     * Gets the token.
     * @return The token
     */
    public String getToken() {
        return mToken;
    }

    /**
     * Gets the token's category.
     * @return The token's category
     */
    public String getCategory() {
        return mCategory;
    }

    /**
     * Gets the token's line.
     * @return The token's line.
     */
    public int getLine() { return mLine; }

    /**
     * Prints the token into a more readable way.
     */
    @Override
    public String toString() {
        return this.mToken;
    }

    public boolean hasHigherPrecedence(Token other) {
        switch (this.getToken()) {
            case ("*"):
            case ("/"):
                if ("*".equals(other.getToken())
                    || "/".equals(other.getToken())
                    || "+".equals(other.getToken())
                    || "-".equals(other.getToken())
                    || "=".equals(other.getToken())) {
                    return true;
                }
                break;
            case ("+"):
            case ("-"):
                if ("+".equals(other.getToken())
                        || "-".equals(other.getToken())
                        || "=".equals(other.getToken()))
                    return true;
                break;
        }
        return false;
    }
    /*public String toString() {
        String str = "";
        switch (this.mCategory) {
            case "Type":
                str = this.mToken + " -> Type -> " + mValue + " -> L" + this.mLine;
                break;
            case "Operator":
                str = this.mToken + " -> Operator -> " + mValue + " -> L" + this.mLine;
                break;
            case "Keyword":
                str = this.mToken + " -> Keyword -> " + mValue + " -> L" + this.mLine;
                break;
            case "Identifier":
                str = this.mToken + " -> Identifier -> " + mValue + " -> L" + this.mLine;
                break;
            case "Delimiter":
                str = this.mToken + " -> Delimiter -> " + mValue + " -> L" + this.mLine;
                break;
            case "Constant":
                str = this.mToken + " -> Constant -> " + mValue + " -> L" + this.mLine;
        }
        return str;
    }*/
}
