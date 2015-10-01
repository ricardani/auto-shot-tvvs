package gui;

/**
 * Eduardo Fernandes
 * Filipe Eiras
 */
class MoveValues implements Comparable<MoveValues> {
    
    private final int number;
    
    private final int value;

    public MoveValues( int number,  int value) {
        this.number = number;
        this.value = value;
    }

    public int getNumber() {
        return number;
    }

    @Override
    public int compareTo( MoveValues o) {
        return (value < o.value) ? 1 : ((value > o.value) ? -1 : 0);
    }
}
