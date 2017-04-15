/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lanchat.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Dinesh
 */
public class UniqueIdentifier {
    private static List<Integer> ids = new ArrayList<Integer>();
    public static int range=1000;
    public static int index=0;
    static{
        for(int i=0;i<1000;i++){
            ids.add(i);
        }
        Collections.shuffle(ids);
    }
    UniqueIdentifier(){}
    public static int getIdentifier(){
        if(index>ids.size()-1){
            index=0;
        }
        return ids.get(index++);
    }
}
