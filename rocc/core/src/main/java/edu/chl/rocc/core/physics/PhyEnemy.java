package edu.chl.rocc.core.physics;

import edu.chl.rocc.core.observers.DeathEvent;
import edu.chl.rocc.core.utility.Direction;
import edu.chl.rocc.core.observers.IDeathListener;
import edu.chl.rocc.core.m2phyInterfaces.IEnemy;
import edu.chl.rocc.core.model.Enemy;
import edu.chl.rocc.core.fileHandlers.CharacterLoader;
import edu.chl.rocc.core.observers.IDeathEvent;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;
import static edu.chl.rocc.core.utility.GlobalConstants.PPM;

/**
 * A class to handle enemies (including box2D stuff)
 * <br>Implements IEnemy.
 *
 * Created by Yen on 2015-05-09.
 */
public class PhyEnemy implements IEnemy {

    private final World world;
    private final IEnemy enemy;
    private final float width, height;
    private final Body body;
    private final BodyDef def;
    private final FixtureDef fDef;
    private Direction direction;

    private final int damageHP;
    private final int airForce;
    private final int health;
    private final int value;
    private int nbrOfCollisions = 0;

    /**
     * Creates a enemy. Constructor.
     *
     * @param world
     * @param x
     * @param y
     * @param name
     */
    public PhyEnemy(World world, float x, float y, String name){

        CharacterLoader cl = new CharacterLoader(name, true);
        this.airForce        = cl.getCharecaristic("AirForce");
        this.damageHP        = cl.getCharecaristic("DamageDeal");
        this.health          = cl.getCharecaristic("Health");
        this.value           = cl.getCharecaristic("Value");

        this.world = world;
        this.width = 16 / PPM;
        this.height = 25 / PPM;
        this.enemy = new Enemy(health, name, 0, 0);
        this.direction = Direction.LEFT;

        //Defining & creating body
        def = new BodyDef();
        def.position.set(x , y);
        def.type = BodyType.DYNAMIC;
        body = this.world.createBody(def);
        body.setUserData(this);
        body.setLinearVelocity(new Vec2(-1, 0));

        //Defining & creating fixture
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width,height);
        fDef = new FixtureDef();
        fDef.shape = shape;
        fDef.filter.categoryBits = BitMask.BIT_ENEMY;
        fDef.filter.maskBits = BitMask.BIT_GROUND;
        body.createFixture(fDef).setUserData("enemyBody");

        //create upperbody sideSensor
        shape.setAsBox(width, height/2 , new Vec2(0, 0) ,0);
        fDef.shape = shape;
        fDef.filter.categoryBits = BitMask.BIT_ENEMY;
        fDef.filter.maskBits = BitMask.BIT_GROUND | BitMask.BIT_BODY | BitMask.BIT_BULLET;
        fDef.isSensor = true;
        body.createFixture(fDef).setUserData("enemyUpperSensor");
    }


    @Override
    public void changeMoveDirection(){
            if(nbrOfCollisions==1) {
                if (this.direction.equals(Direction.LEFT)) {
                    body.setLinearVelocity(new Vec2(airForce, 0));
                    this.direction = Direction.RIGHT;
                } else {
                    body.setLinearVelocity(new Vec2(-airForce, 0));
                    this.direction = Direction.LEFT;
                }
                nbrOfCollisions = nbrOfCollisions - 2;
                this.enemy.move(this.direction);
            }
        nbrOfCollisions++;
    }


    @Override
    public int getDamageDeal() {
        return damageHP;
    }

    @Override
    public String getName() {
        return this.enemy.getName();
    }

    @Override
    public float getX() {
        return (body.getPosition().x - width) * PPM;
    }

    @Override
    public float getY() {
        return (body.getPosition().y - height) * PPM;
    }

    @Override
    public int getHP() {
        return this.enemy.getHP();
    }

    @Override
    public void setHP(int value) {
        this.enemy.setHP(value);
    }

    @Override
    public void decHP(int value) {
        if (enemy.getHP() <= value){
            this.death("No more hp");
        } else {
            this.enemy.decHP(value);
        }
    }

    @Override
    public Direction getDirection(){
        return this.direction;
    }

    @Override
    public void move(Direction dir) {
        this.enemy.move(direction);
    }

    @Override
    public void dispose() {
        this.enemy.dispose();
        world.destroyBody(body);
    }

    @Override
    public String getMoveState(){
        return enemy.getMoveState();
    }

    @Override
    public int getValue() {
        return value;
    }

    @Override
    public void addDeathListener(IDeathListener listener) {
        this.enemy.addDeathListener(listener);
    }

    @Override
    public void removeDeathListener(IDeathListener listener) {
        this.enemy.removeDeathListener(listener);
    }

    @Override
    public void death(String message) {
        this.enemy.death(new DeathEvent(this, message));
    }

    @Override
    public void death(IDeathEvent deathEvent) {
        this.enemy.death(deathEvent);
    }

}
