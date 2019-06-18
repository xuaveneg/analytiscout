package org.leplan73.outilssgdf.gui.registredepresence;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ProgressMonitor;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import org.leplan73.outilssgdf.Consts;
import org.leplan73.outilssgdf.Progress;
import org.leplan73.outilssgdf.engine.EngineAnalyseurRegistreDePresence;
import org.leplan73.outilssgdf.gui.GuiProgress;
import org.leplan73.outilssgdf.gui.utils.Appender;
import org.leplan73.outilssgdf.gui.utils.Dialogue;
import org.leplan73.outilssgdf.gui.utils.ExportFileFilter;
import org.leplan73.outilssgdf.gui.utils.GuiCommand;
import org.leplan73.outilssgdf.gui.utils.LoggedDialog;
import org.leplan73.outilssgdf.gui.utils.Logging;
import org.leplan73.outilssgdf.gui.utils.Preferences;
import org.slf4j.LoggerFactory;

public class AnalyseRegistreDePresence extends Dialogue implements LoggedDialog, GuiCommand {

	private final JPanel contentPanel = new JPanel();
	private JFileChooser fcEntree = new JFileChooser();
	private File fEntree = new File("./données/registrepresence.csv");
	private JFileChooser fcModele = new JFileChooser();
	protected File fModele = new File("conf/modele_registrepresence.xlsx");
	private JFileChooser fcSortie = new JFileChooser();
	protected File fSortie = new File("./données/analyse_registrepresence.xlsx");
	private JLabel lblSortie;
	private JLabel lblEntree;
	private JLabel lblModele;
	private JButton btnGo;

