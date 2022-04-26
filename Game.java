import java.util.HashMap;
import java.util.Scanner;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 *  This class is the main class of the "World of Zuul" application.
 *  "World of Zuul" is a text based adventure game.
 *
 *  To play this game, create an instance of this class and call the "play"
 *  method.
 *
 *  This main class creates and initialises all the others: it creates all
 *  rooms, creates the parser and starts the game.  It also evaluates and
 *  executes the commands that the parser returns.
 *
 * @author  Michael Kölling and David J. Barnes and Charlotte Hoi Yi Leong k21062990
 * @version 2021.12.03
 */

public class Game
{
    private final Parser parser;
    private Room currentRoom;
    private Character player;
    private Room previousRoom;

    private boolean wonGame = false;
    private final HashMap<String, String> waysToWin;

    private Character supportingCharacter;
    private Character antagonist;

    private Random randInt;

    // create the game
    public Game()
    {
        parser = new Parser();
        waysToWin = new HashMap<>();
        randInt = new Random();
    }

    /**
     *  Main play routine that loops until end of the game.
     */
    public void play() throws InterruptedException {
        printWelcome();
        setUpCharacter();
        createRooms();
        setUpWaysToWin();

        // Enter the main command loop.  Here we repeatedly read commands and execute them until the game is over.
        boolean finished = false;
        while (! finished && !wonGame) {
            Command command = parser.getCommand();
            finished = processCommand(command);
        }
        System.out.println("thanks for playing! i hope this was a fulfilling experience");
    }

    /**
     * Create all the rooms and link their exits together to create the internal map
     * Creates and assigns objects to their respective rooms
     */
    private void createRooms() {
        // create the rooms
        Room home = new Room("the knight's childhood home", "");
        Room townSquare = new Room("the center of town and the entrance to the forest",
                "while you are free to make your own choices, it is strongly advised you stock up on supplies before any adventure! " +
                        "\nbut the forest down south looks so intriguing...");
        Room store = new Room("the local convenience store",
                "given your current predicament, the shopkeeper is willing to look past any potential theft");
        Item sword, knife, charisma;
        sword = new Item("sword", 4, "a sword", "injure");
        knife = new Item("knife", 2, "a knife", "injure");
        charisma = new Item("potion", 1, "a mysterious looking potion labelled as charisma", "seduce");
        store.addItemToRoom(sword);
        store.addItemToRoom(knife);
        store.addItemToRoom(charisma);

        Room forestEdge = new Room ("the edge of the forest", "");
        Room treePatch = new Room ("a suspicious patch of trees",
                "you see a tower so tall it couldn't possibly be approved by an engineer, " +
                        "\nbut what is that? another suspicious patch of trees further south?");
        Room nest = new Room ("a surprisingly well organised dragon nest", "");
        Item treasure;
        treasure = new Item("treasure", 3, "the dragon's treasure", "repair");
        nest.addItemToRoom(treasure);

        Room mainRoom = new Room ("the tower's main room",
                "you see a very large window, perfect for summoning people");
        Room kitchen = new Room ("the tower's kitchen", "");
        Room bedroom = new Room ("the tower's bedroom",
                "you're too anxious to rest, but there is a funky looking cupboard to your east");
        Room cupboard = new Room ("a deceptively roomy cupboard",
                "this is where hidden things stay hidden, but you can't help but notice a strange vortex?");
        Item egg, chest;
        egg = new Item("egg", 2, "an unnervingly large egg", "repair");
        chest = new Item("chest", 8, "a locked treasure chest", "repair");
        kitchen.addItemToRoom(egg);
        kitchen.addItemToRoom(knife);
        cupboard.addItemToRoom(chest);
        nest.addItemToRoom(egg);

        // initialise room exits
        home.setExit("west", townSquare);
        townSquare.setExit("east", home);
        townSquare.setExit("west", store);
        townSquare.setExit("south", forestEdge);
        store.setExit("east", townSquare);

        forestEdge.setExit("north", townSquare);
        forestEdge.setExit("south", treePatch);
        treePatch.setExit("north", forestEdge);
        treePatch.setExit("up", mainRoom); //does not have down exit as tower cannot be exited naturally
        treePatch.setExit("south", nest);
        nest.setExit("north", treePatch);

        mainRoom.setExit("north", bedroom);
        mainRoom.setExit("south", kitchen);
        bedroom.setExit("south", mainRoom);
        bedroom.setExit("east", cupboard);
        cupboard.setExit("west", bedroom);
        kitchen.setExit("north", mainRoom);
        cupboard.setExit("east", kitchen); // it will not go to the kitchen

        //initialise the regions where the NPCs can wander
        Room[] town = new Room[]{home, townSquare, store};
        Room[] forest = new Room[]{forestEdge, treePatch, nest};
        Room[] tower = new Room[]{mainRoom, bedroom, cupboard, kitchen};

        //set up the locations regarding how they relate to the player and NPCs
        if(player.getCharacterClass().equals("princess")){
            currentRoom = mainRoom;
            previousRoom = mainRoom;
            player.setAvailableWanderRooms(tower);
            supportingCharacter.setAvailableWanderRooms(town);
            antagonist.setAvailableWanderRooms(forest);
        }else if (player.getCharacterClass().equals("dragon")){
            currentRoom = nest;
            previousRoom = nest;
            player.setAvailableWanderRooms(forest);
            supportingCharacter.setAvailableWanderRooms(town);
            antagonist.setAvailableWanderRooms(tower);
        }else{
            currentRoom = home;
            previousRoom = home;
            player.setAvailableWanderRooms(town);
            supportingCharacter.setAvailableWanderRooms(tower);
            antagonist.setAvailableWanderRooms(forest);
        }
        System.out.println("you are starting in " + currentRoom.getShortDescription());
        System.out.println(currentRoom.getExitString());
    }

