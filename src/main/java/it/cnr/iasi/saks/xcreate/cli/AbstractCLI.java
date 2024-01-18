package it.cnr.iasi.saks.xcreate.cli;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import it.cnr.isti.labse.xcreate.guiXCREATE.XCreateMain;

import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public abstract class AbstractCLI {

//	protected File tmpDir;
//	protected Connection mySQLConnection;
//	protected EXistDBDriver eXistDBDriver;
	
//	protected XCreateMain mock_XCreateMain;
	protected XCreateMain guiInstance;

	protected static final int MOCKED_RETURN_showOpenDialog = 0;
	private static File REFERENCE_TO_THE_POLICY_FILE = null;
		
//	private static String REFERENCE_TO_THE_INSERTED_INPUT = "__unset__";
	private static Object REFERENCE_TO_THE_INSERTED_INPUT = null;
	
	public AbstractCLI() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		this.guiInstance = new XCreateMain();
		this.mockJFileChooser();
		
		this.mockJOptionPane();
//		MockedStatic<JOptionPane> utilities = Mockito.mockStatic(JOptionPane.class);
//	    utilities.when(() -> JOptionPane.showConfirmDialog(Mockito.any(Component.class),Mockito.anyString(),Mockito.anyString(),Mockito.anyInt())).thenReturn(0);		
	}
	
	protected Object retreiveGUIPrivateField (String fieldName) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		Field privateField = this.guiInstance.getClass().getDeclaredField(fieldName);
		privateField.setAccessible(true);
		Object fieldValue = privateField.get(this.guiInstance);	
		return fieldValue;
	}

	protected void setMockInGUIPrivateField (String fieldName, Object fieldValue) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		Field privateField = this.guiInstance.getClass().getDeclaredField(fieldName);
		privateField.setAccessible(true);
		privateField.set(this.guiInstance, fieldValue);		
		
	}
		
	protected void disposeConnections() throws SQLException, NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Method method_exitButtonActionPerformed = this.guiInstance.getClass().getDeclaredMethod("exitButtonActionPerformed", ActionEvent.class);
		method_exitButtonActionPerformed.setAccessible(true);
		method_exitButtonActionPerformed.invoke(this.guiInstance, this.newDummyEvent());
		
//		System.err.println("Is eXistDBDriver connected?");
//		if (this.eXistDBDriver.isConnected()) {
//			System.err.println("Yes it is");
//		} else {
//			System.err.println("No it isn't");
//		}
//		System.err.println("Is mySQLConnection open?");
//		if (this.mySQLConnection.isClosed()) {
//			System.err.println("No it isn't");
//		} else {
//			System.err.println("Yes it is");
//		}
//
//		this.eXistDBDriver.disconnect();
//		this.mySQLConnection.commit();
//		this.mySQLConnection.close();
//	    
//		System.err.println("Is eXistDBDriver connected?");
//		if (this.eXistDBDriver.isConnected()) {
//			System.err.println("Yes it is");
//		} else {
//			System.err.println("No it isn't");
//		}
//		System.err.println("Is mySQLConnection open?");
//		if (this.mySQLConnection.isClosed()) {
//			System.out.println("No it isn't");
//		} else {
//			System.out.println("Yes it is");
//		}				
	}
	
	private void mockJFileChooser() {
		Mockito.mockConstruction(JFileChooser.class,(mock,context)-> {
			context.constructor(); 
			Mockito.when(mock.showOpenDialog(Mockito.any())).thenReturn(MOCKED_RETURN_showOpenDialog);
			Mockito.when(mock.getSelectedFile()).thenReturn(REFERENCE_TO_THE_POLICY_FILE);
	 	});		
		
	}
	
	protected void mockJFileChooser_UpdateReference(File currentPolicyToAdd) {
		REFERENCE_TO_THE_POLICY_FILE = currentPolicyToAdd;		
	}

	private void mockJOptionPane() {
		MockedStatic<JOptionPane> utilities = Mockito.mockStatic(JOptionPane.class);
	    utilities.when(() -> JOptionPane.showConfirmDialog(Mockito.any(Component.class),Mockito.anyString(),Mockito.anyString(),Mockito.anyInt())).thenReturn(0);	
//	    ****************************************************************
// FOR SOME HIDDEN REASON THE FOLLOWING STATEMENT DOES NOT WORK, IT LOOKS BINDING THE VALUE AD THE TIME OF THE DEFINITION. THE NEXT STATEMENT WORKS
//	    utilities.when(() -> JOptionPane.showInputDialog(Mockito.anyString())).thenReturn(REFERENCE_TO_THE_INSERTED_INPUT);		
	    utilities.when(() -> JOptionPane.showInputDialog(Mockito.anyString())).then(new Answer<Object>() {
																										@Override
																										public Object answer( InvocationOnMock arg0) throws Throwable {																									
																											return REFERENCE_TO_THE_INSERTED_INPUT;
																										}
	    	 																						});		
//	    ****************************************************************	    
	}
	
	protected void mockJOptionPane_UpdateReference(String currentInputInserted) {
		REFERENCE_TO_THE_INSERTED_INPUT = currentInputInserted;		

	}

	protected ActionEvent newDummyEvent() {
		Object dummySource = new Object(); 
		ActionEvent dummyEvent = new ActionEvent(dummySource, ActionEvent.ACTION_LAST-1, "command");
		return dummyEvent;
	}			
	
}
