package utils.object;

import ins.EntityInstance;
import javafx.geometry.Pos;
import utils.func.RandomGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class Grid {
    private int rows;
    private int columns;
    private int[][] board;
    private int elementsAmount = 0;

    public Grid(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        this.board = new int[rows][columns];
        for(int[] line : this.board) {
            for(int i : line) {
                i = 0;
            }
        }
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    public Grid generateGrid(Map<String, List<EntityInstance>> entities) {
        Grid newGrid = new Grid(this.rows, this.columns);
        entities.forEach(((s, entityInstances) -> {
            entityInstances.forEach(entityInstance -> {
                Position insPos = newGrid.addOnRandom(entityInstance.getId());
                entityInstance.setPosition(insPos);
            });
        }));
        return newGrid;
    }

    public void removeFromPos(Position pos) {
        board[pos.getX()][pos.getY()] = 0;
    }

    private Position addOnRandom(int value) {
        int leftPlaces = (rows*columns) - elementsAmount;
        System.out.println("Left places: " + leftPlaces);
        if(leftPlaces > 0) {
            int randomLocation = (RandomGenerator.getInt(new Range(0, leftPlaces-1)));
            System.out.println("Random place: " + randomLocation);

            for(int i = 0; (i < rows); i++) {
                for(int j = 0; (j < columns); j++) {
                    if(board[i][j] == 0) {
                        if(randomLocation == 0) {
                            board[i][j] = value;
                            this.elementsAmount++;
                            System.out.println("Adding " + value);
                            printGrid();
                            return new Position(i, j);
                        }
                        randomLocation--;
                    }
                }
            }
        }
        System.out.println("Could not find");
        printGrid();
        return null;
    }

    public void setPos(Position pos, int value) {
        this.board[pos.getX()][pos.getY()] = value;
    }

    public int getPos(Position p) {
        return this.board[p.getX()][p.getY()];
    }

    private void move(Position from, Position to) {
        board[to.getX()][to.getY()] = board[from.getX()][from.getY()];
        board[from.getX()][from.getY()] = 0;
    }

    private boolean isFree(Position p) {
        return (getPos(p) == 0);
    }

    public Position moveToFreePosition(Position p) {
        Position newPos;
        List<Position.DIRECTION> dirList = new ArrayList<>();
        if(isFree(p.oneTo(Position.DIRECTION.UP, this))) {
            dirList.add(Position.DIRECTION.UP);
        }
        if(isFree(p.oneTo(Position.DIRECTION.DOWN, this))) {
            dirList.add(Position.DIRECTION.DOWN);
        }
        if(isFree(p.oneTo(Position.DIRECTION.RIGHT, this))) {
            dirList.add(Position.DIRECTION.RIGHT);
        }
        if(isFree(p.oneTo(Position.DIRECTION.LEFT, this))) {
            dirList.add(Position.DIRECTION.LEFT);
        }

        if(dirList.size() == 0) return p;
        int randomMove = RandomGenerator.getInt(new Range(0, dirList.size()-1));
        newPos = p.oneTo(dirList.get(randomMove), this);
        move(p, newPos);
        return newPos;
    }

    public void printGrid() {
        for(int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                System.out.print(String.format("%3d ", board[i][j]));
            }
            System.out.println();
        }
        System.out.println("===========================");
    }
}
