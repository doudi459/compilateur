/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package texteditor;


import de.javasoft.plaf.synthetica.SyntheticaAluOxideLookAndFeel;
import java.awt.Image;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;


/**
 *
 * @author DRIA
 */
public class Main_frame extends javax.swing.JFrame {
    Runtime runtime = Runtime.getRuntime();
    File fichierALire;
    int cptLigne=1;
    int indice=-1;
    boolean bUndo=true;
    ArrayList<String> liste = new ArrayList<>();
    
     void setLook(){
            try 
    {
      UIManager.setLookAndFeel(new SyntheticaAluOxideLookAndFeel());
    } 
    catch ( ParseException | UnsupportedLookAndFeelException e) 
    {
    }
    }
     
     
    public Main_frame() {
        setLook();
        initComponents();
        try {
            Image i= ImageIO.read(getClass().getResource("/texteditor/Code_15px.png"));
            this.setIconImage(i);
        } catch (IOException ex) {
            Logger.getLogger(Main_frame.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public void deleteFiles(){
        File f = new File("msg.txt");
        File f2= new File("fichierTs.txt");
        File f3= new File("fichierQuad.txt");
        f.delete();  f2.delete(); f3.delete();
    }
    
    public void readFile(){
        FileReader fr;
        BufferedReader br;
        JFileChooser fileOuvrir = new JFileChooser();
			if (fileOuvrir.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
           
                fichierALire=new File(fileOuvrir.getSelectedFile().getAbsolutePath());
            try {
                fr = new FileReader(fichierALire.getAbsolutePath());
                br = new BufferedReader(fr);
                int nbLigne=1,nbMot=0;
                String  s ="",m=br.readLine();
                while(m!=null){
                    nbLigne++;
                    String[] tab=m.split(" ");
                    nbMot+=tab.length;
                    
                    s+=m+"\n";
                    m=br.readLine();
                    
                }
                
                liste.add(s);
                tCode.setText(s);
                br.close();
            } catch (FileNotFoundException ex) {
                Logger.getLogger(Main_frame.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(Main_frame.class.getName()).log(Level.SEVERE, null, ex);
            }
                
			}
    }
    
    public void saveFile(File f){
        BufferedWriter output;
        FileWriter fw;
        
            String s= tCode.getText();
            try {
                fw= new FileWriter(fichierALire.getAbsolutePath());
                output= new BufferedWriter(fw);
                output.write(s);
                output.flush();
                output.close();
                lSave.setText("File saved");
            } catch (FileNotFoundException ex) {
                Logger.getLogger(Main_frame.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(Main_frame.class.getName()).log(Level.SEVERE, null, ex);
            }
        
    }
    
    public void procedureSave(){
         if(fichierALire!=null) saveFile(fichierALire);
         else{
             if(!tCode.getText().isEmpty()){
                        JFileChooser fileEnregistrer = new JFileChooser();
			if (fileEnregistrer.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
				fichierALire = new File(fileEnregistrer.getSelectedFile().getAbsolutePath()+".txt");
                                saveFile(fichierALire);                     	
			}                       
             }
         }
    }
    
    public void procedureNew(){
            if(fichierALire!=null ){
            int x=JOptionPane.showConfirmDialog(this, "save ");
            if(x==0) saveFile(fichierALire);
            if(x!=2) {tCode.setText("");fichierALire=null;}
        }
       else{
           if(fichierALire==null && !tCode.getText().isEmpty()){
                        int x=JOptionPane.showConfirmDialog(this, "save ");
                        if(x==0){
                        JFileChooser fileEnregistrer = new JFileChooser();
			if (fileEnregistrer.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
				fichierALire = new File(fileEnregistrer.getSelectedFile().getAbsolutePath()+".txt");
                                
                                 saveFile(fichierALire);
                                                     	
			}}
                        if(x!=2) {tCode.setText("");fichierALire=null;}
           }
       }
       deleteFiles();
    }
    
    public void procedurRun(){
        try {
            if(!tCode.getText().isEmpty()){
            
                //sauvegarde du fichier 
               if(fichierALire!=null) saveFile(fichierALire);
         else{
                        JFileChooser fileEnregistrer = new JFileChooser();
			if (fileEnregistrer.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
				fichierALire = new File(fileEnregistrer.getSelectedFile().getAbsolutePath()+".txt");
                                saveFile(fichierALire);                     	
			}                       
            }
               //compilation
               
            String path=fichierALire.getAbsolutePath();
            final Process p;
            p = runtime.exec( new String[]{ "cmd.exe","/C","compil\\compilateur <"+path+" >msg.txt" });
            tOutput.setText(" (run)");
                
            Thread.sleep(1500);            
        //messages de compilation
                FileReader fr = null;
        try {
            fr = new FileReader("msg.txt");
            BufferedReader br = new BufferedReader(fr);
            String m=br.readLine(),s="";
            while(m!=null){
                s+=m+"\n";
                m=br.readLine();
                
            }
            tOutput.setText(s);
            br.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Main_frame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Main_frame.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fr.close();
            } catch (IOException ex) {
                Logger.getLogger(Main_frame.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
            
            
            }
          
        } catch (IOException ex) {
            Logger.getLogger(Main_frame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(Main_frame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void setPath(){
        if(!tCode.getText().isEmpty()){
                        JFileChooser fileEnregistrer = new JFileChooser();
			if (fileEnregistrer.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
				fichierALire = new File(fileEnregistrer.getSelectedFile().getAbsolutePath()+".txt");
                                                  	
			}
    }
    }
    public void undo(){
        
        if(indice != -1){
             indice--;
             
            tCode.setText(liste.get(indice-1));
   
        }
    }
    
    public void redo(){
        if(indice != -1 && indice < liste.size()-1) {      
          indice ++;
          tCode.setText(liste.get(indice));
          
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

        jScrollPane3 = new javax.swing.JScrollPane();
        tCode = new javax.swing.JEditorPane();
        jLabel1 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        lSave = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tOutput = new javax.swing.JEditorPane();
        bOpenTableSymboles = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        bNew = new javax.swing.JMenu();
        bOpen = new javax.swing.JMenu();
        bSave = new javax.swing.JMenu();
        undo = new javax.swing.JMenu();
        redo = new javax.swing.JMenu();
        bRun = new javax.swing.JMenu();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("IDE");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        tCode.setBackground(new java.awt.Color(39, 40, 34));
        tCode.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        tCode.setForeground(new java.awt.Color(255, 255, 255));
        tCode.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        tCode.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tCodeKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                tCodeKeyTyped(evt);
            }
        });
        jScrollPane3.setViewportView(tCode);

        jLabel1.setFont(new java.awt.Font("Corbel", 3, 20)); // NOI18N
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/texteditor/Source Code_22px.png"))); // NOI18N
        jLabel1.setText("Compiler ");
        jLabel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        tOutput.setEditable(false);
        tOutput.setBorder(javax.swing.BorderFactory.createTitledBorder("Output :"));
        tOutput.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jScrollPane2.setViewportView(tOutput);

        bOpenTableSymboles.setIcon(new javax.swing.ImageIcon(getClass().getResource("/texteditor/Table Properties_60px.png"))); // NOI18N
        bOpenTableSymboles.setPreferredSize(new java.awt.Dimension(80, 25));
        bOpenTableSymboles.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bOpenTableSymbolesActionPerformed(evt);
            }
        });

        jMenuBar1.setFont(new java.awt.Font("Corbel", 0, 12)); // NOI18N

        bNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/texteditor/Add File_30px.png"))); // NOI18N
        bNew.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                bNewMouseClicked(evt);
            }
        });
        jMenuBar1.add(bNew);

        bOpen.setIcon(new javax.swing.ImageIcon(getClass().getResource("/texteditor/Open Folder_30px.png"))); // NOI18N
        bOpen.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                bOpenMouseClicked(evt);
            }
        });
        jMenuBar1.add(bOpen);

        bSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/texteditor/Save_30px.png"))); // NOI18N
        bSave.setToolTipText("save");
        bSave.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                bSaveMouseClicked(evt);
            }
        });
        jMenuBar1.add(bSave);

        undo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/texteditor/Undo_32px.png"))); // NOI18N
        undo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                undoMouseClicked(evt);
            }
        });
        jMenuBar1.add(undo);

        redo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/texteditor/Redo_32px.png"))); // NOI18N
        redo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                redoMouseClicked(evt);
            }
        });
        jMenuBar1.add(redo);

        bRun.setIcon(new javax.swing.ImageIcon(getClass().getResource("/texteditor/Circled Play_32px_2.png"))); // NOI18N
        bRun.setToolTipText("Run");
        bRun.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                bRunMouseClicked(evt);
            }
        });
        jMenuBar1.add(bRun);

        jMenu1.setBorder(null);
        jMenu1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/texteditor/DOC_30px.png"))); // NOI18N

        jMenuItem1.setText("Fichier Flex");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuItem2.setText("Fichier Bison");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem2);

        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator1)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lSave, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 697, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 605, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(bOpenTableSymboles, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(9, 9, 9)
                        .addComponent(jLabel1))
                    .addComponent(lSave, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 283, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(bOpenTableSymboles, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void bNewMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bNewMouseClicked
     procedureNew();
    }//GEN-LAST:event_bNewMouseClicked

    private void bOpenMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bOpenMouseClicked
        if(fichierALire!=null){
            int x=JOptionPane.showConfirmDialog(this, "save ");
            if(x==0) saveFile(fichierALire);
            if(x!=2) readFile();
        }
        else {
            if(fichierALire==null && !tCode.getText().isEmpty()){
                        int x=JOptionPane.showConfirmDialog(this, "save ");
                        if(x==0){
                        JFileChooser fileEnregistrer = new JFileChooser();
			if (fileEnregistrer.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
				fichierALire = new File(fileEnregistrer.getSelectedFile().getAbsolutePath()+".txt");
                                
                                 saveFile(fichierALire);
                                                     	
			}}
                        if(x!=2) {tCode.setText("");fichierALire=null;}
           }
            
            readFile();
        }
        deleteFiles();
    }//GEN-LAST:event_bOpenMouseClicked

    private void bSaveMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bSaveMouseClicked
        procedureSave();
    }//GEN-LAST:event_bSaveMouseClicked

    private void tCodeKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tCodeKeyTyped
        lSave.setText("");
        if(liste.size()<100)
        liste.add(tCode.getText());
        else liste.remove(0);
        indice=liste.size();
       
    }//GEN-LAST:event_tCodeKeyTyped

    private void undoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_undoMouseClicked
        if(bUndo){
        if(liste.size()<100 )
        liste.add(tCode.getText());
        else liste.remove(0);
        indice=liste.size();
        bUndo=false;
        }
        undo();
    }//GEN-LAST:event_undoMouseClicked

    private void redoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_redoMouseClicked
       redo();
    }//GEN-LAST:event_redoMouseClicked

    private void bRunMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bRunMouseClicked
    procedurRun();
    }//GEN-LAST:event_bRunMouseClicked

    private void bOpenTableSymbolesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bOpenTableSymbolesActionPerformed
        try {
            Table table =  new Table("fichierTs.txt","fichierQuad.txt");
            table.setVisible(true);
        } catch (IOException ex) {
            Logger.getLogger(Main_frame.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }//GEN-LAST:event_bOpenTableSymbolesActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
       deleteFiles();
    }//GEN-LAST:event_formWindowClosing

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
       
        try {
            final Process p;
           
            p = runtime.exec( new String[]{ "cmd.exe","/C","compil\\lex.l" });
        } catch (IOException ex) {
            Logger.getLogger(Main_frame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
             try {
            final Process p;
            p = runtime.exec( new String[]{ "cmd.exe","/C","compil\\synt.y" });
        } catch (IOException ex) {
            Logger.getLogger(Main_frame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void tCodeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tCodeKeyPressed
       if(evt.getKeyCode()==117) procedurRun();
    }//GEN-LAST:event_tCodeKeyPressed

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
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Main_frame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Main_frame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Main_frame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Main_frame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Main_frame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu bNew;
    private javax.swing.JMenu bOpen;
    private javax.swing.JButton bOpenTableSymboles;
    private javax.swing.JMenu bRun;
    private javax.swing.JMenu bSave;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel lSave;
    private javax.swing.JMenu redo;
    private javax.swing.JEditorPane tCode;
    private javax.swing.JEditorPane tOutput;
    private javax.swing.JMenu undo;
    // End of variables declaration//GEN-END:variables
}
