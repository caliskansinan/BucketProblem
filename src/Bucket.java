/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author SinanCaliskan
 */
public class Bucket 
{
    private int current=0;
    private int max;
    
    public int getCurrent() {
        return current;
    }
    public void setCurrent(int Current) {
        if(Current>=0 && max>=Current)//Set bucket value must be in range(0<x<Maximum)
            this.current = Current;
    }
    
    public int getMax() {
        return max;
    }
    public void setMax(int max) {
        this.max = max;
    }
    
    public Bucket(int Max){
        max=Max;
    }
    public Bucket(int Current,int Max){
        current=Current;
        max=Max;
    }
    
    public void Fill(){
        setCurrent(getMax());//Fill the bucket
    }
    public void Empty(){
        setCurrent(0);//Bucket is empty(0)
    }
    public void Transfer(Bucket other){                              //x=this.getCurrent()+other.getCurrent()
        if(this.getCurrent()+other.getCurrent()<=other.getMax()){    //Direct transfer must be in range (0<x<=Transfer bucket Maximum Value)            
            other.setCurrent(this.getCurrent()+other.getCurrent());
            this.setCurrent(this.getCurrent()-this.getCurrent());            
        }        
        else if(this.getCurrent()+other.getCurrent()>other.getMax()){//transfer must be in range (Transfer Bucket Maximum Value<x)
            this.setCurrent(this.getCurrent()-(other.getMax()-other.getCurrent()));//
            other.setCurrent(other.getMax());            
        }
    }
    public Bucket Clone()
    {
        return new Bucket(this.getCurrent(),this.getMax());
    }
}
