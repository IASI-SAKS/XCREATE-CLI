package it.cnr.iasi.saks.xcreate.cli;

import java.awt.event.ActionEvent;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.SQLException;

import javax.swing.JFileChooser;

import it.cnr.isti.labse.xcreate.guiXCREATE.XCreateMain;

public class AddPolicies extends AbstractCLI{

	private String xacmlPoliciesDirname;
	
	public AddPolicies (String xacmlPoliciesDirname) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException{
		this.xacmlPoliciesDirname = xacmlPoliciesDirname;
	}
		
	public void addPolicy(File policyToAdd) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		this.mockJFileChooser_UpdateReference(policyToAdd);

		JFileChooser foo = new JFileChooser(new File("."));
//		if (foo.showOpenDialog(null) == AbstractCLI.MOCKED_RETURN_showOpenDialog) {
//			System.err.println("This is the mocked return value:" + AbstractCLI.MOCKED_RETURN_showOpenDialog);
//		} else {
//			System.err.println("This is something else");
//		}
		System.err.println("This is the current selected file:" + foo.getSelectedFile().getAbsolutePath());

		Method method_addPolicyButtonActionPerformed = this.guiInstance.getClass().getDeclaredMethod("addPolicyButtonActionPerformed", ActionEvent.class);
		method_addPolicyButtonActionPerformed.setAccessible(true);
		method_addPolicyButtonActionPerformed.invoke(guiInstance, this.newDummyEvent());

//		File file = new File(this.xacmlPolicyFilename);
//		this.addAdaptaionOfTheLegacyImplementation(file);
	}
	
	public void addPolicies() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		File[] fileList = new File(this.xacmlPoliciesDirname).listFiles(); 
		for (File policyToAdd : fileList) {
			this.addPolicy(policyToAdd);
			
//		    Foo f = new Foo();
////		    Foo f = Mockito.mock(Foo.class);
//		    f.showValues();
//		    System.err.println("the values are: " +f.getInt() + ", " + f.getString());
////			Method method_updateValues = f.getClass().getDeclaredMethod("updateValues", Object.class, int.class, String.class);
//			Method method_updateValues = f.getClass().getDeclaredMethod("addPolicyButtonActionPerformed", ActionEvent.class);
//			method_updateValues.setAccessible(true);
//			Object dummySource = new Object(); 
////			int value = (int) method_updateValues.invoke(f, dummySource, 1978, "test");
//			method_updateValues.invoke(f, new ActionEvent(new Object(), ActionEvent.ACTION_LAST-1, "command"));
//		    System.err.println("the values are: " +f.getInt() + ", " + f.getString());
//		    f.showValues();

//			Method method_addPolicyButtonActionPerformed = cli.mock_XCreateMain.getClass().getDeclaredMethod("addPolicyButtonActionPerformed", ActionEvent.class);
//			method_addPolicyButtonActionPerformed.setAccessible(true);
//			Object dummySource = new Object(); 
//			ActionEvent dummyEvent = new ActionEvent(dummySource, ActionEvent.ACTION_LAST-1, "command");
//			method_addPolicyButtonActionPerformed.invoke(cli.mock_XCreateMain, dummyEvent);
		}
		
	}

	public static void main (String args[]) throws NoSuchMethodException, SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException, SQLException, InvocationTargetException {
		if (args.length != 1){
			System.err.println("USAGE : AddPolicy <xacml_policy_dirname>");
			System.exit(1);
		}
		
		System.err.println("exist.home: " + System.getProperty("exist.home"));
		
		AddPolicies cli  = new AddPolicies(args[0]);

		XCreateMain guiInstance = new XCreateMain();

//		int randomValueOption = JOptionPane.showConfirmDialog(cli.mock_XCreateMain, "Do you want to insert Random Values?", "Random Values Option", 0);
//		if (randomValueOption==0) {
//			System.out.println("0");
//		} else {
//			System.out.println("other");
//		}
		
		cli.addPolicies();

//		int randomValueOption = JOptionPane.showConfirmDialog(xCreateMain,
//				"Do you want to insert Random Values?", "Random Values Option", 0);
		System.err.println("Closing ...");				
		cli.disposeConnections();
		System.err.println("done!");
	}
}
