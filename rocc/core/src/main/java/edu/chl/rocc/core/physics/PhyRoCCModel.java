package edu.chl.rocc.core.physics;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import edu.chl.rocc.core.model.Direction;
import edu.chl.rocc.core.m2phyInterfaces.IRoCCModel;
import edu.chl.rocc.core.model.Level;
import org.jbox2d.collision.shapes.ChainShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;

/**
 * Created by Joel on 2015-05-03.
 */
public class PhyRoCCModel implements IRoCCModel {

    private IRoCCModel model;

    @Override
    public void aim(int x, int y) {
        this.model.aim(x, y);
    }

    @Override
    public void constructWorld(TiledMap tMap) {
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
                    model.getLevel().addBlock(bDef, fDef);
                }
            }
        }
    }

    @Override
    public void moveSideways(Direction dir) {
        this.model.moveSideways(dir);
    }

    @Override
    public void jump() {
        this.model.jump();
    }

    @Override
    public float getCharacterXPos() {
        return this.model.getCharacterXPos();
    }

    @Override
    public float getCharacterYPos() {
        return this.model.getCharacterYPos();
    }

    @Override
    public Level getLevel() {
        return this.model.getLevel();
    }

    @Override
    public void updateWorld(float dt) {
        this.model.updateWorld(dt);
    }
}
