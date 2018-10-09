package org.academiadecodigo.bootcamp.Objects.walls;

import org.academiadecodigo.bootcamp.Field;
import org.academiadecodigo.bootcamp.Objects.Destroyable;
import org.academiadecodigo.bootcamp.Objects.GameObject;
import org.academiadecodigo.bootcamp.Objects.ObjectFactory;
import org.academiadecodigo.bootcamp.Position.Position;
import org.academiadecodigo.simplegraphics.graphics.Color;
import org.academiadecodigo.simplegraphics.graphics.Rectangle;

public class Block extends GameObject implements Destroyable {
    private boolean powerUp;
    private Rectangle block;

    public Block(int x, int y, Field field, boolean powerUp){
        position = new Position(x, y, field);
        this.powerUp = powerUp;

        block = new Rectangle(position.getX(), position.getY(), field.getCellSize(), field.getCellSize());
        block.setColor(Color.ORANGE);
        block.fill();
    }

    @Override
    public void destroy(){
        if(powerUp){
            ObjectFactory.createPowerUp(position.getX(), position.getY(), position.getField());
        }
        destroyed = true;
    }

    @Override
    public boolean isDestroyed() {
        return destroyed;
    }
}