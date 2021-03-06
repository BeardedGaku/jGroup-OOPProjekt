package edu.chl.rocc.core.model;

import edu.chl.rocc.core.factories.IRoCCFactory;
import edu.chl.rocc.core.m2phyInterfaces.*;

import java.lang.*;

/**
 * Interface for factory creating all objects handling all non-physics related logic in our game.
 * <br>Implements IRoCCFactory.
 *
 * Created by Joel on 2015-05-08.
 */
public class RoCCFactory implements IRoCCFactory {

    public RoCCFactory(){

    }

    @Override
    public IPlayer createPlayer(String name) {
        return new Player(this);
    }

    @Override
    public ILevel createLevel(String name) {
        return new Level();
    }

    @Override
    public ICharacter createCharacter(String name, float x, float y) {
        return new edu.chl.rocc.core.model.Character(name);
    }

    @Override
    public IFood createFood(String name, int x, int y) {
        return new Food(x, y);
    }

    @Override
    public IWeapon createWeapon(String name, float x, float y){
        return new Weapon(new BulletFactory(), name);
    }

    @Override
    public IPickupableCharacter createPickupAbleCharacter(String name, int x, int y) {
        return new PickupableCharacter(name);
    }

    @Override
    public IEnemy createEnemy(String name, int x, int y, int hp){
        return new Enemy(hp, name, x, y);
    }
}
