import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.UIManager;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author SinanCaliskan
 */
public class frmMain extends javax.swing.JFrame {

    
    private Branch root;
    private List<Branch> branches;
    private List<Bucket> buckets;
    private int MaxStepNumber=10;
    int count=0;
    JPanel pnl=new JPanel(new GridLayout(0,1));
    ButtonGroup bg=new ButtonGroup();
    private void SetFirstValues(){
        buckets=new ArrayList<>();
        buckets.add(new Bucket(3));
        buckets.add(new Bucket(4));
        
        root=new Branch(null,buckets);          
    }
    
    private boolean CheckNotTarget(List<Bucket> Buckets){
        for(Bucket item: Buckets)
            if(item.getCurrent()==2)
                return false;
        return true;
    }
    private boolean CheckBuckets(Branch set,Branch node){
        if(node.bucketOperation!=null)//fixed       
            if(set.bucketOperation==node.bucketOperation)
                return false;
            else if(set.bucketOperation==BucketOperation.Empty && node.bucketOperation==BucketOperation.Fill){
                 for (int i = 0; i < node.buckets.size(); i++) {
                    for (int j = 0; j < set.buckets.size(); j++) {
                        if(set.buckets.get(j).getCurrent()==node.buckets.get(i).getCurrent() && set.buckets.get(j).getMax()==node.buckets.get(i).getMax())
                            return false;
                    }                    
                }        
            }
            else if(set.bucketOperation==BucketOperation.Fill && node.bucketOperation==BucketOperation.Empty){
                for (int i = 0; i < node.buckets.size(); i++) {
                    if(set.buckets.get(i).getCurrent()==node.buckets.get(i).getCurrent() && set.buckets.get(i).getMax()==node.buckets.get(i).getMax())
                        if(!CheckBuckets(set,node.node))
                            return false;//fixed                   
                }    
            }
            else if(set.bucketOperation==BucketOperation.Empty && node.bucketOperation==BucketOperation.Transfer){
                for (int i = 0; i < node.buckets.size(); i++) {
                    if(set.buckets.get(i).getCurrent()==node.buckets.get(i).getCurrent() && set.buckets.get(i).getMax()==node.buckets.get(i).getMax())
                        if(set.buckets.get(i).getCurrent()==node.node.buckets.get(i).getCurrent() && set.buckets.get(i).getMax()==node.node.buckets.get(i).getMax() )
                            return false;//?          
                }
            }
            else if(set.bucketOperation==BucketOperation.Transfer && node.bucketOperation==BucketOperation.Empty){
                for (int i = 0; i < node.buckets.size(); i++) {
                    for (int j = 0; j < set.buckets.size(); j++) {
                        if(set.buckets.get(j).getCurrent()==node.buckets.get(i).getCurrent() && set.buckets.get(j).getMax()==node.buckets.get(i).getMax())
                            return false;
                    }                    
                }
            } 
            else if(set.bucketOperation==BucketOperation.Fill && node.bucketOperation==BucketOperation.Transfer){
                for (int i = 0; i < node.buckets.size(); i++) {
                    if(set.buckets.get(i).getCurrent()==node.buckets.get(i).getCurrent() && set.buckets.get(i).getMax()==node.buckets.get(i).getMax())
                        if(set.buckets.get(i).getCurrent()==node.node.buckets.get(i).getCurrent() && set.buckets.get(i).getMax()==node.node.buckets.get(i).getMax() )
                            return false;//fixed                   
                }
            }
            else if(set.bucketOperation==BucketOperation.Transfer && node.bucketOperation==BucketOperation.Fill){
                for (int i = 0; i < node.buckets.size(); i++) {                    
                    if(set.buckets.get(i).getCurrent()==node.buckets.get(i).getCurrent() && set.buckets.get(i).getMax()==node.buckets.get(i).getMax())
                        return false;//fixed                                     
                }
            }
        return true;
    }         //check the Bucket Operations some or not
    private String PrintBuckets(List<Bucket> Buckets){
        String str="";
        for(Bucket item:Buckets)
            str+=item.getCurrent() + "L/"+item.getMax()+ "L\t";
        return str;
    }             //Gets List<Bucket> and return string value of bucket/maximum of bucket
    private String PrintOperations(Branch Node){                                //Gets Branch it like a tree,starting and leaf and going to the root
        String operations="</html>";
        while(Node.node!=null){
            switch(Node.bucketOperation)
            {
                case Fill:
                    operations="Fill\t" + PrintBuckets(Node.buckets) + "<br>" +operations;
                    break;
                case Empty:
                    operations="Empty\t" + PrintBuckets(Node.buckets) + "<br>" +operations;
                    break;
                case Transfer:
                    operations="Transfer\t" + PrintBuckets(Node.buckets) + "<br>" +operations;
                    break;                        
            }
            Node=Node.node;
        }
        return "<html>" +operations;
    }   
    private boolean HasFullBucket(List<Bucket>Buckets){
        for(Bucket item:Buckets)
            if(item.getCurrent()==item.getMax())
                return false;
        return true;
    }
    
