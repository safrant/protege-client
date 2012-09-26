package org.protege.editor.owl.client.action;

import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import org.protege.editor.core.ProtegeApplication;
import org.protege.editor.owl.client.connect.ServerConnectionManager;
import org.protege.editor.owl.client.panel.ChangeListTableModel;
import org.protege.editor.owl.ui.action.ProtegeOWLAction;
import org.protege.editor.owl.ui.renderer.OWLCellRenderer;
import org.protege.owl.server.api.Client;
import org.protege.owl.server.api.VersionedOntologyDocument;
import org.protege.owl.server.api.exception.UserDeclinedAuthenticationException;
import org.protege.owl.server.util.ClientUtilities;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyChange;

public class ShowUncommittedChangesAction extends ProtegeOWLAction {
    private static final long serialVersionUID = -7628375950917155764L;

    @Override
    public void initialise() throws Exception {

    }

    @Override
    public void dispose() throws Exception {

    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
        try {
            ServerConnectionManager connectionManager = ServerConnectionManager.get(getOWLEditorKit());
            OWLOntology ontology = getOWLModelManager().getActiveOntology();
            VersionedOntologyDocument vont = connectionManager.getVersionedOntology(ontology);
            if (vont != null) {
                Client client = connectionManager.createClient(ontology);
                List<OWLOntologyChange> uncommitted = ClientUtilities.getUncommittedChanges(client, vont);
                if (uncommitted.isEmpty()) {
                	JOptionPane.showMessageDialog(getOWLWorkspace(), "No uncommitted changes");
                } else {
                    ChangeListTableModel tableModel = new ChangeListTableModel(uncommitted);
                    JTable table = new JTable(tableModel);
                    table.setDefaultRenderer(OWLObject.class, new OWLCellRenderer(getOWLEditorKit()));
                    JScrollPane pane = new JScrollPane(table);
                    JOptionPane.showMessageDialog(getOWLWorkspace(), pane);
                }
            }
            else {
                JOptionPane.showMessageDialog(getOWLWorkspace(), "Active Ontology is not connected to a server.");
            }
        }
        catch (UserDeclinedAuthenticationException udae) {
            ; // ignore this because the user knows that he didn't authenticate.
        }
        catch (Exception e) {
            ProtegeApplication.getErrorLog().logError(e);
        }
    }

}
