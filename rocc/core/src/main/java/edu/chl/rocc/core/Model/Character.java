package edu.chl.rocc.core.model;

import edu.chl.rocc.core.m2phyInterfaces.ICharacter;


/**
 * A class for the playable characters.
 * Handeling data and setters/getters.
 *
 * @author Jenny Orell
 */
public class Character implements ICharacter {

    private int maxHealth = 100;
    private int healthPoints;
    private final String name;

    public Character(String name){
        this.setHP(maxHealth);
        this.name = name;
    }

    /*
    * Returns the character's health.
    */
    public int getHP(){
        return this.healthPoints;
    }

    /*
    * Set character's health with a chosen value.
    */
    public void setHP(int value){
        if(value < 0){
            System.out.print("Input value for health points cannot be negative.");
            // die-method??
        }

        if(value <= maxHealth && value >= 0){
            this.healthPoints = value;
        }else{
            this.healthPoints = maxHealth;
        }
    }

    /*
    * Increase character's health with a given value.
    */
    public void incHP(int value){
        this.setHP(this.getHP() + value);
    }


    /*
    * Decrease character's health with a given value.
    */
    public void decHP(int value){
        this.setHP(this.getHP() - value);
    }

    /*
    * Move the character in a given direction.
    */
    public void move(Direction dir){

    }

    public void moveFollower(int i){

    }


    /*
    * Make the character jump by changing its y-coordinate.
    */
    public void jump(){

    }

    public void moveFollower(Direction dir){

    }

    /*
    * Returns the x-coordinate of the character.
    */
    public float getX(){
        return 0;
    }

    /*
    * Returns the y-coordinate of the character.
    */
    public float getY(){
        return 0;
    }

    @Override
    public String getName() {
        return name;
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
        hash += maxHealth * 257;
        hash += name.hashCode();
        return hash;
    }

}
