import java.util.Set;
import java.util.HashMap;
import java.util.Random;
import java.util.ArrayList;

/**
 * Class Room - a room in an adventure game.
 *
 * This class is part of the "World of Zuul" application.
 * "World of Zuul" is a text based adventure game.
 *
 * A "Room" represents one location in the scenery of the game.  It is
 * connected to other rooms via exits.  For each existing exit, the room
 * stores a reference to the neighboring room.
 *
 * @author  Michael Kölling and David J. Barnes and Charlotte Hoi Yi Leong k21062990
 * @version 2021/12/03
 */

public class Room
{
    private final String description;
    private HashMap<String, Room> exits;
    private ArrayList <Item> items;
    private final String dialogue; // text that appears when a character enters a room, no purpose aside from providing context

    /**
     * Create a room described "description". Initially, it has no exits.
     * "description" is something like "a kitchen" or "an open court yard".
     * @param description The room's description.
     */
    public Room(String description, String dialogue)
    {
        this.description = description;
        exits = new HashMap<>();
        this.items = new ArrayList<>();
        this.dialogue = dialogue;
    }

    /**
     * Define an exit from this room.
     * @param direction The direction of the exit.
     * @param neighbor  The room to which the exit leads.
     */
    public void setExit(String direction, Room neighbor)
    {
        exits.put(direction, neighbor);
    }

    /**
     * @return The short description of the room
     * (the one that was defined in the constructor).
     */
    public String getShortDescription() {
        return description;
    }

    /**
     * Return a description of the room in the form:
     *     You are in the kitchen.
     *     Paths: north west
     * @return A long description of this room
     */
    public String getLongDescription()
    {
        String[] introPhrases = {"you are currently in ", "you have arrived at ", "wow! you're in ",
                "you are now at ", "oh boy, you've found "};
        Random randInt = new Random();
        return introPhrases[randInt.nextInt(introPhrases.length)] + description + ".\n" + getExitString();
    }

    /**
     * Return a string describing the room's exits, for example
     * "Exits: north, west".
     * @return Details of the room's exits.
     */
    public String getExitString()
    {
        String returnString = "from here you can go";
        Set<String> keys = exits.keySet();
        for(String exit : keys) {
            returnString += " " + exit;
        }
        return returnString;
    }

    /**
     * Return the room that is reached if we go from this room in direction
     * "direction". If there is no room in that direction, return null.
     * @param direction The exit's direction.
     * @return The room in the given direction.
     */
    public Room getExit(String direction){
        return exits.get(direction);
    }

    /**
     * Return the game commentary associated with a room to provide in-game context
     * if there is no assigned dialogue, it will return an empty string
     * @return the dialogue set for the room
     */
    public String getDialogue(){
        return this.dialogue;
    }

    /**
     * Adds the designated item to the room's contents
     * @param item The item to be added to the room
     */
    public void addItemToRoom (Item item){
        items.add(item);
    }

    /**
     * Checks whether there is an item in the room with the inputted item name
     * @param itemName the name of the item to be checked
     * @return the boolean value of whether the item is in the room
     */
    public boolean itemExistsInThisRoom(String itemName){
        for(int i = 0; i< items.size(); i++){
            if(items.get(i).getItemName().equals(itemName)){
                return true;
            }
        }
        return false;
    }

    /**
     * Converts the inputted item name into an Item that can be referenced
     * If there are no items that correspond to that name, return null
     * @param itemName the name of the item being converted
     * @return an object of Item class with the matching item name
     */
    public Item getItemFromString(String itemName){

        for(int i = 0; i< items.size(); i++){
            if(items.get(i).getItemName().equals(itemName)){
                return items.get(i);
            }
        }
        return null;
    }

    /**
     * Removes the item from the room
     * If item is not present in the room, the player is notified
     * @param item the item to be removed
     */
    public void removeItemFromRoom (Item item){
        if(items.contains(item)){
            items.remove(item);
        }
        else{
            System.out.println("sorry this item doesn't exist in this room");
        }
    }

    /**
     * Creates a list containing all the items in the room
     * Will return null if no items are present in the room
     * @return String listing all items in the room
     */
    public String listItemsInRoom(){
        if(items.isEmpty()){
            return null;
        }

        String output = "";
        for(int i = 0; i< items.size(); i++){
            if(items.get(i) != null) {
                output += items.get(i).getItemDescription() + " ";
            }
        }
        return output;
    }
}