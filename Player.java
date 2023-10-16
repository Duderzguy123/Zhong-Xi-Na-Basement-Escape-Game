import java.util.ArrayList;

public class Player{
  //Instance Variables
  private int weightLimit;
  private int currentWeight;
  private ArrayList<Item> playerItems;
  
  //Constructor
  public Player(){
    playerItems = new ArrayList<Item>();
    weightLimit = 250;
    currentWeight = 0;
  }
  
 /**
  *@return Player Weight Limit
  */
  public int getWeightLimit(){
    return weightLimit;
  }
 /**
  *@return Player's current weight
  */
  public int getCurrentWeight(){
    return currentWeight;
  }
   /**
  *Method adds an item to player holding
  */
  public void addPlayerItem(String name, int weight, int id){
      Item temp = new Item(name, weight, id);
      playerItems.add(temp);
    }
  
   /**
  *@return playerItems List of Items Player has
  */
  public ArrayList<Item> getPlayerItems(){
      return playerItems;
    }
   /**
  *Increases weight of player based of weight of object
  */
  public void addPlayerWeight(int weight){
    currentWeight += weight;
  }
   /**
  *Decreases weight of player based of weight of object
  */
  public void losePlayerWeight(int weight){
    currentWeight -= weight;
  }
  
   /**
  *Method removes item from ArrayList playerItems based on id of item
  */
  public void removePlayerItem(int id){
    for (int i = 0; i < playerItems.size(); i++){
      if (playerItems.get(i).getId() == id){
        playerItems.remove(i);
      }
    }
  }
  


  
}
