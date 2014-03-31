package net.mormolhs.facebook.fbanalytics.ui;

import com.toedter.calendar.JDateChooser;
import net.mormolhs.facebook.fbanalytics.data.pages.PageData;
import net.mormolhs.facebook.fbanalytics.data.pages.PageTable;
import net.mormolhs.facebook.fbanalytics.integration.facebookdatacollectors.FacebookDataCollector;
import net.mormolhs.facebook.fbanalytics.resources.GlobalParameters;
import net.mormolhs.facebook.fbanalytics.ui.dataview.FbStatsDateUtils;
import net.mormolhs.facebook.fbanalytics.utils.ColumnSorter;
import net.mormolhs.facebook.fbanalytics.utils.CommonGuiTools;
import org.jbundle.thin.base.screen.jcalendarbutton.JCalendarButton;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.Vector;

/**
 * Created by toikonomakos on 3/16/14.
 */
public class LandingPage extends JFrame {

    PageTable pages;
    String pageId;
    DefaultTableModel dtm;
    ImageIcon coverPicture;
    ImageIcon profilePicture;
    JPanel panel2;
    JLabel coverPictureLabel;
    JLabel labelProfilePicture;
    JLabel pageInfoLabel;    JPanel pageInfoPanel;
    JTable table4;
    JScrollPane panel4;
    JPanel panel234;
    JPanel panel3;
    JCheckBox linksEnabled;
    JCheckBox textsEnabled;
    JCheckBox photosEnabled;
    JButton executeButton = new JButton("Execute");
    MyTextArea textarea = new MyTextArea();
    FacebookDataCollector fbCollector;
    JTable table1 = new JTable() {
        @Override
        public boolean isCellEditable(int arg0, int arg1) {
            return false;
        }

        @Override
        public Class<?> getColumnClass(int col) {
            if (col == 1) {
                return Integer.class;
            } else {
                return super.getColumnClass(col);
            }
        }
    };

    private JPanel getLoginPanel() {
        JPanel loginGui = new JPanel();
        return loginGui;
    }

    private void getMainPage() {
//        Collect all pages from facebook API
        fbCollector = new FacebookDataCollector();
        pages = fbCollector.getAllFacebookPagesForAccount();
        table1.setModel(populatePageDataOnGui(pages));
        table1.setColumnSelectionAllowed(true);
        table1.setAutoCreateRowSorter(true);
        table1.getFont().deriveFont(Font.BOLD);
        table1.getColumnModel().getColumn(0).setPreferredWidth(150);
        table1.getColumnModel().getColumn(0).setMaxWidth(150);
        table1.getColumnModel().getColumn(1).setPreferredWidth(60);
        table1.getColumnModel().getColumn(1).setMaxWidth(60);
        table1.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() != 0) {
                    performActionForPageListListener();
                }
            }
        });

        executeButton.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() != 0) {
                    performActionForExecuteButtonListener();
                }
            }
        });
        table1.repaint();
        JScrollPane panel1 = new JScrollPane(table1);
        panel1.setPreferredSize(new Dimension(210, 500));
        this.add(panel1, BorderLayout.WEST);

//        get the first page loaded
        //String pageId = pages.getPageDetails().entrySet().iterator().next().getKey();
        if (pageId == null) {
            pageId = pages.getPageDetails().entrySet().iterator().next().getKey();
        }

        panel234 = new JPanel();
        panel234.setLayout(new GridLayout(1,3,0,5));
        panel234.setLayout(new BoxLayout(panel234, BoxLayout.Y_AXIS));
        this.add(panel234, BorderLayout.EAST);
        panel2 = new JPanel();
        panel2.setPreferredSize(new Dimension(950, 150));
        panel2.setMaximumSize(new Dimension(1050, 150));
