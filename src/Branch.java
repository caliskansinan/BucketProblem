
import java.util.ArrayList;
import java.util.List;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author SinanCaliskan
 */
public class Branch 
{
    public Branch node;
    public List<Branch> branches=new ArrayList<>();
    public List<Bucket> buckets=new ArrayList<>();
    public BucketOperation bucketOperation;  
    public int StepNumber=0;
    
    public Branch(Branch Node){
        node=Node;
    }
    public Branch(Branch Node,List<Bucket> Buckets){
        node=Node;
        buckets=Buckets;
    }
        
    public Branch(Branch Node,List<Bucket> Buckets,BucketOperation operation,int Step){
        node=Node;
        buckets=Buckets;     
        bucketOperation=operation;
        StepNumber=Step+1;        
    }
        
    public Branch(Branch Node,Branch Branches,List<Bucket> Buckets,BucketOperation operation,int Step){
        node=Node;
        branches.add(Branches);
        buckets=Buckets;
        bucketOperation=operation;
        StepNumber=Step+1;
    }
        
    public Branch(Branch Node,List<Branch> Branches,List<Bucket> Buckets,BucketOperation operation,int Step){
        node=Node;
        branches=Branches;
        buckets=Buckets;
        bucketOperation=operation;
        StepNumber=Step+1;
    }
    
    public List<Bucket> Clone(){
        List<Bucket> newList;
        if(buckets!=null)
        {
            if(buckets.size()>0)
            {
                newList=new ArrayList<>();  
                for(Bucket item :buckets)
                {
                    newList.add(new Bucket(item.getCurrent(),item.getMax()));
                }
                return newList;
            }
            else            
                return null;
        }
        else
            return null;
    }
    public List<Bucket> Clone(List<Bucket> Buckets){
        List<Bucket> newList;
        if(Buckets!=null)
        {
            if(Buckets.size()>0)
            {
                newList=new ArrayList<>();  
                for(Bucket item :buckets)
                {
                    newList.add(new Bucket(item.getCurrent(),item.getMax()));
                }
                return newList;
            }
            else            
                return null;
        }
        else
            return null;
    }
}
