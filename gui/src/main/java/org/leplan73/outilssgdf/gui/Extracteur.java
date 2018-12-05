package org.leplan73.outilssgdf.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.time.Instant;
import java.util.Iterator;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ProgressMonitor;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.http.client.ClientProtocolException;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;
import org.leplan73.outilssgdf.intranet.ExtractionAdherents;
import org.leplan73.outilssgdf.intranet.ExtractionMain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Extracteur extends JDialog implements LoggedDialog,GuiCommand {

	private static final String ENCODING_WINDOWS = "Windows-1252";
	private static final String ENCODING_UTF8 = "UTF-8";
	
	private final JPanel contentPanel = new JPanel();
	private JTextField txfIdentifiant;
	private JPasswordField txfMotdepasse;
	private JTextField txfStructure;
	private JTextField txfCodefonction;
	private JComboBox cbxGenerateur;
	private Logger logger_ = LoggerFactory.getLogger(Extracteur.class);
	
	private JFileChooser fcSortie;
	private JLabel lblSortie;
	private JTextArea txtLog;
	private JCheckBox chkFormatIndividu;
	private JCheckBox chkFormatParents;
	private JCheckBox chkFormatInscription;
	private JCheckBox chkFormatAdhesion;
	private JCheckBox chkFormatJS;
	private JCheckBox chkFormatSansQF;
	private JComboBox cbxTypeinscription;
	private JComboBox cbxType;
	private JComboBox cbxCategorie;
	private JComboBox cbxSpecialite;
	private JCheckBox chkAdherents;
	private JComboBox cbxFormation;
	private JComboBox cbxQualification;
	private JComboBox cbxDiplome;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			Extracteur dialog = new Extracteur();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public Extracteur() {
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		
		Appender.setLoggedDialog(this);
		
		setModalityType(ModalityType.APPLICATION_MODAL);
		setResizable(false);
		setTitle("Extracteur");
		setBounds(100, 100, 676, 678);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		GridBagLayout gbl_contentPanel = new GridBagLayout();
		gbl_contentPanel.columnWidths = new int[]{0, 0};
		gbl_contentPanel.rowHeights = new int[]{0, 0, 204, 0, 63, 0, 0};
		gbl_contentPanel.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_contentPanel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		contentPanel.setLayout(gbl_contentPanel);
		{
			JPanel panel = new JPanel();
			GridBagConstraints gbc_panel = new GridBagConstraints();
			gbc_panel.anchor = GridBagConstraints.NORTH;
			gbc_panel.insets = new Insets(0, 0, 5, 0);
			gbc_panel.fill = GridBagConstraints.HORIZONTAL;
			gbc_panel.gridx = 0;
			gbc_panel.gridy = 0;
			contentPanel.add(panel, gbc_panel);
			panel.setLayout(new BorderLayout(0, 0));
			{
				JPanel panel_1 = new JPanel();
				panel_1.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Identifiant", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
				panel.add(panel_1, BorderLayout.WEST);
				panel_1.setLayout(new BoxLayout(panel_1, BoxLayout.X_AXIS));
				{
					txfIdentifiant = new JTextField();
					panel_1.add(txfIdentifiant);
					txfIdentifiant.setColumns(30);
				}
			}
			{
				JPanel panel_1 = new JPanel();
				panel_1.setBorder(new TitledBorder(null, "Mot de passe", TitledBorder.LEADING, TitledBorder.TOP, null, null));
				panel.add(panel_1, BorderLayout.CENTER);
				panel_1.setLayout(new BoxLayout(panel_1, BoxLayout.X_AXIS));
				{
					txfMotdepasse = new JPasswordField();
					txfMotdepasse.setColumns(10);
					panel_1.add(txfMotdepasse);
				}
			}
			{
				checkBox = new JCheckBox("Mémoriser");
				panel.add(checkBox, BorderLayout.EAST);
			}
		}
		{
			JPanel panel = new JPanel();
			GridBagConstraints gbc_panel = new GridBagConstraints();
			gbc_panel.anchor = GridBagConstraints.NORTH;
			gbc_panel.insets = new Insets(0, 0, 5, 0);
			gbc_panel.fill = GridBagConstraints.HORIZONTAL;
			gbc_panel.gridx = 0;
			gbc_panel.gridy = 1;
			contentPanel.add(panel, gbc_panel);
			panel.setLayout(new BorderLayout(0, 0));
			{
				JPanel panel_1 = new JPanel();
				panel_1.setBorder(new TitledBorder(null, "Structure", TitledBorder.LEADING, TitledBorder.TOP, null, null));
				panel.add(panel_1, BorderLayout.WEST);
				panel_1.setLayout(new BoxLayout(panel_1, BoxLayout.X_AXIS));
				{
					txfStructure = new JTextField();
					panel_1.add(txfStructure);
					txfStructure.setColumns(30);
				}
			}
			{
				JPanel panel_1 = new JPanel();
				panel_1.setBorder(new TitledBorder(null, "Code fonction", TitledBorder.LEADING, TitledBorder.TOP, null, null));
				panel.add(panel_1, BorderLayout.CENTER);
				panel_1.setLayout(new BoxLayout(panel_1, BoxLayout.X_AXIS));
				{
					txfCodefonction = new JTextField();
					panel_1.add(txfCodefonction);
					txfCodefonction.setColumns(10);
				}
			}
		}
		{
			JPanel panel = new JPanel();
			panel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Filtrage", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
			GridBagConstraints gbc_panel = new GridBagConstraints();
			gbc_panel.anchor = GridBagConstraints.NORTH;
			gbc_panel.insets = new Insets(0, 0, 5, 0);
			gbc_panel.fill = GridBagConstraints.HORIZONTAL;
			gbc_panel.gridx = 0;
			gbc_panel.gridy = 2;
			contentPanel.add(panel, gbc_panel);
			GridBagLayout gbl_panel = new GridBagLayout();
			gbl_panel.columnWidths = new int[]{141, 207, 134, 0};
			gbl_panel.rowHeights = new int[]{56, 0, 0, 0};
			gbl_panel.columnWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
			gbl_panel.rowWeights = new double[]{1.0, 1.0, 0.0, Double.MIN_VALUE};
			panel.setLayout(gbl_panel);
			{
				JPanel panel_1 = new JPanel();
				panel_1.setBorder(new TitledBorder(null, "Dipl\u00F4me", TitledBorder.LEADING, TitledBorder.TOP, null, null));
				GridBagConstraints gbc_panel_1 = new GridBagConstraints();
				gbc_panel_1.anchor = GridBagConstraints.WEST;
				gbc_panel_1.insets = new Insets(0, 0, 5, 5);
				gbc_panel_1.fill = GridBagConstraints.VERTICAL;
				gbc_panel_1.gridx = 0;
				gbc_panel_1.gridy = 0;
				panel.add(panel_1, gbc_panel_1);
				{
					cbxDiplome = new JComboBox();
					cbxDiplome.setModel(new DefaultComboBoxModel(new String[] {"Sans importance", "2 Buchettes (185)", "3 Buchettes (612)", "4 Buchettes (613)", "AFPS (619)", "BAFA (73)", "BAFA Qualification Voile (607)", "BAFD (140)", "BP JEPS (618)", "Brevet d'état (activités marines) (615)", "Brevet Professionnel (Skipper, marine marchande) (617)", "Carte Mer (616)", "Certificat Radio Restreint CRR (609)", "Chef de Flottille (606)", "Chef de quart (605)", "DE JEPS (626)", "DEUG STAPS (628)", "Directeur nautique (624)", "DUT animation socioculturelle  (614)", "Licence STAPS (630)", "Médaille Argent de la JS (622)", "Médaille Bronze de la JS (621)", "Médaille Or de la JS (623)", "Nœud de tisserand (629)", "Patron d'embarcation (604)", "Permis Côtier (610)", "Permis E (625)", "Permis fluvial (627)", "Permis Hauturier (611)", "PSC1 Prévention et Secours Civiques de niveau 1 (603)", "Spirale AMGE (620)", "Surveillant de Baignade (608)"}));
					panel_1.add(cbxDiplome);
				}
			}
			{
				JPanel panel_1 = new JPanel();
				panel_1.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Type d'inscription", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
				GridBagConstraints gbc_panel_1 = new GridBagConstraints();
				gbc_panel_1.anchor = GridBagConstraints.WEST;
				gbc_panel_1.insets = new Insets(0, 0, 5, 5);
				gbc_panel_1.fill = GridBagConstraints.VERTICAL;
				gbc_panel_1.gridx = 1;
				gbc_panel_1.gridy = 0;
				panel.add(panel_1, gbc_panel_1);
				{
					cbxTypeinscription = new JComboBox();
					cbxTypeinscription.setModel(new DefaultComboBoxModel(new String[] {"Toutes (-1)", "Inscrit(e) (0)", "Invité(e) (1)", "Pré-inscrit(e) (2)"}));
					panel_1.add(cbxTypeinscription);
				}
			}
			{
				JPanel panel_1 = new JPanel();
				panel_1.setBorder(new TitledBorder(null, "Type", TitledBorder.LEADING, TitledBorder.TOP, null, null));
				GridBagConstraints gbc_panel_1 = new GridBagConstraints();
				gbc_panel_1.anchor = GridBagConstraints.WEST;
				gbc_panel_1.insets = new Insets(0, 0, 5, 0);
				gbc_panel_1.fill = GridBagConstraints.VERTICAL;
				gbc_panel_1.gridx = 2;
				gbc_panel_1.gridy = 0;
				panel.add(panel_1, gbc_panel_1);
				{
					cbxType = new JComboBox();
					cbxType.setModel(new DefaultComboBoxModel(new String[] {"tout (-1)", "inscrit (0)", "invite (1)", "pré-inscrit (2)"}));
					panel_1.add(cbxType);
				}
			}
			{
				JPanel panel_1 = new JPanel();
				panel_1.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Qualification", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
				GridBagConstraints gbc_panel_1 = new GridBagConstraints();
				gbc_panel_1.anchor = GridBagConstraints.WEST;
				gbc_panel_1.insets = new Insets(0, 0, 5, 5);
				gbc_panel_1.fill = GridBagConstraints.VERTICAL;
				gbc_panel_1.gridx = 0;
				gbc_panel_1.gridy = 1;
				panel.add(panel_1, gbc_panel_1);
				{
					cbxQualification = new JComboBox();
					cbxQualification.setModel(new DefaultComboBoxModel(new String[] {"Sans importance (-1)", "Animateur SF (CAFASF)", "Directeur SF (CAFDSF)", "Responsable Unité SF (CAFRUSF)"}));
					panel_1.add(cbxQualification);
				}
			}
			{
				JPanel panel_1 = new JPanel();
				panel_1.setBorder(new TitledBorder(null, "Sp\u00E9cialit\u00E9", TitledBorder.LEADING, TitledBorder.TOP, null, null));
				GridBagConstraints gbc_panel_1 = new GridBagConstraints();
				gbc_panel_1.anchor = GridBagConstraints.WEST;
				gbc_panel_1.insets = new Insets(0, 0, 5, 5);
				gbc_panel_1.fill = GridBagConstraints.VERTICAL;
				gbc_panel_1.gridx = 1;
				gbc_panel_1.gridy = 1;
				panel.add(panel_1, gbc_panel_1);
				{
					cbxSpecialite = new JComboBox();
					cbxSpecialite.setModel(new DefaultComboBoxModel(new String[] {"Sans importance (-1)", "Marine (622)", "Sans spécialité (624)", "Vent du Large (623)"}));
					panel_1.add(cbxSpecialite);
				}
			}
			{
				JPanel panel_1 = new JPanel();
				panel_1.setBorder(new TitledBorder(null, "Cat\u00E9gorie", TitledBorder.LEADING, TitledBorder.TOP, null, null));
				GridBagConstraints gbc_panel_1 = new GridBagConstraints();
				gbc_panel_1.insets = new Insets(0, 0, 5, 0);
				gbc_panel_1.anchor = GridBagConstraints.WEST;
				gbc_panel_1.fill = GridBagConstraints.VERTICAL;
				gbc_panel_1.gridx = 2;
				gbc_panel_1.gridy = 1;
				panel.add(panel_1, gbc_panel_1);
				{
					cbxCategorie = new JComboBox();
					cbxCategorie.setModel(new DefaultComboBoxModel(new String[] {"Toutes (-1)", "Jeune (0)", "Responsable (1)"}));
					panel_1.add(cbxCategorie);
				}
			}
			{
				JPanel panel_1 = new JPanel();
				panel_1.setBorder(new TitledBorder(null, "Formation", TitledBorder.LEADING, TitledBorder.TOP, null, null));
				GridBagConstraints gbc_panel_1 = new GridBagConstraints();
				gbc_panel_1.anchor = GridBagConstraints.WEST;
				gbc_panel_1.insets = new Insets(0, 0, 0, 5);
				gbc_panel_1.fill = GridBagConstraints.VERTICAL;
				gbc_panel_1.gridx = 0;
				gbc_panel_1.gridy = 2;
				panel.add(panel_1, gbc_panel_1);
				{
					cbxFormation = new JComboBox();
					cbxFormation.setModel(new DefaultComboBoxModel(new String[] {"Sans importance  (-1)", "AFPS (18)", "AIDE A LA PRISE DE FONCTION (219)", "APF AUMONIER ET AVSC (265)", "APF CHEFS - CHEFTAINES (248)", "APF RESPONSABLE DE GROUPE (250)", "APF RESPONSABLE LOCAL DEVELOPPEMENT ET RESEAUX (263)", "APF SECRETAIRE TRESORIER (251)", "APPRO (53)", "APPRO ACCUEIL DE SCOUTISME (243)", "APPRO ANIMATION (244)", "APPRO KAYAK (229)", "APPRO SURVEILLANT DE BAIGNADE (247)", "APPRO TERRE ET MER (246)", "ASSISES DES FORMATEURS (291)", "AUTRE STAGE (220)", "BAFA APPROFONDISSEMENT (245)", "BAFA FORMATION GENERALE (242)", "BAFD FORMATION GENERALE (274)", "BAFD PERFECTIONNEMENT (282)", "CEP 1 (232)", "CEP 2 (233)", "CHAM (77)", "CHEF DE FLOTTILLE (261)", "CHEF DE QUART (260)", "FAC (256)", "FIAC (255)", "FIRPAF (269)", "FIRPDEV (270)", "FIRPP (268)", "FORMATION A LA COMMUNICATION (281)", "FORMATION ACCUEIL DE SCOUTISME RG (286)", "FORMATION COMPAGNON 1ER TEMPS (276)", "FORMATION COMPAGNON 2EME TEMPS (277)", "FORMATION COMPLEMENTAIRE DE FORMATEUR (295)", "FORMATION CONTINUE DE DIRECTEURS (280)", "FORMATION CONTINUE DE FORMATEURS (279)", "FORMATION CONTINUE DES RESPONSABLES DE GROUPE (289)", "FORMATION CONTINUE VENT DU LARGE (298)", "FORMATION DE DIRECTEUR DE STAGE (222)", "FORMATION DEPART A L'ETRANGER (225)", "FORMATION DES ACCOMPAGNATEURS COMPAGNONS (95)", "FORMATION DES AUMONIERS ET ANIMATEURS CLEOPHAS (240)", "FORMATION DES RESPONSABLES FARFADETS (252)", "FORMATION DEVELOPPEMENT ET RESEAUX (264)", "FORMATION GENERALE RESPONSABLE DE GROUPE (224)", "FORMATION INITIALE ACCOMPAGNATEURS PEDAGOGIQUES (257)", "FORMATION INITIALE DE FORMATEURS (223)", "FORMATION INITIALE DES AUMONIERS (284)", "FORMATION INITIALE DES AVSC (283)", "FORMATION INITIALE DES DELEGUES TERRITORIAUX (293)", "FORMATION INITIALE DES EQUIPIERS NATIONAUX (266)", "FORMATION INITIALE DES EQUIPIERS TERRITORIAUX (266)", "FORMATION INITIALE DES MEDIATEURS (290)", "FORMATION INITIALE DES POLES ADMIN ET FINANCIER (285)", "FORMATION INITIALE DES POLES DEVELOPPEMENT (272)", "FORMATION INITIALE DES RESPONSABLES DE GROUPE (287)", "FORMATION INITIALE DES RESPONSABLES DE POLES (275)", "FORMATION INITIALE TRESORIER-SECRETAIRE (68)", "FORMATION INITIALE VENT DU LARGE (294)", "FORMATION JEUNES ADULTES - DEPART A L'ETRANGER (254)", "FORMATION SERVICE CIVIQUE (288)", "FORMATION SPECIFIQUE MARINE (230)", "MASTERCLASS (258)", "MODULE ANIMATEUR DE SCOUTISME ET CAMPISME (278)", "MODULE APPRO ACCUEIL DE SCOUTISME (221)", "MODULE APPRO ANIMATION (241)", "MODULE APPRO CONSTRUCTION-FABRICATION (267)", "MODULE APPRO CUISINE EN CAMPS (299)", "MODULE APPRO EXPRESSION ARTISTIQUE (271)", "MODULE APPRO HANDICAP (273)", "MODULE APPRO RENCONTRES INTERNATIONALES (226)", "MODULE DE FORMATION CONTINUE DE FORMATEURS (297)", "MODULE SPECIFIQUE  DE FORMATION (227)", "PATRON D'EMBARCATION (259)", "PSC1 (228)", "SEMINAIRE DE FORMATION DES RESPONSABLES DE GROUPE (14)", "SEMINAIRE DELEGUES TERRITORIAUX (70)", "SEMINAIRE DES POLES DEVELOPPEMENT (262)", "SENAMCO 2 (235)", "STAF (42)", "STAGE 2 QUALIFICATION VOILE (55)", "STIF (253)", "TECH (52)", "VALIDATION MONITORAT VOILE 1ER DEGRE (142)"}));
					panel_1.add(cbxFormation);
				}
			}
			{
				chkAdherents = new JCheckBox("Adhérents uniquement");
				chkAdherents.setSelected(true);
				GridBagConstraints gbc_chkAdherents = new GridBagConstraints();
				gbc_chkAdherents.anchor = GridBagConstraints.WEST;
				gbc_chkAdherents.insets = new Insets(0, 0, 0, 5);
				gbc_chkAdherents.gridx = 1;
				gbc_chkAdherents.gridy = 2;
				panel.add(chkAdherents, gbc_chkAdherents);
			}
			{
				chkRecursif = new JCheckBox("Récursif");
				chkRecursif.setSelected(true);
				GridBagConstraints gbc_chkRecursif = new GridBagConstraints();
				gbc_chkRecursif.anchor = GridBagConstraints.WEST;
				gbc_chkRecursif.gridx = 2;
				gbc_chkRecursif.gridy = 2;
				panel.add(chkRecursif, gbc_chkRecursif);
			}
		}
		{
			JPanel panel_2_1 = new JPanel();
			GridBagConstraints gbc_panel_2_1 = new GridBagConstraints();
			gbc_panel_2_1.anchor = GridBagConstraints.NORTHWEST;
			gbc_panel_2_1.insets = new Insets(0, 0, 5, 0);
			gbc_panel_2_1.gridx = 0;
			gbc_panel_2_1.gridy = 3;
			contentPanel.add(panel_2_1, gbc_panel_2_1);
			panel_2_1.setLayout(new BorderLayout(0, 0));
			{
				panel_3 = new JPanel();
				panel_3.setBorder(new TitledBorder(null, "Extraction des champs", TitledBorder.LEADING, TitledBorder.TOP, null, null));
				panel_2_1.add(panel_3, BorderLayout.CENTER);
				{
					chkFormatIndividu = new JCheckBox("individu");
					panel_3.add(chkFormatIndividu);
				}
				{
					chkFormatParents = new JCheckBox("parents");
					panel_3.add(chkFormatParents);
				}
				{
					chkFormatInscription = new JCheckBox("inscription");
					panel_3.add(chkFormatInscription);
				}
				{
					chkFormatAdhesion = new JCheckBox("adhesion");
					panel_3.add(chkFormatAdhesion);
				}
				{
					chkFormatJS = new JCheckBox("JS");
					panel_3.add(chkFormatJS);
				}
				{
					chkFormatSansQF = new JCheckBox("sans QF");
					panel_3.add(chkFormatSansQF);
				}
			}
			{
				panel_4 = new JPanel();
				panel_4.setBorder(new TitledBorder(null, "G\u00E9n\u00E9rateur", TitledBorder.LEADING, TitledBorder.TOP, null, null));
				panel_2_1.add(panel_4, BorderLayout.EAST);
				{
					cbxGenerateur = new JComboBox();
					panel_4.add(cbxGenerateur);
					cbxGenerateur.setModel(new DefaultComboBoxModel(new String[] {"xls", "cvs", "xml"}));
				}
			}
		}
		{
			JPanel panel = new JPanel();
			panel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Sortie", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
			GridBagConstraints gbc_panel = new GridBagConstraints();
			gbc_panel.anchor = GridBagConstraints.NORTH;
			gbc_panel.fill = GridBagConstraints.HORIZONTAL;
			gbc_panel.insets = new Insets(0, 0, 5, 0);
			gbc_panel.gridx = 0;
			gbc_panel.gridy = 4;
			contentPanel.add(panel, gbc_panel);
			panel.setLayout(new BorderLayout(0, 0));
			{
				{
					{
						{
							lblSortie = new JLabel("<rien>");
							panel.add(lblSortie, BorderLayout.CENTER);
						}
						JButton button = new JButton("Fichier...");
						panel.add(button, BorderLayout.EAST);
						button.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent arg0) {
								fcSortie = new JFileChooser();
								fcSortie.setDialogTitle("Export Configuration");
								fcSortie.setApproveButtonText("Export");
								fcSortie.setCurrentDirectory(new File("."));
								fcSortie.setFileSelectionMode(JFileChooser.FILES_ONLY);
								fcSortie.removeChoosableFileFilter(fcSortie.getFileFilter() );
								fcSortie.addChoosableFileFilter(new ExportFileFilter(cbxGenerateur.getSelectedItem().toString()));
								int result = fcSortie.showDialog(panel, "OK");
								if (result == JFileChooser.APPROVE_OPTION) {  
									File targetFile = fcSortie.getSelectedFile();
									lblSortie.setText(targetFile.getPath());
								}
							}
						});
					}
				}
			}
		}
		{
			JPanel panel = new JPanel();
			panel.setBorder(new TitledBorder(null, "Logs", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			GridBagConstraints gbc_panel = new GridBagConstraints();
			gbc_panel.fill = GridBagConstraints.BOTH;
			gbc_panel.gridx = 0;
			gbc_panel.gridy = 5;
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
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				btnGo = new JButton("Go");
				buttonPane.add(btnGo);
				btnGo.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						go();
					}
				});
			}
		}
	}
	
	@Override
	public boolean check()
	{
		logger_.info("Vérification des paramètres");
		if (fcSortie==null)
		{
			logger_.error("Sortie non-sélectionnée");
			return false;
		}
		if (txfIdentifiant.getText().isEmpty())
		{
			logger_.error("Identifiant est vide");
			return false;
		}
		if (txfMotdepasse.getPassword().length == 0)
		{
			logger_.error("Mode de passe est vide");
			return false;
		}
		if (txfStructure.getText().isEmpty())
		{
			logger_.error("Structure est vide");
			return false;
		}
		return true;
	}

	private ExtractionMain connection_;
	private JCheckBox chkRecursif;
	private JButton btnGo;
	private JCheckBox checkBox;
	private JPanel panel_3;
	private JPanel panel_4;
	
	private void login(ExtractionMain connection) throws ClientProtocolException, IOException
	{
		connection_ = connection;
		logger_.info("Connexion");
		
		connection_.init();
		if (connection_.login(txfIdentifiant.getText(),new String(txfMotdepasse.getPassword())) == false)
		{
			throw new IOException("erreur de connexion");
		}
	}
	
	public void logout() throws IOException
	{
		connection_.close();
	}
	
	@Override
	public void go()
	{
		ProgressMonitor p = new ProgressMonitor(this, "Extraction", "", 0, 100);
		p.setMillisToPopup(0);
		p.setMillisToDecideToPopup(0);
		
          new Thread(() -> {
			p.setProgress(0);
			txtLog.setText("");
			btnGo.setEnabled(false);
			
			Instant now = Instant.now();
			
			boolean ret = check();
			p.setProgress(20);
			if (ret)
			{
				String generateur = (String)cbxGenerateur.getSelectedItem();
				int type = GuiCommand.extract(((String)cbxType.getSelectedItem()));
				int specialite = GuiCommand.extract((String)cbxSpecialite.getSelectedItem());
				int categorie = GuiCommand.extract((String)cbxCategorie.getSelectedItem());
				int diplome = GuiCommand.extract((String)cbxDiplome.getSelectedItem());
				int qualif = GuiCommand.extract((String)cbxQualification.getSelectedItem());
				int formation = GuiCommand.extract((String)cbxFormation.getSelectedItem());
				int structure = Integer.parseInt(txfStructure.getText());
				int format = 0;
				if (chkFormatIndividu.isSelected())
					format+=1;
				if (chkFormatParents.isSelected())
					format+=2;
				if (chkFormatInscription.isSelected())
					format+=4;
				if (chkFormatAdhesion.isSelected())
					format+=8;
				if (chkFormatJS.isSelected())
					format+=16;
				if (chkFormatSansQF.isSelected())
					format+=32;
				
				logger_.info("Lancement");
			    
				try {
					if (generateur.compareTo(ExtractionMain.GENERATEUR_XLS) == 0)
					{
						Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fcSortie.getSelectedFile()), ENCODING_WINDOWS));
	
						ExtractionAdherents app = new ExtractionAdherents();
						login(app);
						p.setProgress(40);
					    logger_.info("Extraction");
						String donnees = app.extract(structure,chkRecursif.isSelected(),type, chkAdherents.isSelected(), txfCodefonction.getText(),specialite,categorie,diplome,qualif,formation,format, true);
						p.setProgress(60);
						logout();
						
						logger_.info("Ecriture du fichier \""+fcSortie.getSelectedFile().getAbsolutePath()+"\"");
						p.setProgress(80);
						out.write(donnees);
						out.flush();
						out.close();
						p.setProgress(100);
					}
					else
					if (generateur.compareTo(ExtractionMain.GENERATEUR_XML) == 0)
					{
						Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fcSortie.getSelectedFile()), ENCODING_UTF8));
	
						ExtractionAdherents app = new ExtractionAdherents();
						login(app);
						p.setProgress(40);
					    logger_.info("Extraction");
						String donnees = app.extract(structure,chkRecursif.isSelected(),type, chkAdherents.isSelected(), txfCodefonction.getText(),specialite,categorie,diplome,qualif,formation,format, false);
						p.setProgress(60);
						logout();
	
						logger_.info("Ecriture du fichier \""+fcSortie.getSelectedFile().getAbsolutePath()+"\"");
						p.setProgress(80);
						out.write(donnees);
						out.flush();
						out.close();
						p.setProgress(100);
					}
					else
					if (generateur.compareTo(ExtractionMain.GENERATEUR_CSV) == 0)
					{
						final CSVPrinter out = CSVFormat.DEFAULT.withFirstRecordAsHeader().print(fcSortie.getSelectedFile(), Charset.forName(ENCODING_WINDOWS));
						
						ExtractionAdherents app = new ExtractionAdherents();
						login(app);
						p.setProgress(40);
					    logger_.info("Extraction");
						String donnees = app.extract(structure,chkRecursif.isSelected(),type, chkAdherents.isSelected(), txfCodefonction.getText(),specialite,categorie,diplome,qualif,formation,format, false);
						p.setProgress(60);
						logout();
						
						XPathFactory xpfac = XPathFactory.instance();
						SAXBuilder builder = new SAXBuilder();
				        org.jdom2.Document docx = builder.build(new ByteArrayInputStream(donnees.getBytes(Charset.forName(ENCODING_UTF8))));
				        
				        // Scan des colonnes
				     	XPathExpression<?> xpac = xpfac.compile("tbody/tr[1]/td/text()");
				     	List<?> resultsc = xpac.evaluate(docx);
				     	int nbColumns = resultsc.size();	 
				     			 
				        XPathExpression<?> xpa = xpfac.compile("tbody/tr/td");
				        
				        List<?> results = xpa.evaluate(docx);
	
						logger_.info("Ecriture du fichier \""+fcSortie.getSelectedFile().getAbsolutePath()+"\"");
						p.setProgress(80);
				        int index = 0;
						Iterator<?> iter = results.iterator();
						while (iter.hasNext())
						{
							Object result = iter.next();
							Element resultElement = (Element) result;
							out.print(resultElement.getText());
			                index++;
				        	if (index % nbColumns == 0)
				        	{
				        		out.println();
				        	}
						}
						out.flush();
						out.close();
					}
				} catch (IOException|JDOMException e) {
					logger_.error(Logging.dumpStack(null,e));
				}
			}
			p.setProgress(100);
			p.close();
			
			long d = Instant.now().getEpochSecond() - now.getEpochSecond();
			logger_.info("Terminé en "+d+" seconds");
			
			btnGo.setEnabled(true);
		}).start();
	}
	
	@Override
	public void dispose()
	{
		Appender.setLoggedDialog(null);
		super.dispose();
	}
	
	@Override
	public void addLog(String message)
	{
		String texte = txtLog.getText();
		if (texte.length() > 0) texte+="\n";
		texte+=message;
		txtLog.setText(texte);
	}
	
	public JLabel getLblSortie() {
		return lblSortie;
	}
	public JTextArea getTxtLog() {
		return txtLog;
	}
	public JTextField getTxfIdentifiant() {
		return txfIdentifiant;
	}
	public JPasswordField getTxfMotdepasse() {
		return txfMotdepasse;
	}
	public JTextField getTxfStructure() {
		return txfStructure;
	}
	public JTextField getTxfCodefonction() {
		return txfCodefonction;
	}
	public JCheckBox getChkFormatIndividu() {
		return chkFormatIndividu;
	}
	public JCheckBox getChkFormatParents() {
		return chkFormatParents;
	}
	public JCheckBox getChkFormatInscription() {
		return chkFormatInscription;
	}
	public JCheckBox getChkFormatAdhesion() {
		return chkFormatAdhesion;
	}
	public JCheckBox getChkFormatJS() {
		return chkFormatJS;
	}
	public JCheckBox getChkFormatSansQF() {
		return chkFormatSansQF;
	}
	public JComboBox getCbxTypeinscription() {
		return cbxTypeinscription;
	}
	public JComboBox getCbxType() {
		return cbxType;
	}
	public JComboBox getCbxCategorie() {
		return cbxCategorie;
	}
	public JComboBox getCbxSpecialite() {
		return cbxSpecialite;
	}
	public JCheckBox getChkAdherents() {
		return chkAdherents;
	}
	public JComboBox getCbxFormation() {
		return cbxFormation;
	}
	public JComboBox getCbxQualification() {
		return cbxQualification;
	}
	public JComboBox getCbxDiplome() {
		return cbxDiplome;
	}
	public JCheckBox getChkRecursif() {
		return chkRecursif;
	}
	public JButton getBtnGo() {
		return btnGo;
	}
}