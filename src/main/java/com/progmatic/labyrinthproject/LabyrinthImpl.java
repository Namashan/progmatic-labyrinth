package com.progmatic.labyrinthproject;

import com.progmatic.labyrinthproject.enums.CellType;
import com.progmatic.labyrinthproject.enums.Direction;
import com.progmatic.labyrinthproject.exceptions.CellException;
import com.progmatic.labyrinthproject.exceptions.InvalidMoveException;
import com.progmatic.labyrinthproject.interfaces.Labyrinth;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * @author pappgergely
 */
public class LabyrinthImpl implements Labyrinth {

    private ArrayList<ArrayList<CellType>> labyrth;
    private Coordinate currnt;

    public LabyrinthImpl() {

    }

    @Override
    public int getWidth() {
        try {
            return labyrth.get(0).size();
        } catch (NullPointerException e) {
            System.out.println("No labyrinth to get with.");
            return -1;
        }
    }

    @Override
    public int getHeight() {
        try {
            return labyrth.size();
        } catch (NullPointerException e) {
            System.out.println("There is no labyrinth.");
            return -1;
        }
    }

    @Override
    public void loadLabyrinthFile(String fileName) {
        labyrth = new ArrayList<>();
        try {
            Scanner sc = new Scanner(new File(fileName));
            int width = Integer.parseInt(sc.nextLine());
            int height = Integer.parseInt(sc.nextLine());
            for (int hh = 0; hh < height; hh++) {
                String line = sc.nextLine();
                labyrth.add(new ArrayList<>());
                for (int ww = 0; ww < width; ww++) {
                    switch (line.charAt(ww)) {
                        case 'W':
                            labyrth.get(hh).add(CellType.WALL);
                            break;
                        case 'E':
                            labyrth.get(hh).add(CellType.END);
                            break;
                        case 'S':
                            labyrth.get(hh).add(CellType.START);
                            currnt = new Coordinate(ww, hh);
                            break;
                        default:
                            labyrth.get(hh).add(CellType.EMPTY);
                            break;
                    }
                }
            }
        } catch (FileNotFoundException | NumberFormatException ex) {
            System.out.println(ex.toString());
        }
    }

    @Override
    public CellType getCellType(Coordinate c) throws CellException {
        try {
            int l = labyrth.size();
        } catch (NullPointerException e) {
            throw new CellException(c, "No labyrinth to get with.");
        }
        if (labyrth.isEmpty()) {
            throw new CellException(c, "No labyrinth to get with.");
        } else if (c.getRow() < 0 || c.getCol() < 0 || c.getRow() >= labyrth.size()
                || c.getCol() >= labyrth.get(0).size()) {
            throw new CellException(c, "The coordinates are out of the labyrinth.");
        }
        return labyrth.get(c.getRow()).get(c.getCol());
    }

    @Override
    public void setSize(int width, int height) {
        labyrth = new ArrayList<>();
        for (int i = 0; i < height; i++) {
            labyrth.add(new ArrayList<>());
            for (int j = 0; j < width; j++) {
                labyrth.get(i).add(CellType.EMPTY);
            }
        }
    }


    @Override
    public void setCellType(Coordinate c, CellType type) throws CellException {
        try {
            int l = labyrth.size();
        } catch (NullPointerException e) {
            throw new CellException(c, "No labyrinth to get with.");
        }
        if (labyrth.isEmpty()) {
            throw new CellException(c, "No labyrinth to get with.");
        } else if (c.getRow() < 0 || c.getCol() < 0 || c.getRow() > labyrth.size()
                || c.getCol() > labyrth.get(0).size()) {
            throw new CellException(c, "The coordinates are out of the labyrinth.");
        }
        if (type == CellType.START) {
            currnt = new Coordinate(c.getCol(), c.getRow());
        }
        labyrth.get(c.getRow()).set(c.getCol(), type);
    }

    @Override
    public Coordinate getPlayerPosition() {
        try {
            return currnt;
        } catch (NullPointerException e) {
            System.out.println("No labyrinth to get with.");
            return null;
        }
    }

    @Override
    public boolean hasPlayerFinished() {
        try {
            return labyrth.get(currnt.getRow()).get(currnt.getCol()) == CellType.END;
        } catch (NullPointerException e) {
            System.out.println("No labyrinth to get with.");
            return false;
        }
    }

    @Override
    public List<Direction> possibleMoves() {
        List<Direction> d = new ArrayList<>();
        CellType[] c = {CellType.EMPTY, CellType.END};
        HashSet<CellType> valid = new HashSet<>(Arrays.asList(c));
        try {
            if (currnt.getRow() > 0 && valid.contains(labyrth.get(currnt.getRow() - 1)
                    .get(currnt.getCol()))) {
                d.add(Direction.NORTH);
            }
            if (currnt.getRow() < (labyrth.size() - 1) && valid.contains(labyrth.get(currnt.getRow() + 1)
                    .get(currnt.getCol()))) {
                d.add(Direction.SOUTH);
            }
            if (currnt.getCol() > 0 && valid.contains(labyrth.get(currnt.getRow())
                    .get(currnt.getCol() - 1))) {
                d.add(Direction.WEST);
            }
            if (currnt.getCol() < (labyrth.get(0).size() - 1) && valid.contains(labyrth.get(currnt.getRow())
                    .get(currnt.getCol() + 1))) {
                d.add(Direction.EAST);
            }
        } catch (NullPointerException e) {
            System.out.println("No labyrinth to get with.");
        }
        return d;
    }

    @Override
    public void movePlayer(Direction direction) throws InvalidMoveException {
        List<Direction> d = possibleMoves();
        if (!d.contains(direction)) {
            throw new InvalidMoveException();
        }
        int x = currnt.getCol();
        int y = currnt.getRow();
        switch (direction) {
            case NORTH:
                currnt = new Coordinate(x, y - 1);
                break;
            case EAST:
                currnt = new Coordinate(x + 1, y);
                break;
            case SOUTH:
                currnt = new Coordinate(x, y + 1);
                break;
            case WEST:
                currnt = new Coordinate(x - 1, y);
                break;
        }
    }
}
