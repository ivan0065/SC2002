package Main.BTO;

import Main.Enums.FlatType;
import java.util.List;
import java.util.Map;

public class FlatList{
    private List<Flat> flatlist;
    private int numAvailableUnits;
    private Map<FlatType,Integer> unitCount;

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

    public void UpdateAvailUnits(){
        numAvailableUnits=0;
        for(Flat flat:flatlist){
            if(!flat.isbooked()){
                numAvailableUnits++;
                FlatType type=flat.getFlatType();
                unitCount.put(type,unitCount.get(type)+1);
            }
        }
    }
}