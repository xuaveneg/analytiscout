package org.leplan73.analytiscout.gui.dialogues;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import org.leplan73.analytiscout.gui.Template;
import org.leplan73.analytiscout.gui.analyseur.AnalyseurJeunes;
import org.leplan73.analytiscout.gui.analyseur.AnalyseurResponsables;
import org.leplan73.analytiscout.gui.extracteur.ExtracteurBatchJeunes;
import org.leplan73.analytiscout.gui.extracteur.ExtracteurBatchResponsables;
import org.leplan73.analytiscout.gui.utils.ElementFactory;

@SuppressWarnings("serial")
public class Analyses extends Template {

	/**
	 * Create the panel.
	 */
	public Analyses() {
		super();

		JPanel panel_title1 = ElementFactory.createActionTitle("<html><b>Analyse des maitrises et compas</b></html>");
		GridBagLayout gbl_panel_title1 = (GridBagLayout) panel_title1.getLayout();
		gbl_panel_title1.rowWeights = new double[] { 0.0 };
		gbl_panel_title1.rowHeights = new int[] { 0 };
		gbl_panel_title1.columnWeights = new double[] { 0.0, 0.0 };
		gbl_panel_title1.columnWidths = new int[] { 0, 0 };

		JPanel panel_1 = new JPanel();
		panel_1.setLayout(new BorderLayout(0, 0));
		panel_1.setBorder(null);
		panel_collection.add(panel_1, BorderLayout.NORTH);
		panel_1.add(panel_title1, BorderLayout.NORTH);

		JPanel panel_6 = new JPanel();
		panel_1.add(panel_6, BorderLayout.SOUTH);
		panel_6.setLayout(new BorderLayout(0, 0));

		JPanel panel_7 = new JPanel();
		panel_7.setBorder(null);
		panel_6.add(panel_7);
		GridBagLayout gbl_panel_7 = new GridBagLayout();
		gbl_panel_7.columnWidths = new int[] { 232, 231, 0 };
		gbl_panel_7.rowHeights = new int[] { 0, 23, 0, 0 };
		gbl_panel_7.columnWeights = new double[] { 1.0, 1.0, Double.MIN_VALUE };
		gbl_panel_7.rowWeights = new double[] { 0.0, 0.0, 0.0, Double.MIN_VALUE };
		panel_7.setLayout(gbl_panel_7);

		JButton button_2 = new JButton(
				"<html><p style=\"text-align:center;\">Analyser des données<br>déjà extraites</p>");
		button_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new AnalyseurResponsables().setVisible(true);
			}
		});
		GridBagConstraints gbc_button_2 = new GridBagConstraints();
		gbc_button_2.fill = GridBagConstraints.HORIZONTAL;
		gbc_button_2.insets = new Insets(0, 0, 5, 5);
		gbc_button_2.gridx = 0;
		gbc_button_2.gridy = 1;
		panel_7.add(button_2, gbc_button_2);

		JButton btnexporterDesDonnesen = new JButton(
				"<html><p style=\"text-align:center;\">Exporter des données<br>en batch</p>");
		btnexporterDesDonnesen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new ExtracteurBatchResponsables().setVisible(true);
			}
		});
		GridBagConstraints gbc_btnexporterDesDonnesen = new GridBagConstraints();
		gbc_btnexporterDesDonnesen.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnexporterDesDonnesen.insets = new Insets(0, 0, 5, 0);
		gbc_btnexporterDesDonnesen.gridx = 1;
		gbc_btnexporterDesDonnesen.gridy = 1;
		panel_7.add(btnexporterDesDonnesen, gbc_btnexporterDesDonnesen);

		JPanel panel_title3 = ElementFactory.createActionTitle("<html><b>Analyse des jeunes</b></html>");
		GridBagLayout gbl_panel_title3 = (GridBagLayout) panel_title3.getLayout();
		gbl_panel_title3.rowWeights = new double[] { 0.0 };
		gbl_panel_title3.rowHeights = new int[] { 0 };
		gbl_panel_title3.columnWeights = new double[] { 0.0, 0.0 };
		gbl_panel_title3.columnWidths = new int[] { 0, 0 };

		JPanel panel_3 = new JPanel();
		panel_3.setLayout(new BorderLayout(0, 0));
		panel_3.setBorder(null);
		panel_collection.add(panel_3, BorderLayout.CENTER);
		panel_3.add(panel_title3, BorderLayout.NORTH);

		JPanel panel_8 = new JPanel();
		panel_3.add(panel_8, BorderLayout.CENTER);
		GridBagLayout gbl_panel_8 = new GridBagLayout();
		gbl_panel_8.columnWidths = new int[] { 89, 183, 0 };
		gbl_panel_8.rowHeights = new int[] { 145, 0 };
		gbl_panel_8.columnWeights = new double[] { 1.0, 1.0, Double.MIN_VALUE };
		gbl_panel_8.rowWeights = new double[] { 0.0, Double.MIN_VALUE };
		panel_8.setLayout(gbl_panel_8);

		JButton btnNewButton_1 = new JButton(
				"<html><p style=\"text-align:center;\">Analyser des données<br>déjà extraites</p>");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new AnalyseurJeunes().setVisible(true);
			}
		});
		GridBagConstraints gbc_btnNewButton_1 = new GridBagConstraints();
		gbc_btnNewButton_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnNewButton_1.anchor = GridBagConstraints.NORTH;
		gbc_btnNewButton_1.insets = new Insets(0, 0, 0, 5);
		gbc_btnNewButton_1.gridx = 0;
		gbc_btnNewButton_1.gridy = 0;
		panel_8.add(btnNewButton_1, gbc_btnNewButton_1);

		JButton btnNewButton = new JButton("Exporter des données en batch");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new ExtracteurBatchJeunes().setVisible(true);
			}
		});
		GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
		gbc_btnNewButton.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnNewButton.anchor = GridBagConstraints.NORTH;
		gbc_btnNewButton.gridx = 1;
		gbc_btnNewButton.gridy = 0;
		panel_8.add(btnNewButton, gbc_btnNewButton);

	}

}