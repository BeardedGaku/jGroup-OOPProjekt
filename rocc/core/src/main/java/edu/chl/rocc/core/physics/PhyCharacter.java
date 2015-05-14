package edu.chl.rocc.core.physics;

import static edu.chl.rocc.core.GlobalConstants.PPM;

import edu.chl.rocc.core.m2phyInterfaces.ICharacter;
import edu.chl.rocc.core.model.*;
import edu.chl.rocc.core.model.Character;
import edu.chl.rocc.core.utility.CharacterLoader;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;

/**
 * Created by Joel on 2015-05-03.
 */
public class PhyCharacter implements ICharacter {

    private final World world;
    private final ICharacter character;
    private final float width, height;
    private final Body body;
    private int leftV;
    private int rightV;
    private int characterOnGround;
    private Direction direction;
    private Direction airDir;
    private final int speed;
    private final int numberOfJumps;
    private final int jumpForce;
    private final int airForce;

    private boolean followerOnJumpPoint;
    private boolean isMoving;

    public PhyCharacter(World world, float x, float y, String name){
        this.world = world;
        this.width = 18 / PPM;
        this.height = 35 / PPM;
        this.character = new Character(name);

        this.followerOnJumpPoint = false;
        this.isMoving = false;

        CharacterLoader cl = new CharacterLoader(name);
        this.speed         = cl.getCharecaristic("Speed");
        this.numberOfJumps = cl.getCharecaristic("NumberOfJumps");
        this.jumpForce     = cl.getCharecaristic("JumpForce");
        this.airForce      = cl.getCharecaristic("AirForce");

        airDir = Direction.NONE;

        //Defining & creating body
        BodyDef def = new BodyDef();
        def.position.set(x / PPM, y / PPM);
        def.type = BodyType.DYNAMIC;
        body = this.world.createBody(def);
        body.setUserData(this);

        //Defining & creating fixture
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width,height);
        FixtureDef fDef = new FixtureDef();
        fDef.shape = shape;
        fDef.filter.categoryBits = BitMask.BIT_BODY;
        fDef.filter.maskBits = BitMask.BIT_GROUND | BitMask.BIT_PICKUPABLE | BitMask.BIT_ENEMY | BitMask.BIT_JUMPPOINT;
        body.createFixture(fDef).setUserData("body");

