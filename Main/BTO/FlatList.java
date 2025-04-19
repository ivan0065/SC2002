package Main.BTO;

import Main.Enums.FlatType;
import java.util.List;
import java.util.Map;

public class FlatList{
    private List<Flat> flatlist;
    static int numAvailableUnits;
    static Map<FlatType,Integer> unitCount;

    public FlatList(List<Flat> flatlist){
        this.flatlist=flatlist;
        unitCount.put(FlatType.Two_Room,0);
        unitCount.put(FlatType.Three_Room,0);
        numAvailableUnits=0;

        for(Flat flat:flatlist){
            if(!flat.isbooked()){
                numAvailableUnits++;
                FlatType type=flat.getFlatType();
                unitCount.put(type,unitCount.get(type)+1);
            }
        }
    }

    public int getTavailUnits(){
        return numAvailableUnits;
    }

    public int get2roomAvailUnit(){
        return unitCount.get(FlatType.Two_Room);
    }

    public int get3roomAvailUnit(){
        return unitCount.get(FlatType.Three_Room);
    }

    public Map<FlatType,Integer> getavail_byroom(){
        return unitCount;
    }

    public void book_3room(){
        for(Flat flat: flatlist){
            if(flat.getFlatType()==FlatType.Three_Room && flat.isbooked()==false){
                flat.Book_flat();
            }
        }
    }
    public void book_2room(){
        for(Flat flat: flatlist){
            if(flat.getFlatType()==FlatType.Two_Room && flat.isbooked()==false){
                flat.Book_flat();
            }
        }
    }
    public void unbook_3room(){
        for(Flat flat: flatlist){
            if(flat.getFlatType()==FlatType.Three_Room && flat.isbooked()==true){
                flat.unBook_flat();
            }
        }
    }
    public void unbook_2room(){
        for(Flat flat: flatlist){
            if(flat.getFlatType()==FlatType.Two_Room && flat.isbooked()==true){
                flat.unBook_flat();
            }
        }
    }
}