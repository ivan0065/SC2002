package Main.BTO;
import Main.Enums.FlatType;

public class Flat{
     private final FlatType flatType;
     private Boolean taken;
     private int price;
     
     public Flat(FlatType flatType, int price){
          this.flatType=flatType;
          this.taken=false;
          this.price=price;
     }

     public boolean isbooked(){
          return taken;
     }
     public void Book_flat(){
          this.taken=true;
     }
     public void unBook_flat(){
          this.taken=false;
     }

     public FlatType getFlatType(){
          return this.flatType;
     }
}