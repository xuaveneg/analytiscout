package org.leplan73.analytiscout.gui.analyseurenligne;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ProgressMonitor;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import org.leplan73.analytiscout.Consts;
import org.leplan73.analytiscout.Options;
import org.leplan73.analytiscout.ParamSortie;
import org.leplan73.analytiscout.Params;
import org.leplan73.analytiscout.Progress;
import org.leplan73.analytiscout.engine.EngineAnalyseurEnLigne;
import org.leplan73.analytiscout.gui.GuiProgress;
import org.leplan73.analytiscout.gui.utils.Appender;
import org.leplan73.analytiscout.gui.utils.BoutonOuvrir;
import org.leplan73.analytiscout.gui.utils.Dialogue;
import org.leplan73.analytiscout.gui.utils.ExportFileFilter;
import org.leplan73.analytiscout.gui.utils.GuiCommand;
import org.leplan73.analytiscout.gui.utils.LoggedDialog;
import org.leplan73.analytiscout.gui.utils.Logging;
import org.leplan73.analytiscout.gui.utils.Preferences;
import org.leplan73.analytiscout.outils.ResetableFileInputStream;
import org.slf4j.Logger;

@SuppressWarnings("serial")
abstract public class AnalyseurEnLigne extends Dialogue implements LoggedDialog, GuiCommand {

	private final JPanel contentPanel = new JPanel();
	private JFileChooser fcSortieFichier;
	private JFileChooser fcSortieRepertoire;
	protected File fSortieFichier = new File("données/analyse.xlsx");
	protected File fSortieRepertoire = new File("données");
	protected File fBatch = new File("conf/batch_responsables.txt");
	protected File fModele = new File("conf/modele_responsables.xlsx");
	protected String nomFichier_;
	
