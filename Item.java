public class Item{
  /**
  *
  */
  private int weight;
  private String name;
  private int id;
   /**
  *  Creates an Item
  *@param name Item's name
  *@param weight Item's weight
  *@param id Item's Id number
  */
  public Item(String name, int weight, int id){
    this.name = name;
    this.weight = weight;
    this.id = id;
  }
   /**
  * @return name Items name
  */
  public String getName(){
    return name;
  }
   /**
  *@return id Item's id number
  */
  public int getId(){
    return id;
  }
   /**
  *@return weight Item's weight
  */
  public int getItemWeight(){
    return weight;
  }




  
}