	/**
	 * Create the dialog.
	 */
	public AnalyseRegistreDePresence() {
		super();
		logger_ = LoggerFactory.getLogger(AnalyseRegistreDePresence.class);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setTitle("Analyse registre de présence");

		Appender.setLoggedDialog(this);

		setModalityType(ModalityType.APPLICATION_MODAL);
		setResizable(false);
		double x = Preferences.litd(Consts.FENETRE_ANALYSEUR_X, 100);
		double y = Preferences.litd(Consts.FENETRE_ANALYSEUR_Y, 100);
		setBounds((int)x, (int)y, 778, 608);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		GridBagLayout gbl_contentPanel = new GridBagLayout();
		gbl_contentPanel.columnWidths = new int[] { 211, 0 };
		gbl_contentPanel.rowHeights = new int[] { 0, 0, 0, 0, 0 };
		gbl_contentPanel.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gbl_contentPanel.rowWeights = new double[] { 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE };
		contentPanel.setLayout(gbl_contentPanel);
		{
			JPanel panel = new JPanel();
			panel.setBorder(new TitledBorder(null, "Entr\u00E9e", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			GridBagConstraints gbc_panel = new GridBagConstraints();
			gbc_panel.anchor = GridBagConstraints.NORTH;
			gbc_panel.insets = new Insets(0, 0, 5, 0);
			gbc_panel.fill = GridBagConstraints.HORIZONTAL;
			gbc_panel.gridx = 0;
			gbc_panel.gridy = 0;
			contentPanel.add(panel, gbc_panel);
			panel.setLayout(new BorderLayout(0, 0));
			{
				lblEntree = new JLabel(fEntree.getAbsolutePath());
				panel.add(lblEntree, BorderLayout.WEST);
			}
			{
				JButton btnDossier = new JButton("Fichier...");
				btnDossier.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						fcEntree.setDialogTitle("Répertoire de données");
						fcEntree.setApproveButtonText("Go");
						fcEntree.setCurrentDirectory(new File("."));
						fcEntree.setFileSelectionMode(JFileChooser.FILES_ONLY);
						fcEntree.addChoosableFileFilter(new ExportFileFilter("csv"));
						int result = fcEntree.showDialog(panel, "OK");
						if (result == JFileChooser.APPROVE_OPTION) {
							fEntree = fcEntree.getSelectedFile();
							lblEntree.setText(fEntree.getPath());
						}
					}
				});
				panel.add(btnDossier, BorderLayout.EAST);
			}
		}
		{
			JPanel panel = new JPanel();
			panel.setBorder(new TitledBorder(null, "Mod\u00E8le", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			GridBagConstraints gbc_panel = new GridBagConstraints();
			gbc_panel.anchor = GridBagConstraints.NORTH;
			gbc_panel.insets = new Insets(0, 0, 5, 0);
			gbc_panel.fill = GridBagConstraints.HORIZONTAL;
			gbc_panel.gridx = 0;
			gbc_panel.gridy = 1;
			contentPanel.add(panel, gbc_panel);
			panel.setLayout(new BorderLayout(0, 0));
			{
				lblModele = new JLabel(fModele.getAbsolutePath());
				panel.add(lblModele, BorderLayout.WEST);
			}
			{
				JButton button = new JButton("Fichier...");
				button.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						fcModele.setDialogTitle("Fichier modèle");
						fcModele.setApproveButtonText("Go");
						fcModele.setCurrentDirectory(new File("."));
						fcModele.setSelectedFile(fModele);
						fcModele.setFileSelectionMode(JFileChooser.FILES_ONLY);
						fcModele.removeChoosableFileFilter(fcModele.getFileFilter());
						fcModele.removeChoosableFileFilter(fcModele.getAcceptAllFileFilter());
						fcModele.addChoosableFileFilter(new ExportFileFilter("xlsx"));
						int result = fcModele.showDialog(panel, "OK");
						if (result == JFileChooser.APPROVE_OPTION) {
							fModele = fcModele.getSelectedFile();
							lblModele.setText(fModele.getPath());
						}
					}
				});
				panel.add(button, BorderLayout.EAST);
			}
		}
		{
			JPanel panel = new JPanel();
			panel.setBorder(new TitledBorder(null, "Sortie", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			GridBagConstraints gbc_panel = new GridBagConstraints();
			gbc_panel.anchor = GridBagConstraints.NORTH;
			gbc_panel.insets = new Insets(0, 0, 5, 0);
			gbc_panel.fill = GridBagConstraints.HORIZONTAL;
			gbc_panel.gridx = 0;
			gbc_panel.gridy = 2;
			contentPanel.add(panel, gbc_panel);
			panel.setLayout(new BorderLayout(0, 0));
			{
				lblSortie = new JLabel(fSortie.getAbsolutePath());
				panel.add(lblSortie, BorderLayout.WEST);
			}
			{
				JButton button = new JButton("Fichier...");
				button.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						fcSortie.setDialogTitle("Export Configuration");
						fcSortie.setApproveButtonText("Export");
						fcSortie.setCurrentDirectory(new File("."));
						fcSortie.setFileSelectionMode(JFileChooser.FILES_ONLY);
						fcSortie.removeChoosableFileFilter(fcSortie.getFileFilter());
						fcSortie.removeChoosableFileFilter(fcSortie.getAcceptAllFileFilter());
						fcSortie.addChoosableFileFilter(new ExportFileFilter("xlsx"));
						int result = fcSortie.showDialog(panel, "OK");
						if (result == JFileChooser.APPROVE_OPTION) {
							fSortie = fcSortie.getSelectedFile();
							lblSortie.setText(fSortie.getPath());
						}
					}
				});
				panel.add(button, BorderLayout.EAST);
			}
		}
		{
			JPanel panel = new JPanel();
			panel.setBorder(new TitledBorder(null, "Logs", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			GridBagConstraints gbc_panel = new GridBagConstraints();
			gbc_panel.fill = GridBagConstraints.BOTH;
			gbc_panel.gridx = 0;
			gbc_panel.gridy = 3;
			contentPanel.add(panel, gbc_panel);
			panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
			{
				JScrollPane scrollPane = new JScrollPane();
				panel.add(scrollPane);
				{
					txtLog = new JTextArea();
					scrollPane.setViewportView(txtLog);
					txtLog.setEditable(false);
				}
			}
		}
		{
			JPanel buttonPane = new JPanel();
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			buttonPane.setLayout(new BorderLayout(0, 0));
			{
				{
					JPanel panel = new JPanel();
					buttonPane.add(panel, BorderLayout.EAST);
					{
						btnGo = new JButton("Go");
						panel.add(btnGo);
						JButton btnFermer = new JButton("Fermer");
						panel.add(btnFermer);
						{
							JPanel panel_1 = new JPanel();
							buttonPane.add(panel_1, BorderLayout.WEST);
							{
								JButton btnAide = new JButton("Aide");
								btnAide.setEnabled(false);
								btnAide.addActionListener(new ActionListener() {
									public void actionPerformed(ActionEvent e) {
									}
								});
								panel_1.add(btnAide);
							}
						}
						btnFermer.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent arg0) {
								dispose();
							}
						});
						btnGo.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent arg0) {
								go();
							}
						});
					}
				}
			}
		}
	}

	@Override
	public boolean check() {
		logger_.info("Vérification des paramètres");
		if (fEntree == null) {
			logger_.error("Entrée non-sélectionnée");
			return false;
		}
		if (fModele == null) {
			logger_.error("Modèle non-sélectionnée");
			return false;
		}
		if (fSortie == null) {
			logger_.error("Sortie non-sélectionnée");
			return false;
		}
		return true;
	}

	@Override
	public void go() {
		ProgressMonitor guiprogress = new ProgressMonitor(this, "Analyseur", "", 0, 100);
		
		Progress progress = new GuiProgress(guiprogress);
		progress.setMillisToPopup(0);
		progress.setMillisToDecideToPopup(0);
		
		EngineAnalyseurRegistreDePresence en = new EngineAnalyseurRegistreDePresence(progress, logger_);

		new Thread(() -> {
			progress.setProgress(0);
			txtLog.setText("");
			btnGo.setEnabled(false);

			boolean ret = check();
			progress.setProgress(20);
			if (ret) {
				logger_.info("Lancement");
				try {
					en.go(fEntree, fSortie, fModele);
				} catch (Exception e) {
					logger_.error(Logging.dumpStack(null, e));
				}
			}
			btnGo.setEnabled(true);
		}).start();
	}

	@Override
	public void dispose() {
		Appender.setLoggedDialog(null);
		Preferences.sauved(Consts.FENETRE_ANALYSEUR_X, this.getLocation().getX());
		Preferences.sauved(Consts.FENETRE_ANALYSEUR_Y, this.getLocation().getY());
		super.dispose();
	}

	@Override
	public void addLog(String message) {
		String texte = txtLog.getText();
		if (texte.length() > 0)
			txtLog.append("\n");
		txtLog.append(message);
		txtLog.setCaretPosition(txtLog.getDocument().getLength());
	}

	public JLabel getLblSortie() {
		return lblSortie;
	}

	public JLabel getLblEntree() {
		return lblEntree;
	}

	public JLabel getLblModele() {
		return lblModele;
	}

	public JTextArea getTxtLog() {
		return txtLog;
	}

	public JButton getBtnGo() {
		return btnGo;
	}
}