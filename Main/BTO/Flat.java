package Main.BTO;
import Main.Enums.FlatType;

public class Flat{
     private final FlatType flatType;
     private Boolean taken;
     
     public Flat(FlatType flatType){
          this.flatType=flatType;
          this.taken=false;
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