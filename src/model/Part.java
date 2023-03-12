package model;

public class Part {
    private TolopogyType type;
    private int index ; // začátek partu v index bufferu
    private int count; // počet primitiv nikoliv indexů

    public Part(TolopogyType type, int index, int count) {
        this.type = type;
        this.index = index;
        this.count = count;
    }

    public TolopogyType getType() {
        return type;
    }

    public int getIndex() {
        return index;
    }

    public int getCount() {
        return count;
    }
}
