package edu.chl.rocc.core.model;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import edu.chl.rocc.core.m2phyInterfaces.IRoCCModel;
import org.jbox2d.collision.shapes.ChainShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;


/**
 * A class handeling the game model.
 *
 * Created by Yen on 2015-04-22.
 */
public class RoCCModel implements IRoCCModel {

    private Level level;
    private Player player;

    public RoCCModel(){
        level = new Level();
        player = new Player(level.getWorld());
    }

    public void aim(int x, int y){

    }

    // Creates a logical box2d map mimicing the tiled-map
    public void constructWorld(TiledMap tMap){
        // Get the layer with information about the solid ground
        TiledMapTileLayer tileLayer = (TiledMapTileLayer)tMap.getLayers().get("ground");

        // Create a tile for each block on the map
        for (int row = 0; row < tileLayer.getHeight(); row++){
            for (int col = 0; col < tileLayer.getWidth(); col++){
                TiledMapTileLayer.Cell cell = tileLayer.getCell(col, row);

                // If there is a tile at the position
                if (cell != null && cell.getTile() != null){

                    // Create a body definiion
                    BodyDef bDef = new BodyDef();
                    bDef.type = BodyType.STATIC;
                    bDef.position.set(32 * (col + 0.5f), 32 * (row + 0.5f));

                    // And a fixture definition
                    ChainShape cs = new ChainShape();
                    Vec2[] v = new Vec2[5];
                    v[0] = new Vec2( -16f, -16f);
                    v[1] = new Vec2( -16f,  16f);
                    v[2] = new Vec2(  16f,  16f);
                    v[3] = new Vec2(  16f, -16f);
                    v[4] = new Vec2( -16f, -16f);
                    cs.createChain(v, 5);

                    FixtureDef fDef = new FixtureDef();
                    fDef.friction = 0;
                    fDef.shape = cs;
                    fDef.filter.categoryBits = 2;  // As Character variable BIT_Ground
                    fDef.filter.maskBits = 4;      // As Character variable BIT_Body

                    // Then let the level create the block in the world
                    level.addBlock(bDef, fDef);
                }
            }
        }
    }

    public void moveSideways(Direction dir){
        player.move(dir);
    }

    public void jump() { player.jump(); }

    public float getCharacterXPos(){
        return player.getCharacterXPos();
    }

    public float getCharacterYPos(){
        return player.getCharacterYPos();
    }

    public float getFollowerXPos(int i){
        return player.getFollowerXPos(i);
    }

    public float getFollowerYPos(int i){
        return player.getFollowerYPos(i);
    }

    public Level getLevel(){
        return level;
    }

    public void updateWorld(float dt){level.updateWorld(dt); }


}
