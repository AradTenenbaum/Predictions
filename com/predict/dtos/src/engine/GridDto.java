package engine;

public class GridDto {
    private int rows;
    private int cols;

    public GridDto(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }
}