    /**
     * Print out the opening message for the user
     */
    private void printWelcome()
    {
        System.out.println("\nwelcome to our classic fairytale, where your bravery, strength, and emotional maturity will be tested");
        System.out.println("Type 'help' if you need help.\n");

    }

    /**
     * This function allows the user to choose their character type and subsequent plot line.
     * Any necessary information about the character is also displayed
     */
    private void setUpCharacter (){
        System.out.println("Please select your character: princess, knight, dragon");
        Scanner userInput = new Scanner(System.in);
        String charClass = userInput.nextLine().toLowerCase();
        while(!charClass.equals("princess") && !charClass.equals("knight") && !charClass.equals("dragon")){
            System.out.println("sorry that is not a valid character class :(");
            System.out.println("the options are - \"princess\", \"knight\", \"dragon\"");
            charClass = userInput.nextLine();
        }
        this.player = new Character(charClass);
        System.out.println("welcome! you are a "+ player.getCharacterDescription() +
                "\nyour goal is to " + player.getCharacterGoal());

        player.setIsPlayer(true);

        if(player.getCharacterClass().equals("princess")){
            System.out.println("at the moment, you are trapped in an unnecessarily high tower by a dragon");
            supportingCharacter = new Character("knight");
            antagonist = new Character("dragon");
        }else if(player.getCharacterClass().equals("knight")){
            System.out.println("presently, your family is attacking your masculinity on the basis of not having yet " +
                    "found a partner to settle down with. \nbut what is that? news that a local princess needs rescuing...");
            supportingCharacter = new Character("princess");
            antagonist = new Character("dragon");
        }else if(player.getCharacterClass().equals("dragon")){
            supportingCharacter = new Character("knight");
            antagonist = new Character("princess");
            System.out.println("currently, there is a princess locked in a tower that has legally been designated as your property");
        }
    }

    /**
     * Given a command, process (that is: execute) the command.
     * @param command The command to be processed.
     * @return true If the command ends the game, false otherwise.
     */
    private boolean processCommand(Command command) throws InterruptedException {
        boolean wantToQuit = false;
        if(command.isUnknown()) {
            System.out.println("sorry, i'm not sure what you mean :(");
            return false;
        }

        String commandWord = command.getCommandWord();


        if(commandWord.equals("help")){
            printHelp();
        }else if(commandWord.equals("go")){
            goRoom(command);
        }else if(commandWord.equals("quit")){
            wantToQuit = quit(command);
        }else if(commandWord.equals("take")){
            takeItem(command);
        }else if(commandWord.equals("drop")){
            dropItem(command);
        }else if(commandWord.equals("call")){
            callCharacter(command);
        }else if(commandWord.equals("give")){
            giveItem(command);
        }else if(commandWord.equals("use")){
            deployItem(command);
        }

        // else command not recognised.
        return wantToQuit;
    }

    // implementations of user commands:
    /**
     * Print out some help information.
     * Here we print a cryptic message and a list of the command words
     * it also prints the character's maximum storage capacity and a list of the items they currently have in their storage
     */
    private void printHelp()
    {
        System.out.println("are you lost? lonely? desolate? \nwell then here are your command words:");
        parser.showCommands();
        System.out.println("\n" + player.getCapacityDescription());

        if(player.getCharacterInventory().equals("")){
            System.out.println("you have no items in your inventory");
            return;
        }
        System.out.println("the items in your inventory are: " + player.getCharacterInventory());
    }

