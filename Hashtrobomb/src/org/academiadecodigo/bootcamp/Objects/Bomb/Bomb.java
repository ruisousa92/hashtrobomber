package org.academiadecodigo.bootcamp.Objects.Bomb;

import org.academiadecodigo.bootcamp.CollisionDetector;
import org.academiadecodigo.bootcamp.Field;
import org.academiadecodigo.bootcamp.Game;
import org.academiadecodigo.bootcamp.Objects.Destroyable;
import org.academiadecodigo.bootcamp.Objects.GameObject;
import org.academiadecodigo.bootcamp.Objects.ObjectFactory;
import org.academiadecodigo.bootcamp.Objects.Player;
import org.academiadecodigo.bootcamp.Objects.walls.Block;
import org.academiadecodigo.bootcamp.Objects.walls.Wall;
import org.academiadecodigo.bootcamp.Position.Directions;
import org.academiadecodigo.simplegraphics.pictures.Picture;
import sun.security.krb5.internal.crypto.Des;

import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class Bomb extends GameObject implements Destroyable {

    private final int EXPLOSION_TIMER = 750;
    private int power;
    private int timer = 3000;
    private Player player;
    private Picture bomb;
    private List<Explosion> explosionList = new LinkedList<>();


    public Bomb(int col, int row, Player player, int power, Field field) {

        super(col, row, field);
        this.power = power;
        this.player = player;

        bomb = new Picture(position.getX(), position.getY(), "resources/bomb1.png");
        bomb.draw();
        timerTask();


    }

    public void delete() {
        Game.gameObjects.remove(this);
        bomb.delete();
    }

    public void timerTask() {
        TimerTask explode = new TimerTask() {
            public void run() {
                explode();
            }
        };
        Timer trigger = new Timer();
        trigger.schedule(explode, timer);
    }

    public void explode() {

        int col = position.getCol();
        int row = position.getRow();

        this.delete();


        explosionList.add(ObjectFactory.createExplosion(col, row, position.getField()));


        for (int i = 1; i <= power; i++) {
            if (col + i <= Field.getMaxCol()) {
                if (!CollisionDetector.checkCollision(col + i, row, position.getField())) {
                    explosionList.add(ObjectFactory.createExplosion(col + i, row, position.getField()));
                    continue;
                }
                if (!(Game.objectAtPos(col + i, row, position.getField()) instanceof Wall)) {
                    explosionList.add(ObjectFactory.createExplosion(col + i, row, position.getField()));
                    break;
                }
            }
            break;
        }
        for (int i = 1; i <= power; i++) {
            if (col - i >= Field.getMinCol()) {
                if (!CollisionDetector.checkCollision(col - i, row, position.getField())) {
                    explosionList.add(ObjectFactory.createExplosion(col - i, row, position.getField()));
                    continue;
                }
                if (!(Game.objectAtPos(col - i, row, position.getField()) instanceof Wall)) {
                    explosionList.add(ObjectFactory.createExplosion(col - i, row, position.getField()));
                    break;
                }
            }
            break;
        }
        for (int i = 1; i <= power; i++) {
            if (row + i <= Field.getMaxRow()) {
                if (!CollisionDetector.checkCollision(col, row + i, position.getField())) {
                    explosionList.add(ObjectFactory.createExplosion(col, row + i, position.getField()));
                    continue;
                }
                if (!(Game.objectAtPos(col, row + i, position.getField()) instanceof Wall)) {
                    explosionList.add(ObjectFactory.createExplosion(col, row + i, position.getField()));
                    break;
                }
            }
            break;
        }
        for (int i = 1; i <= power; i++) {
            if (row - i >= Field.getMinRow()) {
                if (!CollisionDetector.checkCollision(col, row - i, position.getField()) && row - i >= Field.getMinRow()) {
                    explosionList.add(ObjectFactory.createExplosion(col, row - i, position.getField()));
                    continue;
                }
                if (!(Game.objectAtPos(col, row - i, position.getField()) instanceof Wall)) {
                    explosionList.add(ObjectFactory.createExplosion(col, row - i, position.getField()));
                    break;
                }
            }
            break;
        }

        for (Explosion explosion : explosionList) {
            GameObject obj = CollisionDetector.checkCollision(explosion);
            if (CollisionDetector.checkCollision(explosion) instanceof Destroyable) {
                ((Destroyable) obj).destroy();
            }
        }


        try {
            Thread.sleep(EXPLOSION_TIMER);
        } catch (InterruptedException ex) {

        }

        for (Explosion ex : explosionList) {
            ex.erase();
        }

        player.decreaseActiveBombs();
    }


    @Override
    public void destroy() {
        destroyed = true;
    }

    @Override
    public boolean isDestroyed() {
        return destroyed;
    }
}
