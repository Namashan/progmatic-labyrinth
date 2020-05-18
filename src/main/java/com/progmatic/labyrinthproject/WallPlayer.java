package com.progmatic.labyrinthproject;

import com.progmatic.labyrinthproject.enums.Direction;
import com.progmatic.labyrinthproject.interfaces.Labyrinth;
import com.progmatic.labyrinthproject.interfaces.Player;

import java.util.List;

public class WallPlayer implements Player {

    Direction former;
    boolean first;

    public WallPlayer() {
        first=true;
    }


    @Override
    public Direction nextMove(Labyrinth l) {
        List<Direction> d = l.possibleMoves();
        if(first){
            first = false;
            former = d.get(0);              // ah, még sem megy.....
            return d.get(0);
        }
        return null;
    }
}
