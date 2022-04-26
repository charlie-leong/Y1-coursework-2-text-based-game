/**
 * This class is part of the "World of Zuul" application.
 * "World of Zuul" is a text based adventure game.
 *
 * This class holds an enumeration of all command words known to the game.
 * It is used to recognise commands as they are typed in.
 *
 * @author  Michael Kölling and David J. Barnes and Charlotte Hoi Yi Leong k21062990
 * @version 2021/12/03
 */

public class CommandWords
{
    // a constant array that holds all valid command words
    private final String[] validCommands;

    /**
     * Constructor - initialise the command words.
     */
    public CommandWords()
    {
        this.validCommands = new String[]{
                "go", "quit", "help", "back", "take", "drop", "call", "use", "give"
        };
    }

    /**
     * Check whether a given String is a valid command word.
     * @return true if it is, false if it isn't.
     */
    public boolean isCommand(String aString)
    {
        for(int i = 0; i < validCommands.length; i++) {
            if(validCommands[i].equals(aString))
                return true;
        }
        return false;
    }

    /**
     * Print all valid commands to System.out.
     */
    public void showAll()
    {
        for(String command: validCommands) {
            System.out.print(command + "  ");
        }
        System.out.println();
    }
}
