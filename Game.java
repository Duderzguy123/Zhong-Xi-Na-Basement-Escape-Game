/**
 *  This class is the main class of the "World of Zuul" application. 
 *  "World of Zuul" is a very simple, text based adventure game.  Users 
 *  can walk around some scenery. That's all. It should really be extended 
 *  to make it more interesting!
 * 
 *  To play this game, create an instance of this class and call the "play"
 *  method.
 * 
 *  This main class creates and initialises all the others: it creates all
 *  rooms, creates the parser and starts the game.  It also evaluates and
 *  executes the commands that the parser returns.
 * 
 * @author  Michael Kölling and David J. Barnes
 * @version 2016.02.29
 */

//Imports
import java.util.ArrayList;
import java.util.Scanner;

public class Game 
{
    private Parser parser;
    private Room currentRoom;
    private Player player;

    
        
    /**
     * Create the game and initialise its internal map.
     */
    public Game() 
    {
        createRooms();
        parser = new Parser();
        player = new Player();
      
    }

    /**
     * Create all the rooms and link their exits together.
     */
    private void createRooms()
    {
        Room bedroom, closet, clockRoom, livingRoom, kitchen, bathroom, freezerRoom,         laundryRoom;
      
        // create the rooms
        bedroom = new Room("in the bedroom");
        closet = new Room("in a spacious closet");
        clockRoom = new Room("in a room containing a giant clock");
        livingRoom = new Room("in a wide and furnished living room");
        kitchen = new Room("in a sleek looking kitchen");
        bathroom = new Room("in a creepily clean bathroom");
        freezerRoom = new Room("in a room with a big freezer");
        laundryRoom = new Room("in a room with a laundry machine");
        

        //Add items
        freezerRoom.addItem("Bing Chilling", 5, 1);
        laundryRoom.addItem("Winnie the Pooh Teddy Bear", 5, 2);
        closet.addItem("Spy Balloon", 50, 3);
        bedroom.addItem("Social Credit Card", 2, 4);
        kitchen.addItem("Chopsticks", 2, 5);
        bathroom.addItem("Tecent Soap", 7, 6);
        clockRoom.addItem("Alarm Clock", 8, 7);
        livingRoom.addItem("Couch", 50, 8);
      
        // initialise room exits
      
        //Exit Room Exits
        clockRoom.setExit("south", bedroom);
      
        // Bedroom Exits  
        bedroom.setExit("east", closet);
        bedroom.setExit("south", livingRoom);
        bedroom.setExit("north", clockRoom);
      
        //Closet Exits
        closet.setExit("west", bedroom);
        closet.setExit("south", kitchen);

        //Living Room Exits
        livingRoom.setExit("north", bedroom); 
        livingRoom.setExit("west", laundryRoom); 
        livingRoom.setExit("south", bathroom); 
        livingRoom.setExit("east", kitchen); 
      
        //Bathroom exits
        bathroom.setExit("north", livingRoom);

        //Laundry Room exits
        laundryRoom.setExit("east", livingRoom);

        //Kitchen exits
        kitchen.setExit("east", freezerRoom);
        kitchen.setExit("west", livingRoom);
        kitchen.setExit("north", closet);

        //Freezer exits
        freezerRoom.setExit("west", kitchen);

        currentRoom = clockRoom;  // start game in clockRoom
    }

    /**
     *  Main play routine.  Loops until end of play.
     */
    public void play() 
    {            
        printWelcome();
        
        // Enter the main command loop.  Here we repeatedly read commands and
        // execute them until the game is over.
                
        boolean finished = false;
        while (! finished) {
            Command command = parser.getCommand();
            
            finished = processCommand(command);
        }
        System.out.println("Thank you for playing!  Good bye.");
    }

    /**
     * Print out the opening message for the player.
     */
    private void printWelcome()
    {
        System.out.println();
        System.out.println("Welcome to Zhong Xi Na's basement!");
        System.out.println("You need to escape! To escape, you need to collect");
        System.out.println("a social credit card, a bing chilling, a spy balloon, ");
        System.out.println("and a Winnie the Pooh teddy bear!");
        System.out.println("Be careful however, as you have a limited weight limit for items!");
        System.out.println();
        System.out.println("Item Ids Are: ");
        System.out.println("1- Bing Chilling");
        System.out.println("2- Winnie the Pooh Teddy Bear");
        System.out.println("3- Spy Balloon");
        System.out.println("4- Social Credit Card");
        System.out.println("5- Chopsticks");
        System.out.println("6- Tecent Soap");
        System.out.println("7- Alarm Clock");
        System.out.println("8- Couch");
        System.out.println();
        System.out.println("Type help for list of commands");
        System.out.println(currentRoom.getLongDescription());
    }

    /**
     * Given a command, process (that is: execute) the command.
     * @param command The command to be processed.
     * @return true If the command ends the game, false otherwise.
     */
    private boolean processCommand(Command command) 
    {
        boolean wantToQuit = false;

        if(command.isUnknown()) {
            System.out.println("I don't know what you mean...");
            return false;
        }

        String commandWord = command.getCommandWord();
        if (commandWord.equals("help")) {
            printHelp();
        }
        else if (commandWord.equals("go")) {
            goRoom(command);
        }
        else if (commandWord.equals("quit")) {
            wantToQuit = quit(command);
        }
        else if (commandWord.equals("look")) {
            look(command);
        }
        else if (commandWord.equals("grab")){
          grab(command);
        }
        else if (commandWord.equals("drop")){
          drop(command);
        }
        else if (commandWord.equals("escape")){
          if (escape(command) == true){
              System.out.println("You have Escaped Zhong Xi Na's Basement!");
              wantToQuit = quit(command);
          }
          else{
            System.out.println("You don't have the necessary items to escape!");
          }
          
        }
        // else command not recognised.
        return wantToQuit;
    }