        //create foot sensor
        shape.setAsBox(width/2, height/4, new Vec2(0, -30 / PPM) ,0);
        fDef.shape = shape;
        fDef.filter.categoryBits = BitMask.BIT_BODY;
        fDef.filter.maskBits = BitMask.BIT_GROUND;
        fDef.isSensor = true;
        body.createFixture(fDef).setUserData("footSensor");
    }

    @Override
    public int getHP() {
        return this.character.getHP();
    }

    @Override
    public void setHP(int value) {
        this.character.setHP(value);
    }

    @Override
    public void incHP(int value) {
        this.character.incHP(value);
    }

    @Override
    public void decHP(int value) {
        this.character.decHP(value);
    }

    @Override
    public void move(Direction dir) {
        if (dir != direction && (characterOnGround > 0)) {
                if (dir.equals(Direction.LEFT)) {
                    body.setLinearVelocity(new Vec2(-speed / PPM, 0));
                    leftV = leftV + 1;
                    this.isMoving = true;
                } else if (dir.equals(Direction.RIGHT)) {
                    body.setLinearVelocity(new Vec2( speed / PPM, 0));
                    rightV = rightV + 1;
                    this.isMoving = true;
                } else if (dir.equals(Direction.UP)) {

                } else if (dir.equals(Direction.DOWN)) {

                } else if (dir.equals(Direction.NONE)) {
                    if (leftV > 0) {
                        body.setLinearVelocity(new Vec2(0, 0));
                        leftV = leftV - 1;
                        this.isMoving = false;
                    } else if (rightV > 0) {
                        body.setLinearVelocity(new Vec2(0, 0));
                        rightV = rightV - 1;
                        this.isMoving = false;
                    }
                }
            direction = dir;
        } else if (dir != airDir && characterOnGround == 0){
            if (dir.equals(Direction.LEFT)) {
                body.applyForceToCenter(new Vec2(-airForce, 0));
            } else if (dir.equals(Direction.RIGHT)) {
                body.applyForceToCenter(new Vec2( airForce, 0));
            } else if (dir.equals(Direction.UP)) {

            } else if (dir.equals(Direction.DOWN)) {

            } else if (dir.equals(Direction.NONE)) {
                if (airDir == Direction.LEFT) {
                    body.applyForceToCenter(new Vec2( airForce, 0));
                } else if (airDir == Direction.RIGHT) {
                    body.applyForceToCenter(new Vec2(-airForce, 0));
                }
            }
            airDir = dir;
        }

    }

    @Override
    public void moveFollower(Direction dir){
        this.move(dir);
        character.moveFollower(dir);
    }

    @Override
    public boolean isMoving(){
        return this.isMoving;
    }

    @Override
    public Direction getFollowerDirection(){
        return character.getFollowerDirection();
    }

    @Override
    public Direction getLastFollowerDir(){
        return character.getLastFollowerDir();
    }

    @Override
    public void jump() {
       if(characterOnGround > 0) {
           this.body.setLinearVelocity(new Vec2(0, 0));
           this.body.applyForceToCenter(new Vec2(0, jumpForce));
           airDir = Direction.UP;

           character.leftGround();
       }
    }

    @Override
    public void jumpIfFollower(){
        if(this.character.isFollower() && this.isOnJumpPoint()){
            this.jump();
        }
    }

    @Override
    public void toggleFollowerOnJumpPoint(){
        if(this.followerOnJumpPoint){
            this.followerOnJumpPoint = false;
        } else{
            this.followerOnJumpPoint = true;
        }
    }

    @Override
    public boolean isOnJumpPoint(){
        return this.followerOnJumpPoint;
    }

    @Override
    public void hitGround(){
        if (characterOnGround == 0){
                if (airDir.equals(Direction.LEFT)) {
                    body.setLinearVelocity(new Vec2(-speed / PPM, 0));
                    leftV = leftV + 1;
                } else if (airDir.equals(Direction.RIGHT)) {
                    body.setLinearVelocity(new Vec2( speed / PPM, 0));
                    rightV = rightV + 1;
                } else if (airDir.equals(Direction.UP)) {

                } else if (airDir.equals(Direction.DOWN)) {

                } else if (airDir.equals(Direction.NONE)) {
                    body.setLinearVelocity(new Vec2(0, 0));
                }
                direction = airDir;

            character.hitGround();
        }
        characterOnGround++;


    }

    @Override
    public void leftGround(){
        characterOnGround--;
        if (characterOnGround == 0){
            if(airDir != Direction.UP) {
                this.body.setLinearVelocity(new Vec2(0, 0));
                character.leftGround();
            }
            airDir = Direction.NONE;
        }
    }

    @Override
    public float getX() {
        return (body.getPosition().x-width) * PPM;
    }

    @Override
    public float getY() {
        return (body.getPosition().y-height) * PPM ;
    }

    @Override
    public String getName() {
        return character.getName();
    }

    @Override
    public void dispose() {
        character.dispose();
        world.destroyBody(body);
    }

    @Override
    public boolean equals(Object o){
        if (this == o){
            return true;
        } else if (o == null){
            return false;
        } else if (this.getClass() != o.getClass()){
            return false;
        } else {
            return this.hashCode() == o.hashCode();
        }
    }

    @Override
    public int hashCode(){
        int hash = 0;
        hash += world.hashCode();
        hash += character.hashCode();
        hash += width * 733  + height * 547;
        hash += body.hashCode();
        return hash;
    }

    @Override
    public void setCurrentDirection(Direction dir){
        character.setCurrentDirection(dir);
    }

    @Override
    public Direction getDirection(){
        return character.getDirection();
    }

    @Override
    public Direction getLastDirection(){
        return character.getLastDirection();
    }

    @Override
    public boolean inAir() {
        return character.inAir();
    }
    
    public boolean isFollower(){
        return this.character.isFollower();
    }

    public void setAsFollower(){
        this.character.setAsFollower();
    }

    public void removeAsFollower(){
        this.character.removeAsFollower();
    }
}

