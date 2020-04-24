package cache;

import java.util.ArrayList;
import java.util.List;

public final class MethodParams{

    Object[] args;

    public MethodParams(List<Object> obList){
        args = new Object[obList.size()];
        int i = 0;
        for(Object o: obList){
            args[i++] = o;
        }
    }

    @Override
    public int hashCode() {
        Long hashSum = 0l;
        for(Object o: args){
            hashSum+=o.hashCode();
            hashSum = Long.valueOf(hashSum.hashCode());
        }
        return hashSum.intValue();
    }

    @Override
    public boolean equals(Object obj) {
        MethodParams mpObj = (MethodParams) obj;
        if(mpObj.args.length != args.length){
            return false;
        }
        for(int i=0; i<args.length; i++){
            try{
                if(!args[i].equals(mpObj.args[i])){
                    return false;
                }
            }catch(ClassCastException ex){
                return false;
            }
        }
        return true;
    }

    public static class Builder{

        List<Object> objectList = new ArrayList<>();

        public Builder addParam(Object obj){
            objectList.add(obj);
            return this;
        }

        public MethodParams build(){
            return new MethodParams(objectList);
        }

        //The way Cache is implemented is that it uses MethodParam as key - So basically we need MethodParam to create MethodParam (Catch22 ?)
        //I can keep a separate HashMap in MethodParam class that takes a List<Object> (params) - i.e. a cache for MethodParam object itself
        //build method will check in the local cache first and create a new object only if the object is not in the cache
        //EVen then we will still be creating List<Objects>
    }


}
