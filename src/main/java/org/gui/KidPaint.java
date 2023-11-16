package org.gui;


import java.awt.*;

public class KidPaint {
	public static void main(String[] args) {
		UI ui = UI.getInstance();			// get the instance of UI



		ui.setData(new int[50][50], 16);	// set the data array and block size. comment this statement to use the default data array and block size.
		ui.setSize(new Dimension(1130, 900));
		ui.setVisible(true);				// set the ui

		UserNameInput userNameInput = UserNameInput.getInstance(ui);
		StudioSelectionPopup studioSelectionPopup = StudioSelectionPopup.getInstance(ui);

		userNameInput.setVisibleAfterClose(studioSelectionPopup);
		userNameInput.setVisible(true);  // launch the username dialog box

	}
}
