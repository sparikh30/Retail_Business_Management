/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication1;
import java.sql.*;
import oracle.jdbc.*;
import java.io.*;
import java.util.*;
import oracle.jdbc.pool.OracleDataSource;

/**
 *
 * @author chetanpatil
 */
public class EmployeeMonthlyReport extends javax.swing.JFrame {

    /**
     * Creates new form EmployeeMonthlyReport
     */
    public EmployeeMonthlyReport() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(204, 51, 255));

        jLabel1.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        jLabel1.setText("Enter Employee ID : ");

        jButton1.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        jButton1.setText("Generate Report");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(83, 83, 83)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(32, 32, 32)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(184, 184, 184)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(108, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(48, 48, 48)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 58, Short.MAX_VALUE)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31))
        );

        jButton2.setText("Home");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(109, 109, 109)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jButton2)))
                .addContainerGap(60, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jButton2)
                .addGap(34, 34, 34)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(49, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        
        
                 String str="";

		 try
   	 	{

        	//Connecting to Oracle server. Need to replace username and
      		//password by your username and your password. For security
      		//consideration, it's better to read them in from keyboard.
        	//OracleDataSource ds = new oracle.jdbc.pool.OracleDataSource();
        	//ds.setURL("jdbc:oracle:thin:@castor.cc.binghamton.edu:1521:acad111");
        	//Connection conn = ds.getConnection(username, password);

        	//Prepare to call stored procedure:
                String          eid;
                eid= jTextField1.getText();
        	CallableStatement cs = NewJFrame.conn.prepareCall( "{call display.employee_Report(?,?,?) }");
		int st=1;

		
        	
        	

           	//register the out parameter (the first parameter)
        	cs.setString(1, eid);
		cs.registerOutParameter(2, OracleTypes.CURSOR);
		cs.registerOutParameter(3, Types.INTEGER);


        	// execute and retrieve the result set
        	cs.execute();

		if(cs.getInt(3)==1)
		{
        		ResultSet rs = (ResultSet)cs.getObject(2);

        		// print the results
	
		
        		while (rs.next()) {
				if(st==1)
				{
					str="EID \t" +"Name \t" + "Month \t" + "Year \t " + "Total\t " + "Count \t" + "Quantity \n ---------------------------------- --------------------- \n\n ";
					System.out.println("EID \t" +"Name \t" + "Month \t" + "Year \t " + "Total\t " + "Count \t" + "Quantity \t");
					System.out.println("___________________________________________________________");
				}	
				st=2;
				System.out.println(rs.getString(2)+ "\t"+rs.getString(1) + "\t" + rs.getString(3) + "\t" + rs.getString(4)+ "\t" +rs.getDouble(5) +"\t" + rs.getInt(6) + "\t" +rs.getInt(7));
            		        str=str+rs.getString(2)+ "\t"+rs.getString(1) + "\t" + rs.getString(3) + "\t" + rs.getString(4)+ "\t" +rs.getDouble(5) +"\t" + rs.getInt(6) + "\t" +rs.getInt(7);
                                
                                
                                
                                   
                                
                                
                                
        		}
			if(st==1){
				System.out.println("No data Found");
                                str="No data found";
                                new Dialog("No data Found"); 
			}
                        if(st==2){
                             // Create file
                                    FileWriter fstream = new FileWriter("Employee_Monthly_Record.txt");
                                    BufferedWriter out = new BufferedWriter(fstream);

                                    // Write details into file
                                    out.write(str);

                                    // Close the output stream
                                    out.close();

                                    // Notification to User
                                    System.out.println("Report is Generated in Current Directory as a file \" Employee_Monthly_Record.txt \" ");
                                    new Dialog("Report is Generated in Current Directory as a file \" Employee_Monthly_Record.txt \"");
                              
                        }

        		//close the result set, statement, and the connection
	
		}
		else if(cs.getInt(3)==3){
			System.out.println("Too large employee ID (Max 3 Digits)");
                        new Dialog("No data Found");
                }
                else{
	 		System.out.println("Invalid Employee ID");
                        new Dialog("No data Found");
                }

        	cs.close();
        	//conn.close();
   	}
   	catch (SQLException ex) { System.out.println ("\n*** SQLException caught ***\n" + ex.getMessage());}
   	catch (Exception e) {System.out.println ("\n*** other Exception caught ***\n");}
  	




      
        
        
        
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
         new NewJFrame1().setVisible(true);
    }//GEN-LAST:event_jButton2ActionPerformed

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
            java.util.logging.Logger.getLogger(EmployeeMonthlyReport.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(EmployeeMonthlyReport.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(EmployeeMonthlyReport.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(EmployeeMonthlyReport.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new EmployeeMonthlyReport().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
}
