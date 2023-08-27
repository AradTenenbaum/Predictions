package utils.object;

public class Position {
    private int x;
    private int y;
    public enum DIRECTION {
        UP, DOWN, LEFT, RIGHT
    }

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Position oneTo(DIRECTION dir, Grid grid) {
        if(dir == DIRECTION.UP) {
            return oneUp(grid);
        }
        if(dir == DIRECTION.DOWN) {
            return oneDown(grid);
        }
        if (dir == DIRECTION.RIGHT) {
            return oneRight(grid);
        }
        if(dir == DIRECTION.LEFT) {
            return oneLeft(grid);
        }
        return this;
    }

    private Position oneUp(Grid grid) {
        Position pos = new Position(this.x, (this.y+1)%grid.getColumns());
        return pos;
    }

    private Position oneDown(Grid grid) {
        Position pos;
        if(this.y == 0) pos = new Position(this.x, grid.getColumns()-1);
        else pos = new Position(this.x, (this.y-1));
        return pos;
    }

    private Position oneRight(Grid grid) {
        Position pos = new Position((this.x+1)%grid.getRows(), this.y);
        return pos;
    }

    private Position oneLeft(Grid grid) {
        Position pos;
        if(this.x == 0) pos = new Position(grid.getRows()-1, this.y);
        else pos = new Position((this.x-1), this.y);
        return pos;
    }

    @Override
    public String toString() {
        return "Position{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
