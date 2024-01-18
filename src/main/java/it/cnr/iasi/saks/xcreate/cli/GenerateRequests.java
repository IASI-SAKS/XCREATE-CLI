package it.cnr.iasi.saks.xcreate.cli;

import java.awt.event.ActionEvent;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JTabbedPane;

import org.mockito.Mockito;

import it.cnr.isti.labse.xcreate.guiXCREATE.GuiCons;
import it.cnr.isti.labse.xcreate.guiXCREATE.PolicyTab;

public class GenerateRequests extends AbstractCLI{

	private HashMap<String, Integer> mapPolicies_NamePK;

	public GenerateRequests (List<String> policyNames) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException, NoSuchMethodException, InvocationTargetException{
		this.mapPolicies_NamePK = new HashMap<String, Integer>();
		this.loadPolicies();
//		Hashtable<String, Integer> fullHashtable = this.retreivePrivateHashtablePolicyNamesPK();
		Hashtable<String, Integer> fullHashtable = this.retreiveGUIPrivateField_politicheAsHashtable();
		for (String policyName : policyNames) {
			if (fullHashtable.containsKey(policyName)) {
				Integer value = fullHashtable.get(policyName);
				this.mapPolicies_NamePK.put(policyName, value);
			}
		}		
	}

	private void loadPolicies() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Method method_loadPolicyButtonActionPerformed = this.guiInstance.getClass().getDeclaredMethod("loadPolicyButtonActionPerformed", ActionEvent.class);
		method_loadPolicyButtonActionPerformed.setAccessible(true);
		method_loadPolicyButtonActionPerformed.invoke(guiInstance, this.newDummyEvent());	
	}
	
	private String retreiveGUIPrivateField_canonicalTempDirPath () throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		String fieldValue = (String) this.retreiveGUIPrivateField("canonicalTempDirPath");
		return fieldValue;
	}

	private Connection retreiveGUIPrivateField_mySqlConnection () throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		Connection fieldValue = (Connection) this.retreiveGUIPrivateField("mySqlConnection");
		return fieldValue;
	}

	private Hashtable<String, Integer> retreiveGUIPrivateField_politicheAsHashtable () throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		Hashtable<String, Integer> fieldValue = (Hashtable<String, Integer>) this.retreiveGUIPrivateField("politicheAsHashtable");
		return fieldValue;
	}

//	private Hashtable<String, Integer> retreivePrivateHashtablePolicyNamesPK () throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
//		Field privateField = this.guiInstance.getClass().getDeclaredField("politicheAsHashtable");
//		privateField.setAccessible(true);
//		Object fieldValue = privateField.get(this.guiInstance);	
//		return ((Hashtable<String, Integer>) fieldValue);
//	}

	protected Object retreivePolicyTabPrivateField (PolicyTab instance, String fieldName) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		Field privateField = instance.getClass().getDeclaredField(fieldName);
		privateField.setAccessible(true);
		Object fieldValue = privateField.get(instance);	
		return fieldValue;
	}

	protected void setMockInPolicyTabPrivateField (PolicyTab instance, String fieldName, Object fieldValue) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		Field privateField = instance.getClass().getDeclaredField(fieldName);
		privateField.setAccessible(true);
		privateField.set(instance, fieldValue);		
		
	}
	
	private PolicyTab loadOnePolicyTab (String policyName) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		String canonicalTempDirPath = this.retreiveGUIPrivateField_canonicalTempDirPath();
		Connection mySqlConnection = this.retreiveGUIPrivateField_mySqlConnection();
		
		String policyNameWithoutExt = policyName.substring(0, policyName.length() - 4);
		File policyTempDir = new File((new StringBuilder(String.valueOf(canonicalTempDirPath)))
				.append(GuiCons.DIR_SEPARATOR).append(policyNameWithoutExt).toString());
		if (!policyTempDir.exists())
			policyTempDir.mkdir();
		PolicyTab policyTab = new PolicyTab(new JTabbedPane(),
				((Integer) this.mapPolicies_NamePK.get(policyName)).intValue(), mySqlConnection,
				policyTempDir);
		
		return policyTab;

	}

	public void generatePolicies_MultipleStrategy() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
		for (String policyName : this.mapPolicies_NamePK.keySet()) {
			PolicyTab policyTab = this.loadOnePolicyTab(policyName);

			String ppm = this.retreivePolicyTabPrivateField(policyTab, "countCombinationsFromRoot").toString();
			this.mockJOptionPane_UpdateReference(ppm);
			
			JComboBox mockStrategy = Mockito.mock(JComboBox.class);
			Mockito.when(mockStrategy.getSelectedIndex()).thenReturn(2);
			Mockito.when(mockStrategy.getItemAt(Mockito.anyInt())).thenReturn("dummyStringAsAnItem");
			this.setMockInPolicyTabPrivateField(policyTab, "strategy", mockStrategy);
			
			Method method_generateButtonActionPerformed = policyTab.getClass().getDeclaredMethod("generateButtonActionPerformed", ActionEvent.class);
			method_generateButtonActionPerformed.setAccessible(true);
			method_generateButtonActionPerformed.invoke(policyTab, this.newDummyEvent());
			
		}
	}
	
	public static void main (String args[]) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException, NoSuchMethodException, InvocationTargetException, SQLException {		
		if (args.length != 1){
			System.err.println("USAGE : AddPolicy <xacml_policy_dirname>");
			System.exit(1);
		}
		
		String xacmlPoliciesDirname = args[0];
		ArrayList<String> policyNames = new ArrayList<String>();
		File[] fileList = new File(xacmlPoliciesDirname).listFiles(); 
		for (File policyFile : fileList) {
			String policyName = policyFile.getName();
			policyNames.add(policyName);
		}
		
		GenerateRequests cli = new GenerateRequests(policyNames);
//String ppm = "thisisfoo, isn't it?";
//cli.mockJOptionPane_UpdateReference(ppm);
//String result = JOptionPane.showInputDialog(ppm);
//System.err.println("PPM: " + ppm + ", RESULT: " + result);
		cli.generatePolicies_MultipleStrategy();
		
		System.err.println("Closing ...");				
		cli.disposeConnections();
		System.err.println("done!");
	} 
}