    public frmMain(){
        initComponents();
        SetFirstValues();                                                       //Set initial values and buckets
        RunAlgorithm();        
    }
    private void RunAlgorithm(){ 
        Process(root);
        System.out.println(count);
    } 
    int locX=0;
    private void Process(Branch node){                                          
        while(node.branches.isEmpty() && node.StepNumber<=MaxStepNumber){
            if(CheckNotTarget(node.buckets)){
                Branch newBranch=null;
                List<Bucket> cloned=null;
                for (int i = 0; i < node.buckets.size(); i++) {
                    Bucket get = node.buckets.get(i);
                    if(get.getCurrent()!=0){
                        cloned=node.Clone();
                        for (int j = 0; j < cloned.size(); j++) {
                            Bucket set = cloned.get(j);
                            if(i!=j && set.getCurrent()!=set.getMax()){                                
                                cloned.get(i).Transfer(set);
                                newBranch=new Branch(node, cloned, BucketOperation.Transfer, node.StepNumber);
                                if(CheckBuckets(newBranch, node))
                                    node.branches.add(newBranch);                                
                            }                            
                        }
                    }
                    if(get.getCurrent()==0 && HasFullBucket(node.buckets)){//Fill
                        cloned=node.Clone();
                        cloned.get(i).Fill();
                        newBranch=new Branch(node, cloned, BucketOperation.Fill, node.StepNumber);
                        if(CheckBuckets(newBranch, node))
                            node.branches.add(newBranch);                        
                    }
                    if(get.getCurrent()!=0){//Empty
                        cloned=node.Clone();
                        cloned.get(i).Empty();
                        newBranch=new Branch(node, cloned, BucketOperation.Empty, node.StepNumber);
                        if(CheckBuckets(newBranch, node))
                            node.branches.add(newBranch);                        
                    }
                }
                for(Branch item:node.branches){
                    Process(item);
                }
            }
            else
            {
                for (int i = 0; i < node.buckets.size(); i++) {                 //Checking when the reaches which bucket has 2 liter
                    List<Bucket>cloned=node.Clone();
                    Bucket item=cloned.get(i);
                    if(item.getMax()==3 && item.getCurrent()==2 && node.StepNumber<=MaxStepNumber-2){
                        Bucket liter4=null;
                        for(Bucket liter:cloned)
                            if(liter.getMax()==4){
                                liter4=liter;
                                break;
                            }
                        liter4.Empty();
                        Branch branchEmpty=new Branch(node,cloned,BucketOperation.Empty,node.StepNumber);
                        node.branches.add(branchEmpty);
                        
                        cloned=branchEmpty.Clone();
                        for(Bucket liter:cloned)
                            if(liter.getMax()==4){
                                liter4=liter;
                                break;
                            }
                        item=cloned.get(i);
                        item.Transfer(liter4);
                        Branch branchTransfer=new Branch(branchEmpty,cloned,BucketOperation.Transfer,branchEmpty.StepNumber);
                        branchEmpty.branches.add(branchTransfer);
                        
                        JRadioButton rb=new JRadioButton(branchTransfer.StepNumber + " Step Solution");
                        rb.setLocation(0,locX);
                        //rb.addActionListener(radioButtonActionListener);
                        
                        locX+=10;                      
                        bg.add(rb);
                        pnl.add(rb);
                        System.out.println("\nFound 2L");               
                        String ret=PrintOperations(branchTransfer);
                        System.out.println(ret);
                        rb.setToolTipText(ret);
                        rb.addItemListener(new ItemListener() {
                        @Override
                        public void itemStateChanged(ItemEvent event) {
                            int state = event.getStateChange();
                            if (state == ItemEvent.SELECTED) {
                                Thread t1 = new Thread(new Runnable() {
                                    public void run() {
                                        pbBucket3.setValue(0);
                                        pbBucket4.setValue(0);
                                        try {
                                            Thread.sleep(1000);
                                        } catch (InterruptedException ex) {
                                            Logger.getLogger(frmMain.class.getName()).log(Level.SEVERE, null, ex);
                                        }
                                        //System.out.println(rb.getToolTipText());
                                        String[] operation=rb.getToolTipText().replaceAll("<html>", "").replaceAll("</html>", "").split("<br>");
                                        // do something when the button is selected
                                        for(String str: operation){

                                            String[] detail=str.split("\t");//0-Operation,1-3Liter,2-4Liter
                                            for (String value : detail) {
                                                switch(value){
                                                    case "Fill":
                                                        break;
                                                    case "Empty":
                                                        break;
                                                    case "Transfer":
                                                        break;    
                                                    default:
                                                        String replace=value.replace("L", "");
                                                        String [] values=replace.split("/");
                                                        if(Integer.parseInt(values[1])==pbBucket3.getMaximum()){
                                                            pbBucket3.setValue(Integer.parseInt(values[0]));
                                                        }
                                                        else if(Integer.parseInt(values[1])==pbBucket4.getMaximum()){
                                                            pbBucket4.setValue(Integer.parseInt(values[0]));
                                                        } 
                                                        break;
                                                }   
                                            }
                                            try {
                                                Thread.sleep(2000);
                                            } catch (InterruptedException ex) {
                                                //Logger.getLogger(frmMain.class.getName()).log(Level.SEVERE, null, ex);
                                            }
                                        }             
                                    }
                                });  
                                t1.start();
                            }                             
                        }
                        });
                        count++;
                    }
                    else if(item.getMax()==4 && item.getCurrent()==2){
                        JRadioButton rb=new JRadioButton(node.StepNumber + " Step Solution");
                        rb.setLocation(locX,0);
                        locX+=10;                      
                        bg.add(rb);
                        pnl.add(rb);
                        System.out.println("\nFound 2L");                
                        String ret=PrintOperations(node);
                        System.out.println(ret);
                        rb.setToolTipText(ret);  
                        rb.addItemListener(new ItemListener() {
                        @Override
                        public void itemStateChanged(ItemEvent event) {
                            int state = event.getStateChange();
                            if (state == ItemEvent.SELECTED) {
                                Thread t1 = new Thread(new Runnable() {
                                    public void run() {
                                        pbBucket3.setValue(0);
                                        pbBucket4.setValue(0);
                                        try {
                                            Thread.sleep(1000);
                                        } catch (InterruptedException ex) {
                                            Logger.getLogger(frmMain.class.getName()).log(Level.SEVERE, null, ex);
                                        }
                                        //System.out.println(rb.getToolTipText());
                                        String[] operation=rb.getToolTipText().replaceAll("<html>", "").replaceAll("</html>", "").split("<br>");
                                        // do something when the button is selected
                                        for(String str: operation){

                                            String[] detail=str.split("\t");//0-Operation,1-3Liter,2-4Liter
                                            for (String value : detail) {
                                                switch(value){
                                                    case "Fill":
                                                        break;
                                                    case "Empty":
                                                        break;
                                                    case "Transfer":
                                                        break;    
                                                    default:
                                                        String replace=value.replace("L", "");
                                                        String [] values=replace.split("/");
                                                        if(Integer.parseInt(values[1])==pbBucket3.getMaximum()){
                                                            pbBucket3.setValue(Integer.parseInt(values[0]));
                                                        }
                                                        else if(Integer.parseInt(values[1])==pbBucket4.getMaximum()){
                                                            pbBucket4.setValue(Integer.parseInt(values[0]));
                                                        } 
                                                        break;
                                                }   
                                            }
                                            try {
                                                Thread.sleep(2000);
                                            } catch (InterruptedException ex) {
                                                //Logger.getLogger(frmMain.class.getName()).log(Level.SEVERE, null, ex);
                                            }
                                        }             
                                    }
                                });  
                                t1.start();
                            }
                        }
                        });
                        count++;
                    }
                }        
                spSolutionList.getViewport().add(pnl);
                break;
            }                                  
        }
    }    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pbBucket3 = new javax.swing.JProgressBar();
        pbBucket4 = new javax.swing.JProgressBar();
        spSolutionList = new javax.swing.JScrollPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        pbBucket3.setForeground(new java.awt.Color(0, 204, 255));
        pbBucket3.setMaximum(3);
        pbBucket3.setOrientation(1);
        pbBucket3.setToolTipText("Bucket 3 Liter");
        pbBucket3.setDoubleBuffered(true);
        pbBucket3.setMaximumSize(new java.awt.Dimension(20, 32767));
        pbBucket3.setMinimumSize(new java.awt.Dimension(20, 10));
        pbBucket3.setPreferredSize(new java.awt.Dimension(20, 150));

        pbBucket4.setForeground(new java.awt.Color(0, 204, 255));
        pbBucket4.setMaximum(4);
        pbBucket4.setOrientation(1);
        pbBucket4.setToolTipText("Bucket 4 Liter");
        pbBucket4.setDoubleBuffered(true);
        pbBucket4.setMaximumSize(new java.awt.Dimension(20, 32767));
        pbBucket4.setMinimumSize(new java.awt.Dimension(20, 10));
        pbBucket4.setPreferredSize(new java.awt.Dimension(20, 200));

        spSolutionList.setToolTipText("<html>Test</html>");
        spSolutionList.setAutoscrolls(true);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pbBucket3, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 238, Short.MAX_VALUE)
                .addComponent(pbBucket4, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addComponent(spSolutionList)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(spSolutionList, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(pbBucket4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addComponent(pbBucket3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );

        spSolutionList.getAccessibleContext().setAccessibleParent(spSolutionList);

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {  
            UIManager.setLookAndFeel(
            UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(frmMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frmMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frmMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frmMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new frmMain().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JProgressBar pbBucket3;
    private javax.swing.JProgressBar pbBucket4;
    private javax.swing.JScrollPane spSolutionList;
    // End of variables declaration//GEN-END:variables
}
