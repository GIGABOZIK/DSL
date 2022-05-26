package DSL2.TOKENS;

public record Token(String type, String value, int position) {
    //

    //
    @Override
    public String toString() {
        return "Token{" +
                "type='" + type + '\'' +
                ", value='" + value + '\'' +
                ", position=" + position +
                '}';
    }
}
