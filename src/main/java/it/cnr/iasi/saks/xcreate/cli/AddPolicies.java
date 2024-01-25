/* 
 * This file is part of the XCREATE-CLI project.
 * 
 * XCREATE-CLI is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * XCREATE-CLI is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with XCREATE-CLI.  If not, see <https://www.gnu.org/licenses/>
 *
 */
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
