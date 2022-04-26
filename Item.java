/**
 * Class Item - an item in the game
 *
 * The class is part of the "World of Zuul" application
 * "World of Zuul" is a very text based adventure game.
 *
 * An "Item" represents an object in the game that is incapable of movement outside of being moved by the player
 * Items have set locations that are assigned to either a room or a character's inventory
 * Each item also has a designated ability that can be utilized by the player
 *
 * @author Charlotte Hoi Yi Leong k21062990
 * @version version 2021/12/03
 */
public class Item
{
    private final String name;
    private final int weight;
    private final String description;
    private final String ability; // injure, seduce, repair
    // maybe add damage?? or description??? idk we'll figure it out babe

    /**
     * Constructor for objects of class Item
     */
    public Item(String n, int w, String d, String a)
    {
        this.name = n;
        this. weight = w;
        this.description = d;
        this.ability = a;
    }

    /**
     * @return    the value of the item name
     */
    public String getItemName(){
        return this.name;
    }

    /**
     *
     * @return  the weight of the item
     */
    public int getItemWeight(){
        return this.weight;
    }

    /**
     * @return  the description of the item
     */
    public String getItemDescription(){
        return this.description;
    }

    /**
     * @return  the ability of the item
     */
    public String getAbility(){
        return this.ability;
    }


}
