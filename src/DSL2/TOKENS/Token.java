package DSL2.TOKENS;

public record Token(String type, String value, int position, int line) {
    //

    //
    @Override
    public String toString() {
//        return "Token{" +
//                "type='" + type + '\'' +
//                ", value='" + value + '\'' +
//                ", position=" + position +
//                '}';
        return "" + position +
                ".  '" + type + '\'' +
                " -> '" + value + '\'' +
                "";
    }

    public String insteadWas() {
        return "'" + type + '\'' +
                " -> '" + value + '\'' +
                "";
    }
}