    /**
     * Try to go in to a direction. If there is an exit, enter the new room, otherwise print an error message.
     */
    private void goRoom(Command command) throws InterruptedException {
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know where to go...
            System.out.println("wait, where are we going?");
            return;
        }
        String direction = command.getSecondWord();

        // implementing the teleportation to a random room in the player's range
        if(currentRoom.getShortDescription().equals("a deceptively roomy cupboard")
                && direction.equals("east")){
            if(supportingCharacter.getCurrentRoom() == currentRoom){ // if the princess and knight teleport together, they win
                if(player.getCharacterClass().equals("princess")){
                    displayWinnerMessage("princess teleport");
                }if(player.getCharacterClass().equals("knight")){
                    displayWinnerMessage("knight teleport");
                }
                return;
            }

            supportingCharacter.wander(currentRoom);
            antagonist.wander(currentRoom);

            previousRoom = currentRoom;
            int roomsNumber = player.getAvailableWanderRooms().length;
            currentRoom = player.getAvailableWanderRooms()[randInt.nextInt(roomsNumber)];
            System.out.println("ooh something is happening");
            TimeUnit.SECONDS.sleep(3);
            System.out.println("whoa that was wild, you're now in " + currentRoom.getShortDescription());
            if(player.getCharacterClass().equals("princess")){
                System.out.println("looks like you'll need to be in the room with someone who can teleport out of the tower");
            }
            return;
        }

