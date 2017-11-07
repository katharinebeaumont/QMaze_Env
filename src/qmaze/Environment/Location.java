package qmaze.Environment;

/**
 *
 * @author katharine
 * COLUMN IS X
 * ROW IS Y
 */
public class Location {
    
    private final int col;
    private final int row;
    
    public Location(int col, int row) {
        this.col = col;
        this.row = row;
    }
    
    public Location(Location coordinates) {
        this.col = coordinates.getCol();
        this.row = coordinates.getRow();
    }
    
    public int getCol() {
        return col;
    }
    
    public int getRow() {
        return row;
    }
    
    @Override
    public String toString() {
        return "col (x): " + col + ", row (y): " + row;
    }
    
    public boolean adjoins(Location otherLocation) {
        int col_other = otherLocation.getCol();
        int row_other = otherLocation.getRow();
        return (row_other == row && (col_other == col-1 )) 
                || (row_other == row && (col_other == col+1 ))
                || (col_other == col && row_other == row-1)
                || (col_other == col && row_other == row+1);
    }

    public Location getAdjoining(Direction direction) {
        switch(direction) {
            case UP:
                return new Location(col, row-1);
            case DOWN:
                return new Location(col, row+1);
            case LEFT:
                return new Location(col-1, row);
            case RIGHT:
                return new Location(col+1, row);
            default:
                throw new RuntimeException("Do not understand " + direction);
        }
    }
    
    // Example: {"col":0,"row":0}
    public static Location parseFromString(String str) {
        int col;
        int row;
        try {
            str = str.replace("{", "");
            str = str.replace("}", "");
            String[] rowAndCol = str.split(",");
            String colStr = rowAndCol[0];
            String rowStr = rowAndCol[1];
            col = Integer.parseInt(colStr.substring(colStr.indexOf(":")+1, colStr.length()));
            row = Integer.parseInt(rowStr.substring(rowStr.indexOf(":")+1, rowStr.length()));
        } catch (ArrayIndexOutOfBoundsException | NumberFormatException e ) {
            System.out.println("Exception parsing string " + str);
            System.out.println(e.getMessage());
            throw new RuntimeException();
        }
        return new Location(col,row);
    }
    
    @Override
    public boolean equals(Object other) {
        if (!Location.class.isAssignableFrom(other.getClass())) {
            return false;
        }
        Location otherCoordinates = (Location)other;
        return (col == otherCoordinates.getCol() && row == otherCoordinates.getRow());
    }

    @Override
    public int hashCode() {
        //TODO did I actually write this?
        int hash = 7;
        hash = 97 * hash + this.col;
        hash = 97 * hash + this.row;
        return hash;
    }
}
