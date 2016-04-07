package org.protege.editor.owl.client.action;

import org.protege.editor.core.ui.error.ErrorLogPanel;

import java.awt.event.ActionEvent;
import java.util.concurrent.ScheduledFuture;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenuItem;

public class EnableAutoUpdateAction extends AbstractClientAction {

    private static final long serialVersionUID = 1098490684799516207L;

    private ScheduledFuture<?> autoUpdate;
    private JCheckBoxMenuItem checkBoxMenuItem;

    @Override
    public void initialise() throws Exception {
        super.initialise();
    }

    @Override
    public void dispose() throws Exception {
        super.dispose();
    }

    public void setMenuItem(JMenuItem menu) {
        checkBoxMenuItem = (JCheckBoxMenuItem) menu;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        killAutoUpdate();
        if (checkBoxMenuItem.isSelected()) {
            autoUpdate = submit(new AutoUpdate(), 15); // TODO Make the auto-update timing adjustable
        }
    }

    private void killAutoUpdate() {
        if (autoUpdate != null) {
            autoUpdate.cancel(false);
            autoUpdate = null;
        }
    }

    private class AutoUpdate implements Runnable {
        private boolean lastRunSuccessful = true;

        @Override
        public void run() {
            try {
                if (getOntologyResource().isPresent()) {
//                    getClient(). TODO: Implement update operation in the server
                }
                lastRunSuccessful = true;
            }
//            catch (UserDeclinedAuthenticationException udae) {
//                killAutoUpdate();
//                checkBoxMenuItem.setSelected(false);
//            }
            catch (Throwable t) {
                if (!lastRunSuccessful) {
                    ErrorLogPanel.showErrorDialog(t);
                }
                lastRunSuccessful = false;
            }
        }
    }
}