//        add top panel for cover picture
        if (coverPictureLabel == null) {
            try {
                URL img = new URL("http://www.acsu.buffalo.edu/~rslaine/imageNotFound.jpg");
                coverPicture = new ImageIcon(img);
                coverPicture = new ImageIcon(CommonGuiTools.getScaledImage(coverPicture.getImage(), 1150, 150));
                coverPictureLabel = new JLabel("",coverPicture,JLabel.CENTER);
                coverPictureLabel.setMaximumSize(new Dimension(150,150));
                panel2.add(coverPictureLabel);
                panel2.repaint();
            } catch (MalformedURLException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }

        panel234.add(panel2);

//        add center panel for date range/result limit  and refresh button label
        panel3 = new JPanel();
        panel3.setPreferredSize(new Dimension(1150, 150));

//        add top panel for profile picture and likes label

        labelProfilePicture = new JLabel();
        labelProfilePicture.setAlignmentX(SwingConstants.LEFT);
        pageInfoPanel = new JPanel();
        pageInfoLabel = new JLabel();

        JPanel panel3a = new JPanel();
        SpringLayout panel3aLayout = new SpringLayout();

        panel3aLayout.putConstraint(SpringLayout.WEST, pageInfoLabel,170,SpringLayout.WEST, labelProfilePicture);
        panel3a.setLayout(panel3aLayout);
        panel3a.add(labelProfilePicture);
        panel3a.add(pageInfoLabel);
        panel3a.repaint();

        photosEnabled = new JCheckBox("Photos");
        linksEnabled = new JCheckBox("Links");
        textsEnabled = new JCheckBox("Texts");
        photosEnabled.setSelected(true);
        linksEnabled.setSelected(true);
        textsEnabled.setSelected(true);

        JCalendarButton calendarFrom = new JCalendarButton();
        JDateChooser chooserFrom = new JDateChooser();
        chooserFrom.addPropertyChangeListener(
                new PropertyChangeListener() {
                    @Override
                    public void propertyChange(PropertyChangeEvent e) {
                        if ("date".equals(e.getPropertyName())) {
                            Date date = (Date) e.getNewValue();
                            Long epoch = date.getTime();
                            String str = epoch.toString();
                            GlobalParameters.DATE_FROM = str.replace(str.substring(str.length() - 3), "");
                        }
                    }
                });
        calendarFrom.add(chooserFrom);
        calendarFrom.repaint();

        JCalendarButton calendarTo = new JCalendarButton();
        JDateChooser chooserTo = new JDateChooser();
        chooserTo.addPropertyChangeListener(
                new PropertyChangeListener() {
                    @Override
                    public void propertyChange(PropertyChangeEvent e) {
                        if ("date".equals(e.getPropertyName())) {
                            Date date = (Date) e.getNewValue();
                            Long epoch = date.getTime();
                            String str = epoch.toString();
                            GlobalParameters.DATE_TO = str.replace(str.substring(str.length() - 3), "");
                        }
                    }
                });
        calendarTo.add(chooserTo);
        calendarTo.repaint();

        textarea.addKeyListener(textarea);
        textarea.setTextareainfo("50");

        JPanel panel3b = new JPanel();
        panel3b.setLayout(new GridLayout(4, 1, 0, 0));

        JPanel panel3br1 = new JPanel();
        panel3br1.setLayout(new GridLayout(1, 2, 0, 0));
        JLabel fromCalLabel = new JLabel("<html><b>From: </b></html>");
        fromCalLabel.setFont(new Font("Serif", Font.BOLD, 14));
        fromCalLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        panel3br1.add(fromCalLabel,BorderLayout.EAST);
        panel3br1.add(calendarFrom);
        panel3b.add(panel3br1);

        JPanel panel3br2 = new JPanel();
        panel3br2.setLayout(new GridLayout(1, 2, 0, 0));
        JLabel toCalLabel = new JLabel("<html><b>To: </b></html>");
        toCalLabel.setFont(new Font("Serif", Font.BOLD, 14));
        toCalLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        panel3br2.add(toCalLabel,BorderLayout.EAST);
        panel3br2.add(calendarTo);
        panel3b.add(panel3br2);

        JPanel panel3br3 = new JPanel();
//        panel3br3.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        panel3br3.setLayout(new GridLayout(1, 2, 0, 0));
        executeButton.repaint();
        JLabel textAreaLabel = new JLabel("Results: ");
        textAreaLabel.setFont(new Font("Serif", Font.BOLD, 14));
        textAreaLabel.setEnabled(true);
        textAreaLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        panel3br3.add(textAreaLabel, BorderLayout.EAST);
        JPanel panel3br3a = new JPanel();
        panel3br3a.setLayout(new GridLayout(1, 2, 0, 0));
        panel3br3a.add(textarea, BorderLayout.WEST);
        panel3br3a.add(executeButton, BorderLayout.EAST);
        panel3br3.add(panel3br3a);
        panel3b.add(panel3br3);

        JPanel panel3br4 = new JPanel();
        panel3br4.setLayout(new GridLayout(1, 2, 0, 0));
        panel3br4.add(new JLabel(""));
        JPanel checkBoxesPanel = new JPanel();
        checkBoxesPanel.add(linksEnabled);
        checkBoxesPanel.add(textsEnabled);
        checkBoxesPanel.add(photosEnabled);
        panel3br4.add(checkBoxesPanel);
        panel3b.add(panel3br4);

        panel3a.repaint();
        panel3b.repaint();
        panel3.setLayout(new GridLayout(1, 2, 0, 0));
        panel3.add(panel3a);
        panel3.add(panel3b);
        panel3.repaint();
        panel234.add(panel3);

//        add bottom right panel for page's post details label

        table4 = new JTable();
        table4.setAutoCreateRowSorter(true);
        table4.setRowHeight(70);
        dtm = populatePostDataOnGui(pages, pageId, false);
        table4.setModel(dtm);
        setupTable4Size();
        table4.repaint();
        panel4 = new JScrollPane(table4);
        panel4.setPreferredSize(new Dimension(1150, 440));
        panel4.repaint();
        panel234.add(panel4);
        panel234.setPreferredSize(new Dimension(1150, 690));
        panel234.repaint();
        this.pack();
        this.repaint();
    }


    public void run() {
        /*
        * Login frame
        */
        JPanel loginGui = this.getLoginPanel();
        JLabel loginMessage = new JLabel("Please give your token: ");
        loginGui.add(loginMessage, BorderLayout.NORTH);
        JTextField field = new JTextField(20);
        setTitle("Facebook Insights Sniffer");
        loginGui.add(field, BorderLayout.SOUTH);
        pack();
        repaint();
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        JOptionPane.showMessageDialog(null, loginGui);
        GlobalParameters.ACCESS_TOKEN = field.getText();
        this.getMainPage();
        /*
        * Main page frame
        */
    }

    private String getPageIdFromNameOnPagesJTable(PageTable pages, String pageName) {
        for (Map.Entry<String, PageData> page : pages.getPageDetails().entrySet()) {
            if (page.getValue().getPageName().equals(pageName)) {
                return page.getKey();
            }
        }
        return null;
    }

    public DefaultTableModel populatePageDataOnGui(PageTable pages) {
        DefaultTableModel table = new DefaultTableModel(null, new Object[]{"<html><b>Page Name</b></html>", "<html><b>Likes</b></html>"}) {
            @Override
            public Class<?> getColumnClass(int col) {
                if (col == 1) {
                    return Integer.class;
                } else {
                    return super.getColumnClass(col);
                }
            }
        };

        for (Map.Entry<String, PageData> page : pages.getPageDetails().entrySet()) {
            table.addRow(new Object[]{page.getValue().getPageName(), Integer.valueOf(page.getValue().getLikes())});
        }
        Vector data = table.getDataVector();
        Collections.sort(data, new ColumnSorter(1, false));
        table.fireTableStructureChanged();
        return table;
    }

    public DefaultTableModel populatePostDataOnGui(PageTable pages, String pageId, boolean postDataVisible) {
        DefaultTableModel table = new DefaultTableModel(null, new Object[]{"<html><b>#</b></html>", "<html><b>Post Thumbnail</b></html>", "<html><b>Type</b></html>", "<html><b>Date & Time (GMT)</b></html>", "<html><b>Post Text</b></html>", "<html><b>Post Link</b></html>", "<html><b>Likes</b></html>", "<html><b>Comments</b></html>", "<html><b>Shares</b></html>", "<html><b>Total</b></html>", "<html><b>Reach</b></html>", "<html><b>Clicks</b></html>", "<html><b>CTR(%)</b></html>"}) {
            @Override
            public Class<?> getColumnClass(int col) {
                switch (col) {
                    case 0:
                        return Integer.class;
                    case 1:
                        return ImageIcon.class;
                    case 2:
                        return ImageIcon.class;
                    case 6:
                        return Integer.class;
                    case 7:
                        return Integer.class;
                    case 8:
                        return Integer.class;
                    case 9:
                        return Integer.class;
                    case 10:
                        return Integer.class;
                    case 11:
                        return Integer.class;
                    default:
                        return super.getColumnClass(col);
                }
            }
        };
        if (postDataVisible) {
            int count = 1;
            for (int i = 0; i < pages.getPageDetails().get(pageId).getPostData().getTable().size(); i++) {
                if (GlobalParameters.PHOTOS_ENABLED && pages.getPageDetails().get(pageId).getPostData().getTable().get(i).getType().equals("247")) {
                    table.addRow((Object[]) this.getTableRowBasedOnPosition(pages, pageId, i, count));
                    count++;
                }
                if (GlobalParameters.LINKS_ENABLED && pages.getPageDetails().get(pageId).getPostData().getTable().get(i).getType().equals("80")) {
                    table.addRow((Object[]) this.getTableRowBasedOnPosition(pages, pageId, i, count));
                    count++;
                }
                if (GlobalParameters.TEXTS_ENABLED && pages.getPageDetails().get(pageId).getPostData().getTable().get(i).getType().equals("46")) {
                    table.addRow((Object[]) this.getTableRowBasedOnPosition(pages, pageId, i, count));
                    count++;
                }
            }
        }
        Vector data = table.getDataVector();
        Collections.sort(data, new ColumnSorter(3, false));
        table.fireTableStructureChanged();
        return table;
    }

    private Object getTableRowBasedOnPosition(PageTable pages, String pageId, int i, int count) {
        return new Object[]{
                count,
                this.getLabelFromImageUrl(pages.getPageDetails().get(pageId).getPostData().getTable().get(i).getThumbnail()),
                getImageIconForPostType(pages.getPageDetails().get(pageId).getPostData().getTable().get(i).getType()),
                FbStatsDateUtils.convertTime(pages.getPageDetails().get(pageId).getPostData().getTable().get(i).getDatePosted()),
                pages.getPageDetails().get(pageId).getPostData().getTable().get(i).getPostText(),
                pages.getPageDetails().get(pageId).getPostData().getTable().get(i).getPostLink(),
                Integer.valueOf(pages.getPageDetails().get(pageId).getPostData().getTable().get(i).getLikes()),
                Integer.valueOf(pages.getPageDetails().get(pageId).getPostData().getTable().get(i).getComments()),
                Integer.valueOf(pages.getPageDetails().get(pageId).getPostData().getTable().get(i).getShares()),
                Integer.valueOf(pages.getPageDetails().get(pageId).getPostData().getTable().get(i).getLikes()) + Integer.valueOf(pages.getPageDetails().get(pageId).getPostData().getTable().get(i).getComments()) + Integer.valueOf(pages.getPageDetails().get(pageId).getPostData().getTable().get(i).getShares()),
                Integer.valueOf(pages.getPageDetails().get(pageId).getPostData().getTable().get(i).getReached()),
                Integer.valueOf(pages.getPageDetails().get(pageId).getPostData().getTable().get(i).getClicks()),
                pages.getPageDetails().get(pageId).getPostData().getTable().get(i).getReached().equals("0") ? "0" :
                        String.valueOf((double)(Math.round(((Double.valueOf(pages.getPageDetails().get(pageId).getPostData().getTable().get(i).getClicks()))) / Double.valueOf(pages.getPageDetails().get(pageId).getPostData().getTable().get(i).getReached())*100)))};
    }

    private ImageIcon getLabelFromImageUrl(String imageUrl) {
        ImageIcon picture = new ImageIcon();
        try {
            URL img;
            if (imageUrl.equals("N/A")) {
                img = new URL("http://www.acsu.buffalo.edu/~rslaine/imageNotFound.jpg");
            } else {
                img = new URL(imageUrl);
            }
            Image bImg = ImageIO.read(img).getScaledInstance(60, 50, Image.SCALE_SMOOTH);
            picture = new ImageIcon(bImg);
        } catch (IOException e1) {
            e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return picture;
    }

    private ImageIcon getImageIconForPostType(String postType) {
        ImageIcon picture = new ImageIcon();
        try {
            URL img;
            if (postType.equals("80")) {
                img = new URL("http://i.imgur.com/dYWWWmO.png");
            } else if (postType.equals("247")) {
                img = new URL("http://i.imgur.com/jqUXI88.png");
            } else {
                img = new URL("http://i.imgur.com/29gvJEx.png");
            }
            Image bImg = ImageIO.read(img).getScaledInstance(60, 50, Image.SCALE_SMOOTH);
            picture = new ImageIcon(bImg);
        } catch (IOException e1) {
            e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return picture;
    }

    private void performActionForPageListListener() {
        try {
            URL coverImg;
            URL profileImg;
            String name = table1.getValueAt(table1.getSelectedRow(), 0).toString().replaceAll("\\<.*?>", "");
            pageId = getPageIdFromNameOnPagesJTable(pages, name);
            if (pages.getPageDetails().get(pageId).getPageCoverPicture().equals("N/A")) {
                coverImg = new URL("http://www.acsu.buffalo.edu/~rslaine/imageNotFound.jpg");
            } else {
                coverImg = new URL((pages.getPageDetails().get(pageId).getPageCoverPicture()));
            }
            if (pages.getPageDetails().get(pageId).getPageProfilePicture().equals("N/A")) {
                profileImg = new URL("http://www.acsu.buffalo.edu/~rslaine/imageNotFound.jpg");
            } else {
                profileImg = new URL((pages.getPageDetails().get(pageId).getPageProfilePicture()));
            }
            coverPicture = new ImageIcon(coverImg);
            coverPicture = new ImageIcon(CommonGuiTools.getScaledImage(coverPicture.getImage(), 1150, 150));
            profilePicture = new ImageIcon(profileImg);
        } catch (MalformedURLException e1) {
            e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        String PageInfoHtmlLabel = "<html>\n" +
                "\t<body>\n" +
                "\t\t<p>\n" +
                "\t\t<br>\n" +
                "\t\t\t<span style=\"font-family:verdana,geneva,sans-serif;\"><span style=\"font-size:15px;\"><strong>" + pages.getPageDetails().get(pageId).getPageName() + "</strong></span></span></p><br>\n" +
                "\t\t<p>\n" +
                "\t\t\t<span style=\"color:#696969;\"><span style=\"font-size: 12px;\"><span style=\"font-family: verdana,geneva,sans-serif;\">" + pages.getPageDetails().get(pageId).getLikes() + " like this</span></span></span></p>\n" +
                "\t\t<p>\n" +
                "\t\t\t<span style=\"color:#696969;\"><span style=\"font-size: 12px;\"><span style=\"font-family: verdana,geneva,sans-serif;\">" + pages.getPageDetails().get(pageId).getTalkingAbout() + " talking about this</span></span></span></p>\n" +
                "\t</body>\n" +
                "</html>\n";

        pageInfoLabel.setText(PageInfoHtmlLabel);
        labelProfilePicture.setIcon(profilePicture);
        labelProfilePicture.setPreferredSize(new Dimension(profilePicture.getIconWidth(), profilePicture.getIconHeight()));
        labelProfilePicture.repaint();
        coverPictureLabel.setIcon(coverPicture);
        coverPictureLabel.setMaximumSize(new Dimension(1150,150));
        coverPictureLabel.repaint();
        panel2.add(coverPictureLabel);
        panel2.repaint();
        panel3.repaint();
        if (dtm != null && dtm.getRowCount() > 0) {
            dtm = populatePostDataOnGui(pages, pageId, false);
            table4.setModel(dtm);
            setupTable4Size();
            table4.repaint();
        }

    }

    private void performActionForExecuteButtonListener() {
        GlobalParameters.LINKS_ENABLED = linksEnabled.isSelected();
        GlobalParameters.TEXTS_ENABLED = textsEnabled.isSelected();
        GlobalParameters.PHOTOS_ENABLED = photosEnabled.isSelected();
        if (!textarea.getText().equals("") || textarea.getText().matches("[0-9]+")) {
            GlobalParameters.RESULT_SIZE = textarea.getText();
        } else {
            GlobalParameters.RESULT_SIZE = "50";
        }
        String name = table1.getValueAt(table1.getSelectedRow(), 0).toString().replaceAll("\\<.*?>", "");
        if (!pageId.equals(getPageIdFromNameOnPagesJTable(pages, name))) {
            performActionForPageListListener();
        }
        pageId = getPageIdFromNameOnPagesJTable(pages, name);
        pages = fbCollector.getAllPostsForFacebookPage(pages, pageId);
        dtm = populatePostDataOnGui(pages, pageId, true);
        table4.setModel(dtm);
        setupTable4Size();
        table4.repaint();
    }

    private void setupTable4Size() {
//        header.setFont(new Font("Serif", Font.BOLD, 16));
        table4.getColumnModel().getColumn(0).setPreferredWidth(25);
        table4.getColumnModel().getColumn(0).setMaxWidth(35);
//        table4.getColumnModel().getColumn(1).setHeaderRenderer(header);
        table4.getColumnModel().getColumn(1).setPreferredWidth(110);
        table4.getColumnModel().getColumn(1).setMaxWidth(120);
//        table4.getColumnModel().getColumn(1).setHeaderRenderer(header);
        table4.getColumnModel().getColumn(2).setPreferredWidth(50);
        table4.getColumnModel().getColumn(2).setMaxWidth(60);
//        table4.getColumnModel().getColumn(2).setHeaderRenderer(header);
        table4.getColumnModel().getColumn(3).setPreferredWidth(170);
        table4.getColumnModel().getColumn(3).setMaxWidth(180);
//        table4.getColumnModel().getColumn(3).setHeaderRenderer(header);
        table4.getColumnModel().getColumn(4).setPreferredWidth(150);
        table4.getColumnModel().getColumn(4).setMaxWidth(190);
//        table4.getColumnModel().getColumn(4).setHeaderRenderer(header);
        table4.getColumnModel().getColumn(5).setPreferredWidth(100);
        table4.getColumnModel().getColumn(5).setMaxWidth(130);
//        table4.getColumnModel().getColumn(5).setHeaderRenderer(header);
        table4.getColumnModel().getColumn(6).setPreferredWidth(50);
        table4.getColumnModel().getColumn(6).setMaxWidth(60);
//        table4.getColumnModel().getColumn(6).setHeaderRenderer(header);
        table4.getColumnModel().getColumn(7).setPreferredWidth(70);
        table4.getColumnModel().getColumn(7).setMaxWidth(80);
//        table4.getColumnModel().getColumn(7).setHeaderRenderer(header);
        table4.getColumnModel().getColumn(8).setPreferredWidth(50);
        table4.getColumnModel().getColumn(8).setMaxWidth(60);
//        table4.getColumnModel().getColumn(8).setHeaderRenderer(header);
        table4.getColumnModel().getColumn(9).setPreferredWidth(50);
        table4.getColumnModel().getColumn(9).setMaxWidth(60);
//        table4.getColumnModel().getColumn(9).setHeaderRenderer(header);
        table4.getColumnModel().getColumn(10).setPreferredWidth(50);
        table4.getColumnModel().getColumn(10).setMaxWidth(60);
//        table4.getColumnModel().getColumn(10).setHeaderRenderer(header);
        table4.getColumnModel().getColumn(11).setPreferredWidth(50);
        table4.getColumnModel().getColumn(11).setMaxWidth(60);
//        table4.getColumnModel().getColumn(11).setHeaderRenderer(header);
        table4.getColumnModel().getColumn(12).setPreferredWidth(50);
        table4.getColumnModel().getColumn(12).setMaxWidth(60);
//        table4.getColumnModel().getColumn(12).setHeaderRenderer(header);
        table4.setColumnSelectionAllowed(true);
        table4.repaint();
    }

}