	/**
	 * Create the dialog.
	 * @param fModele 
	 * @param fBatch 
	 * @param fSortieFichier 
	 * @param logger 
	 * @throws URISyntaxException 
	 */
	public AnalyseurEnLigne(String titre, Logger logger, File pfSortieFichier, File pfSortieRepertoire, String nomFichier, File pfBatch, File pfModele, boolean cacherDdcs, boolean cacherOptions) {
		super();
		this.logger_ = logger;
		this.fSortieFichier = pfSortieFichier;
		this.fSortieRepertoire = pfSortieRepertoire;
		this.fBatch = pfBatch;
		this.fModele = pfModele;
		this.nomFichier_ = nomFichier;
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		
		Appender.setLoggedDialog(this);

		setResizable(false);
		setTitle(titre);
		setModalityType(ModalityType.APPLICATION_MODAL);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		double x = Preferences.litd(Consts.FENETRE_ANALYSEURENLIGNE_X, 100);
		double y = Preferences.litd(Consts.FENETRE_ANALYSEURENLIGNE_Y, 100);
		setBounds((int)x, (int)y, 726, 627);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		GridBagLayout gbl_contentPanel = new GridBagLayout();
		gbl_contentPanel.columnWidths = new int[] { 121, 0 };
		gbl_contentPanel.rowHeights = new int[] { 0, 0, 0, 0, 0 };
		gbl_contentPanel.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gbl_contentPanel.rowWeights = new double[] { 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE };
		contentPanel.setLayout(gbl_contentPanel);
		{
			JPanel panel = new JPanel();
			panel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Code structure", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
			GridBagConstraints gbc_panel = new GridBagConstraints();
			gbc_panel.anchor = GridBagConstraints.NORTH;
			gbc_panel.insets = new Insets(0, 0, 5, 0);
			gbc_panel.fill = GridBagConstraints.HORIZONTAL;
			gbc_panel.gridx = 0;
			gbc_panel.gridy = 0;
			contentPanel.add(panel, gbc_panel);
			panel.setLayout(new BorderLayout(0, 0));
			{
				txfCodeStructure = new JTextField();
				txfCodeStructure.setColumns(30);
				txfCodeStructure.setText(Preferences.lit(Consts.INTRANET_STRUCTURE, "", true));
				panel.add(txfCodeStructure, BorderLayout.NORTH);
			}
		}
		{
			JPanel panelOptions = new JPanel();
			panelOptions.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Options", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
			GridBagConstraints gbc_panelOptions = new GridBagConstraints();
			gbc_panelOptions.insets = new Insets(0, 0, 5, 0);
			gbc_panelOptions.anchor = GridBagConstraints.NORTH;
			gbc_panelOptions.fill = GridBagConstraints.HORIZONTAL;
			gbc_panelOptions.gridx = 0;
			gbc_panelOptions.gridy = 1;
			contentPanel.add(panelOptions, gbc_panelOptions);
			panelOptions.setLayout(new BorderLayout(0, 0));
			{
				chkAge = new JCheckBox("Gestion de l'âge");
				panelOptions.add(chkAge, BorderLayout.NORTH);
			}
			{
				chcDdcs = new JCheckBox("Générer les informations d'aide à la déclaration DDCS");
				panelOptions.add(chcDdcs, BorderLayout.CENTER);
			}
			{
				chcPreinscrits = new JCheckBox("Inclure les adhérents pré-inscrits");
				panelOptions.add(chcPreinscrits, BorderLayout.SOUTH);
			}
			
			if (cacherOptions)
			{
				panelOptions.setVisible(false);
			}
			if (cacherDdcs)
			{
				chcDdcs.setVisible(false);
			}
		}
		{
			JPanel panel = new JPanel();
			panel.setBorder(new TitledBorder(null, "Sortie", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			GridBagConstraints gbc_panel = new GridBagConstraints();
			gbc_panel.insets = new Insets(0, 0, 5, 0);
			gbc_panel.anchor = GridBagConstraints.NORTH;
			gbc_panel.fill = GridBagConstraints.HORIZONTAL;
			gbc_panel.gridx = 0;
			gbc_panel.gridy = 2;
			contentPanel.add(panel, gbc_panel);
			panel.setLayout(new BorderLayout(0, 0));
			{
				lblSortie = new JLabel(fSortieFichier.getAbsolutePath());
				panel.add(lblSortie, BorderLayout.WEST);
			}
			{
				JPanel panel_1 = new JPanel();
				panel_1.setBorder(null);
				panel.add(panel_1, BorderLayout.EAST);
				panel_1.setLayout(new BorderLayout(0, 0));
				{
					chkGenererParGroupe = new JCheckBox("Générer un fichier par groupe");
					chkGenererParGroupe.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							btnFichier.setText(chkGenererParGroupe.isSelected() ? "Répertoire..." : "Fichier...");
							lblSortie.setText(chkGenererParGroupe.isSelected() ? fSortieRepertoire.getPath() : fSortieFichier.getPath());
						}
					});
					panel_1.add(chkGenererParGroupe, BorderLayout.NORTH);
				}
				{
					btnFichier = new JButton("Fichier...");
					panel_1.add(btnFichier, BorderLayout.CENTER);
					{
						btnOuvrir = new BoutonOuvrir("Ouvrir...", lblSortie);
						btnOuvrir.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								try {
									btnOuvrir.ouvrir();
								} catch (Exception ex) {
									logger_.error(Logging.dumpStack(null, ex));
								}
							}
						});
						panel_1.add(btnOuvrir, BorderLayout.EAST);
					}
					btnFichier.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							if (chkGenererParGroupe.isSelected())
							{
								fcSortieRepertoire = new JFileChooser();
								fcSortieRepertoire.setDialogTitle("Répertoire de sortie");
								fcSortieRepertoire.setApproveButtonText("Export");
								fcSortieRepertoire.setCurrentDirectory(fSortieRepertoire);
								fcSortieRepertoire.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
								int resultRepertoire = fcSortieRepertoire.showDialog(panel, "OK");
								if (resultRepertoire == JFileChooser.APPROVE_OPTION) {
									fSortieRepertoire = fcSortieRepertoire.getSelectedFile();
									String path = fSortieRepertoire.getPath();
									lblSortie.setText(path);
									btnOuvrir.maj();
								}
							}
							else
							{
								fcSortieFichier = new JFileChooser();
								fcSortieFichier.setDialogTitle("Fichier de sortie");
								fcSortieFichier.setApproveButtonText("Export");
								fcSortieFichier.setCurrentDirectory(fSortieFichier.getParentFile());
								fcSortieFichier.setFileSelectionMode(JFileChooser.FILES_ONLY);
								fcSortieFichier.removeChoosableFileFilter(fcSortieFichier.getFileFilter());
								fcSortieFichier.removeChoosableFileFilter(fcSortieFichier.getAcceptAllFileFilter());
								fcSortieFichier.addChoosableFileFilter(new ExportFileFilter("xlsx"));
								int result = fcSortieFichier.showDialog(panel, "OK");
								if (result == JFileChooser.APPROVE_OPTION) {
									fSortieFichier = ajouteExtensionFichier(fcSortieFichier, lblSortie, "xlsx");
									btnOuvrir.maj();
								}
							}
						}
					});
				}
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
			panel.setLayout(new BorderLayout(0, 0));
			{
				JScrollPane scrollPane = new JScrollPane();
				panel.add(scrollPane);
				{
					txtLog = new JTextArea();
					scrollPane.setViewportView(txtLog);
					{
						chkGarderFichiers = new JCheckBox("Garder les fichiers téléchargés");
						panel.add(chkGarderFichiers, BorderLayout.SOUTH);
					}
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
					btnGo = new JButton("Go");
					panel.add(btnGo);
					{
						JButton btnFermer = new JButton("Fermer");
						panel.add(btnFermer);
						btnFermer.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								dispose();
							}
						});
					}
					btnGo.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							go();
						}
					});
				}
				{
					JPanel panel = new JPanel();
					buttonPane.add(panel, BorderLayout.WEST);
					{
						JButton btnAide = new JButton("Aide");
						btnAide.setEnabled(false);
						panel.add(btnAide);
					}
				}
			}
		}
	}

	private JLabel lblSortie;
	private JTextField txfCodeStructure;
	private JButton btnGo;
	private JCheckBox chkAge;
	private BoutonOuvrir btnOuvrir;
	private JCheckBox chkGarderFichiers;
	private JCheckBox chkGenererParGroupe;
	private JButton btnFichier;
	private JCheckBox chcDdcs;
	private JCheckBox chcPreinscrits;

	@Override
	public boolean check() {
		logger_.info("Vérification des paramètres");
		if (fBatch == null) {
			logger_.error("Le fichier batch est non-sélectionnée");
			return false;
		}
		if (fSortieFichier == null) {
			logger_.error("Le répertoire de sortie est non-sélectionnée");
			return false;
		}
		return true;
	}
	
	public boolean check(JTextField txfStructure) {
		if (check() == false) return false;
		structure_ = txfCodeStructure.getText();
		return checkIntranet();
	}

	@Override
	public void go() {
		
		ProgressMonitor guiprogress = new ProgressMonitor(this, "AnalyseurEnLigne", "", 0, 100);
		
		Progress progress = new GuiProgress(guiprogress, this.getTitle());
		progress.setMillisToPopup(0);
		progress.setMillisToDecideToPopup(0);
		
		Options options = new Options();
		if (chcPreinscrits.isSelected()) options.add(Options.OPTION_PREINSCRITS);
		
		new Thread(() -> {
			initLog();
			boolean ret = check(txfCodeStructure);
			if (ret) {
				try {
					int structures[] = construitStructures();
					EngineAnalyseurEnLigne en = new EngineAnalyseurEnLigne(progress, logger_);
					ParamSortie psortie = chkGenererParGroupe.isSelected() ? new ParamSortie(fSortieRepertoire, true, nomFichier_) : new ParamSortie(fSortieFichier);
					en.go(identifiant_, motdepasse_, new ResetableFileInputStream(new FileInputStream(fBatch)), new ResetableFileInputStream(new FileInputStream(fModele)), structures, chkAge.isSelected(), "tout_responsables", true, psortie ,Params.getb(Consts.PARAMS_ANONYMISER, false), chkGarderFichiers.isSelected(), chkGenererParGroupe.isSelected(), chcDdcs.isSelected(), options);
					btnOuvrir.maj();
				} catch (Exception e) {
					logger_.error(Logging.dumpStack(null, e));
				}
			}
		}).start();
	}

	@Override
	public void dispose() {
		Appender.setLoggedDialog(null);
		Preferences.sauved(Consts.FENETRE_ANALYSEURENLIGNE_X, this.getLocation().getX());
		Preferences.sauved(Consts.FENETRE_ANALYSEURENLIGNE_Y, this.getLocation().getY());
		Preferences.sauve(Consts.REPERTOIRE_SORTIE, chkGenererParGroupe.isSelected() ? this.fSortieRepertoire.getPath() : this.fSortieFichier.getAbsoluteFile().getParent(), false);
		super.dispose();
	}

	public JLabel getLblSortie() {
		return lblSortie;
	}

	public JTextField getTfStructure() {
		return txfCodeStructure;
	}

	public JButton getBtnGo() {
		return btnGo;
	}
	
	public JCheckBox getChkAge() {
		return chkAge;
	}
	public BoutonOuvrir getBtnOuvrir() {
		return btnOuvrir;
	}
	protected JButton getBtnFichier() {
		return btnFichier;
	}
	protected JCheckBox getChcDdcs() {
		return chcDdcs;
	}
	protected JCheckBox getChcPreinscrits() {
		return chcPreinscrits;
	}
}