        Room nextRoom = currentRoom.getExit(direction);
        if (nextRoom == null) {
            System.out.println("sorry buddy, that direction doesn't lead anywhere");
        }
        else {
            previousRoom = currentRoom;
            currentRoom = nextRoom;
            System.out.println(currentRoom.getLongDescription());
            System.out.println(currentRoom.getDialogue());
            if(currentRoom.listItemsInRoom() != null){
                System.out.println("the items in this room are " + currentRoom.listItemsInRoom());
            }
            if(supportingCharacter.getCurrentRoom() == currentRoom ){
                System.out.println(supportingCharacter.getCharacterClass() + " is also in this room");
            }
            if(antagonist.getCurrentRoom() == currentRoom){
                System.out.println(antagonist.getCharacterClass() + " is also in this room, look out!");
            }
        }
    }

    /**
     *
     * @param command the full command, where information about the action can be extracted
     */
    public void takeItem(Command command){
        String itemName = command.getSecondWord();
        if(currentRoom.itemExistsInThisRoom(itemName)){
            Item item = currentRoom.getItemFromString(itemName);

            if (player.getCharacterClass().equals("knight") && item.getItemName().equals("treasure")) {
                System.out.println("the dragon has caught you trying to steal their treasure!\n you've been locked in the tower with the princess");
                previousRoom = currentRoom;
                currentRoom  = supportingCharacter.getAvailableWanderRooms()[0];
                System.out.println(currentRoom.getLongDescription());
                return;
            }

            player.addItemToStorage(item);
            if(player.addItemToStorage(item)){
                currentRoom.removeItemFromRoom(item);
                System.out.println("this room now contains: " + currentRoom.listItemsInRoom());
            }
        }
        else{
            System.out.println("are you sure that item is in this room?");
        }
        if(player.getCharacterClass().equals("dragon") &&
                player.getAvailableWanderRooms()[2].itemExistsInThisRoom("egg") &&
                player.getAvailableWanderRooms()[2].itemExistsInThisRoom("chest") &&
                player.getAvailableWanderRooms()[2].itemExistsInThisRoom("treasure")){
            displayWinnerMessage("treasure");
        }

    }

    public void dropItem(Command command){
        String itemName = command.getSecondWord();
        Item item = player.convertItemNameToItem(itemName);
        if(item != null){
            currentRoom.addItemToRoom(item);
            player.dropItem(player.convertItemNameToItem(itemName));
            System.out.println(currentRoom.listItemsInRoom());
        }
        else{
            System.out.println("sorry that item was not recognised. try again?");
        }
    }

    /**
     *
     * @param command the entire command that was entered in the terminal
     */
    public void callCharacter(Command command) throws InterruptedException {
        if(!command.hasSecondWord()){
            System.out.println("who are we calling?");
            return;
        }
        Character subject = getCharacterFromString(command.getSecondWord());
        if(subject !=null){
            System.out.println("wait a second...");
            TimeUnit.SECONDS.sleep(3); //user timer delay before teleporting to the current room
            subject.setCurrentRoom(currentRoom);
            System.out.println(subject.getCharacterClass() + " is now in " + currentRoom.getShortDescription()+ " with you");
        }

        if(player.getCharacterClass().equals("dragon") && supportingCharacter.getCurrentRoom() == currentRoom && antagonist.getCurrentRoom() == currentRoom){
            displayWinnerMessage("friendship");
        }

    }

    /**
     * this function allows the player to give another character an item from their storage
     * @param command the entire inputted command, where the subject and object can be extracted
     */
    private void giveItem(Command command){
        if((!command.hasSecondWord() || !command.hasThirdWord())
                || command.getSecondWord()==null || command.getThirdWord()==null){
            System.out.println("give what?? to whom??");
            return;
        }

        Item object = player.convertItemNameToItem(command.getSecondWord());
        Character subject = getCharacterFromString(command.getThirdWord());

        if(subject == null || object == null){
            System.out.println("your command seems a bit off, could you check and try again?");
            return;
        }


        if(subject.getCurrentRoom() == currentRoom){
            player.dropItem(object);
            subject.addItemToStorage(object);
            System.out.println("item successfully given!");

            if(player.getCharacterClass().equals("princess") && object==subject.convertItemNameToItem("egg")
                    && subject.getCharacterClass().equals("dragon")){
                displayWinnerMessage("egg");
            }
        }
        else{
            System.out.println("sorry but "+ subject.getCharacterClass() +" isn't in this room right now");
        }
    }

    /**
     * "Quit" was entered. Check the rest of the command to see
     * whether we really quit the game.
     * @return true, if this command quits the game, false otherwise.
     */
    private boolean quit(Command command)
    {
        if(command.hasSecondWord()) {
            System.out.println("quit what?");
            return false;
        }
        else {
            return true;  // signal that we want to quit
        }
    }

    /**
     * allows the user to go back to the previous room they were in
     */
    private void goBack(){ //MAKE THIS A STACK HHHH
        Room temp = currentRoom;
        currentRoom = previousRoom;
        previousRoom = temp;
        System.out.println("you have gone back to "+ currentRoom.getShortDescription() + "!");
    }

    /**
     * allows the player to use an item as a means of attacking
     * if referring to items that cannot be used, game will suggest giving it to a character instead
     * @param command the entire command input by the user, where information about the action can be extracted
     */
    private void deployItem(Command command){
        if((!command.hasSecondWord() || !command.hasThirdWord())
                || command.getSecondWord()==null || command.getThirdWord()==null){
            System.out.println("this command seems a bit incomplete! what are we using against who?");
            return;
        }
        Item object = player.convertItemNameToItem(command.getSecondWord());
        Character subject = getCharacterFromString(command.getThirdWord());

        if(subject == null || object == null){
            System.out.println("your command seems a bit off, could you check and try again?");
            return;
        }

        if(object.getAbility().equals("repair")){
            System.out.println("this item cannot be deployed. did you mean to give it to someone? *wink*");
        }
        else if(object.getAbility().equals("injure")){
            subject.setIsInjured(true);
            System.out.println("you have injured "+ subject.getCharacterClass() + "! they will now be stuck in this location");
        }
        else if(object.getAbility().equals("seduce")){
            subject.setIsSeduced(true);
            System.out.println("you have successfully charmed "+ subject.getCharacterClass());
            player.dropItem(object);
            if(subject.getCharacterClass().equals("dragon") && player.getCharacterClass().equals("knight")){
                displayWinnerMessage("companionship");
            }

        }
    }


    /**
     * this function converts the character's class name from the parser to reference the actual character
     * @param classType variable of String type that the user references
     * @return variable of Character class that
     */
    private Character getCharacterFromString(String classType){
        if(classType.equals(supportingCharacter.getCharacterClass())){
            return supportingCharacter;
        }else if(classType.equals(antagonist.getCharacterClass())){
            return antagonist;
        }
        System.out.println("whom?");
        return null;
    }

    /**
     * this function sets up the hashmap for all the possible ways to win the game
     */
    private void setUpWaysToWin(){
        waysToWin.put("egg", "you have returned the dragon egg to it's rightful owner. \n" +
                "you have completed your goal by escaping your own toxic mindset and freeing " +
                "yourself from internalised guilt. ");
        waysToWin.put("princess teleport", "you have escaped the tower with the help of a friendly passerby knight");
        waysToWin.put("companionship", "you have found companionship with the dragon and emotional fulfilment " +
                "knowing your judgemental parents can't complain about your dragon wife ");
        waysToWin.put("knight teleport", "you have helped the princess escape and won the approval of your parents");
        waysToWin.put("friendship", "you have found the true treasure in life- friendship :)");
        waysToWin.put("treasure", "you have successfully gathered back all of your treasure");
    }

    /**
     * this function displays a message corresponding to how the player has won the game, and ends the game
     * @param means the key to the hashmap, referring to how the player has won
     */
    private void displayWinnerMessage(String means){
        System.out.println("\n\ncongratulations, you have won!");
        System.out.println(waysToWin.get(means));
        wonGame = true;
    }
}