    // implementations of user commands:

    /**
     * Print out some help information.
     * Here we print some stupid, cryptic message and a list of the 
     * command words.
     */
    private void printHelp() 
    {
        System.out.println("You are lost. You are alone. You wander");
        System.out.println("around Zhong Xi Na's basement.");
        System.out.println();
        System.out.println("Your command words are:");
        parser.showCommands();
    }

    /** 
     * Try to in to one direction. If there is an exit, enter the new
     * room, otherwise print an error message.
     */
    private void goRoom(Command command) 
    {
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know where to go...
            System.out.println("Go where?");
            return;
        }

        String direction = command.getSecondWord();

        // Try to leave current room.
        Room nextRoom = currentRoom.getExit(direction);

        if (nextRoom == null) {
            System.out.println("There is no door!");
        }
        else {
            currentRoom = nextRoom;
            System.out.println(currentRoom.getLongDescription());
        }
    }
  
    /** 
     * Looks around Room and checks if there are any objects. If so, prints the objects.
     * If there are no objects, prints "There are no items in this room to grab!"
     */
    private void look(Command command){ 
        if (currentRoom.checkItem() == true)
        {
          ArrayList<Item> temp = new ArrayList<Item>();
          temp = currentRoom.getArray();
          System.out.print("The Items in this Room are: ");
          for(int i = 0; i < temp.size(); i++){
            System.out.print(temp.get(i).getName() + " ");
          }
          System.out.println();
        }
        else
        {
          System.out.println("There are no items in this room to grab!"); 
        }
        
    }
  
  /** 
     * Trys to drop an item in the room. If player has no items, 
     * print "You have no items to drop!".
     */
    private void drop(Command command){
      Scanner input = new Scanner(System.in);
      int tempInt = 0;
      ArrayList<Item> temp = new ArrayList<Item>();
      temp = player.getPlayerItems();
      if (temp.size() == 0){
        System.out.println("You have no items to drop!");
      }
      else{
        System.out.print("Here are the items you have: ");
        for (int i = 0; i < temp.size(); i++){
          System.out.print(temp.get(i).getName() + " ");
        }
        System.out.println();
        System.out.print("Please enter the id in exactly what item to drop: ");
        tempInt = input.nextInt();
        
        for (int i = 0; i < temp.size(); i++){
          if (temp.get(i).getId() == tempInt){
                currentRoom.addItem(temp.get(i).getName(),temp.get(i).getItemWeight(), temp.get(i).getId());
                player.losePlayerWeight(temp.get(i).getItemWeight());
                player.removePlayerItem(i);
          }
        } 
       
      
      
      }
    }
  
  /** 
     * Trys to pick up an item in a room. If there is no item, 
     * print  "there are no items in this room".
     * If player is over weight limit, 
     */
    private void grab(Command command){
      Scanner input = new Scanner(System.in);
      int tempInt = 0;
      ArrayList<Item> temp = new ArrayList<Item>();
      ArrayList<Item> temp_2 = new ArrayList<Item>();
      temp = currentRoom.getRoomItems();
      temp_2 = player.getPlayerItems();
      
      if (player.getCurrentWeight() > player.getWeightLimit()){
        System.out.println("You cannot grab any more items or you would go over weight limit, please drop an item");
      }
      else if(currentRoom.checkItem() == false){
        System.out.println("There are no items in this room!"); 
      }
      else{
        System.out.print("Here are the items you can pick up: ");
        for (int i = 0; i < temp.size(); i++){
          System.out.print(temp.get(i).getName() + " ");
        }
        System.out.println();
        System.out.print("Please enter the id of item that you want to grab exactly: ");
        tempInt = input.nextInt();
        
        for (int i = 0; i < temp.size(); i++){
          if (temp.get(i).getId() == tempInt){
            player.addPlayerItem(temp.get(i).getName(),temp.get(i).getItemWeight(), temp.get(i).getId());
            player.addPlayerWeight(temp.get(i).getItemWeight());
          
            currentRoom.removeRoomItem(temp.get(i).getId());
            System.out.println("item grabbed, continue commands");
          }
        }
        
      }
    
    
    }
  
  /** 
     * Checks to see if player has necessary items to escape
     * @return true if player has neccessary items to escape, false if not
     */
    private boolean escape(Command command){
      
      boolean hasCard = false;
      boolean hasBingChilling = false;
      boolean hasBalloon = false;
      boolean hasBear = false;
      ArrayList<Item> temp = new ArrayList<Item>();
      temp = player.getPlayerItems();
      for(int i = 0; i < player.getPlayerItems().size(); i++){
        if (temp.get(i).getName() == "Bing Chilling"){
          hasBingChilling = true;
        }
        if (temp.get(i).getName() == "Winnie the Pooh Teddy Bear"){
          hasBear = true;
        }
        if (temp.get(i).getName() == "Spy Balloon"){
          hasBalloon = true;
        }
        if (temp.get(i).getName() == "Social Credit Card"){
          hasCard = true;
        }
      }

      if (hasCard = true && hasBingChilling == true && hasBalloon == true && hasBear == true){
        return true;
      }
      return false;
    }
  
    /** 
     * "Quit" was entered. Check the rest of the command to see
     * whether we really quit the game.
     * @return true, if this command quits the game, false otherwise.
     */
    private boolean quit(Command command) 
    {
        if(command.hasSecondWord()) {
            System.out.println("Quit what?");
            return false;
        }
        else {
            return true;  // signal that we want to quit
        }
    }
}
