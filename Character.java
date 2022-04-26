import java.util.ArrayList;
import java.util.Random;

/**
 * Class Character - a character in the game
 *
 * The class is part of the "World of Zuul" application
 * "World of Zuul" is a text based adventure game.
 *
 * A "Character" represents a character in the game that is capable of movement independent of the player's actions
 * The player also plays as a character that can be controlled through the parser.
 *
 * @author Charlotte Hoi Yi Leong k21062990
 * @version version 2021/12/03
 */
public class Character
{
    private final String characterClass;
    private String description;
    private String goal;
    private ArrayList<Item> storage;
    private int capacity;
    private String capacityDescription;
    private Room[] availableWanderRooms;
    private Room currentRoom; // this attribute is only relevant for NPCs to randomly travel their designated areas

    private boolean isPlayer;
    private boolean isInjured;
    private boolean isSeduced;

    Random random = new Random();

    /**
     * Constructor for objects of class Character
     */
    public Character(String type)
    {
        this.characterClass = type;
        this.availableWanderRooms = new Room[3];
        this.currentRoom = availableWanderRooms[0];
        setConstructor(type);

        setIsPlayer(false);
        setIsInjured(false);
        setIsSeduced(false);
    }

    /**
     * as each character class would have different entry qualities, this function sets the character constructor based on the character's type
     *
     * @param  type    the character type that the user chose at spawn
     */
    public void setConstructor(String type){

        if(type.equals("princess")){
            this.storage = new ArrayList<>();
            this.capacity = 2;
            this.goal = "escape";
            this.description = "frequently neglected princess that doesn't take responsibility for your actions";
            this.capacityDescription = "alas! your dress has no pockets, your total storage capacity is " + capacity + " units";
        }
        else if(type.equals("knight")){
            this.storage = new ArrayList<>();
            this.capacity = 6;
            this.goal = "find companionship";
            this.description = "supremely lonely knight that seeks validation from your parents";
            this.capacityDescription = "your shining armour is surprisingly roomy! your total storage capacity is " + capacity + " units";
        }
        else if(type.equals("dragon")){
            this.storage = new ArrayList<>();
            this.capacity = 0;
            this.goal = "find your treasure";
            this.description = "wildly misunderstood dragon that lacks proper communication skills";
            this.capacityDescription = "you have no thumbs! your total storage capacity is " + capacity + " units";
        }

    }

    /**
     * this function returns the character's class type
     * the three options are princess, knight, or dragon
     * @return the character's class type
     */
    public String getCharacterClass(){
        return characterClass;
    }

    /**
     * this function returns the character's corresponding description
     * @return the character's description
     */
    public String getCharacterDescription(){
        return description;
    }

    /**
     * this function returns the character's goal for the character setup
     * @return the character's goal
     */
    public String getCharacterGoal(){
        return goal;
    }

    /**
     *  this function returns dialogue describing the maximum weight of items the character can hold
     * @return the description of the character's storage capabilities
     */
    public String getCapacityDescription(){
        return capacityDescription;
    }

    /**
     * returns an array containing the rooms within a character's base region
     * this is used for the player to be able to teleport to their region and for NPCs to wander between rooms in their region
     * @return an array of three to four neighboring rooms
     */
    public Room[] getAvailableWanderRooms(){
        return availableWanderRooms;
    }

    /**
     * this function allows the character's regional area to be set from an external class where the rooms are initialised
     * @param rooms an array of three to four neighboring rooms
     */
    public void setAvailableWanderRooms(Room[] rooms) {
        this.availableWanderRooms = rooms;
    }

    /**
     * this function allows an NPC's current room to be set externally from another class
     * @param room the room that the NPC has moved into
     */
    public void setCurrentRoom(Room room){
        this.currentRoom = room;
    }

    /**
     * this function returns the room that a character is currently in
     * @return the character's current room
     */
    public Room getCurrentRoom(){
        return this.currentRoom;
    }

    /**
     * this function sets the boolean value for whether the player is playing as this character
     * @param state whether the character is the main player
     */
    public void setIsPlayer(boolean state){
        this.isPlayer = state;
    }


    /**
     * this function sets the boolean value for whether a charisma potion has been used against a character
     * this affects the character's ability to wander freely
     * @param state whether the character has been seduced
     */
    public void setIsSeduced(boolean state){
        this.isSeduced = state;
    }

    /**
     * this function allows the boolean value for whether a character is injured to be set externally
     * @param state whether the character has been injured
     */
    public void setIsInjured(boolean state){
        this.isInjured = state;
    }

    /**
     * this function returns a string containing all the items currently in a character's inventory
     * if the character has no items in their inventory, it will return an empty string
     * @return a string listing all items in the character's inventory
     */
    public String getCharacterInventory(){
        String output = "";
        for (Item item : storage) {
            output += item.getItemName() + " ";
        }
        return output;
    }

    /**
     * this function searches through a character's storage to find an item on the item's name
     * if there is no corresponding item in their storage, it will return null
     * @param itemName the inputted name of an item
     * @return the item (of Item class) containing the inputted item name
     */
    public Item convertItemNameToItem(String itemName){
        for(int i = 0; i< storage.size(); i++){
            if(storage.get(i).getItemName().equals(itemName)){
                return storage.get(i);
            }
        }
        return null;
    }

    /**
     * this function checks if a character is capable of storing an item, and adds it to their storage if possible
     * if a character does not have enough storage space, it will be either rejected or moved to their nest, depending on the character type
     * when adding an item to a character's storage, their available storage space left will be updated
     * @param item the item to be added to storage
     */
    public boolean addItemToStorage(Item item){
        if(isPlayer){
            if(this.characterClass.equals("dragon")){
                System.out.println(capacityDescription);
                System.out.println("that being said, don't worry, the item will be placed in your nest");
                availableWanderRooms[2].addItemToRoom(item);
            }
            else{
                if(capacity - item.getItemWeight() >= 0){
                    storage.add(item);
                    capacity -= item.getItemWeight();
                    System.out.println(item.getItemName() + " has been added to your storage!");
                    System.out.println("you have " + capacity + " units of storage left");
                    return true;
                }else{
                    System.out.println("oh no, looks like you don't have enough storage to pick this up");
                    System.out.println("the item is " + item.getItemWeight() + " units");
                    System.out.println("you have " + capacity + " units of storage left");
                    return false;
                }
            }
        }
        else{
            storage.add(item);
            if(characterClass.equals("dragon")) {
                availableWanderRooms[2].addItemToRoom(item);
            }
        }return true;
    }

    /**
     * this function allows a character to "drop" an item to remove it from their storage and leave it in the room they are currently in
     * if they do not have the item to be dropped, they will be notified and rejected
     * @param item the item to be removed
     */
    public void dropItem(Item item){

        if(storage.contains(item)){
            storage.remove(item);
            capacity += item.getItemWeight();
            System.out.println("you now have " + capacity + " units of storage left");
        }
        else{
            System.out.println("sorry, this item doesn't exist in your inventory");
        }
    }

    /**
     * this allows a character to "wander" to a random room in their designated region
     * if a character has been injured, they cannot move to another room
     * if a character has been seduced, they will always follow the player instead of wandering freely
     * @param playerCurrentRoom the room that the player is currently in
     */
    public void wander(Room playerCurrentRoom){
        if(!isInjured && !isSeduced){
            this.setCurrentRoom(availableWanderRooms[random.nextInt(availableWanderRooms.length)]);
        }
        if(isSeduced){
            this.setCurrentRoom(playerCurrentRoom);
        }
    }


}

