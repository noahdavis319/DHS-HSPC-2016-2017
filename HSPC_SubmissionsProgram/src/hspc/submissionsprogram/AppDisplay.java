package hspc.submissionsprogram;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Noah Davis on 5/2/2016.
 * <p>
 * This work is licensed under a
 * Creative Commons Attribution 4.0
 * International License.
 * <p>
 * You can read more about the license by
 * visiting the link provided below.
 * http://creativecommons.org/licenses/by/4.0/legalcode
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS",
 * WITHOUT WARRANTY OF ANY KIND, EXPRESS
 * OR IMPLIED, INCLUDING BUT NOT LIMITED
 * TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL
 * THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF
 * CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS
 * IN THE SOFTWARE.
 */

class AppDisplay extends JFrame {
	private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	//private static final String DB_URL = "jdbc:mysql://localhost/hspc?useSSL=false";
	private final JList<String> cList;
	private final ArrayList<ClarificationData> clarificationDatas = new ArrayList<>();
	private int lastId = Integer.MIN_VALUE;
	private Instant old = Instant.now();

	AppDisplay() {
		this.setTitle("Dominion High School Programming Contest");
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.setResizable(false);

		WindowListener exitListener = new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		};
		this.addWindowListener(exitListener);

		JTabbedPane pane = new JTabbedPane();
		this.add(pane);

		JPanel submitPanel = new JPanel(null);
		submitPanel.setPreferredSize(new Dimension(500, 500));

		UIManager.put("FileChooser.readOnly", true);
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setBounds(0, 0, 500, 350);
		fileChooser.setVisible(true);
		FileNameExtensionFilter javaFilter = new FileNameExtensionFilter("Java files (*.java)", "java");
		fileChooser.setFileFilter(javaFilter);
		fileChooser.setAcceptAllFileFilterUsed(false);
		fileChooser.setControlButtonsAreShown(false);
		submitPanel.add(fileChooser);

		JSeparator separator1 = new JSeparator();
		separator1.setBounds(12, 350, 476, 2);
		separator1.setForeground(new Color(122, 138, 152));
		submitPanel.add(separator1);

		JLabel problemChooserLabel = new JLabel("Problem:");
		problemChooserLabel.setBounds(12, 360, 74, 25);
		submitPanel.add(problemChooserLabel);

		String[] listOfProblems = Main.Configuration.get("problem_names").split(Main.Configuration.get("name_delimiter"));
		JComboBox problems = new JComboBox<>(listOfProblems);
		problems.setBounds(96, 360, 393, 25);
		submitPanel.add(problems);

		JButton submit = new JButton("Submit");
		submit.setBounds(170, 458, 160, 30);
		submit.addActionListener(e -> {
			try {
				File file = fileChooser.getSelectedFile();
				try {
					CloseableHttpClient httpClient = HttpClients.createDefault();
					HttpPost uploadFile = new HttpPost("http://localhost:8080/submit");

					MultipartEntityBuilder builder = MultipartEntityBuilder.create();
					builder.addTextBody("accountID", Main.accountID, ContentType.TEXT_PLAIN);
					builder.addTextBody("problem", String.valueOf(problems.getSelectedItem()), ContentType.TEXT_PLAIN);
					builder.addBinaryBody("submission", file, ContentType.APPLICATION_OCTET_STREAM, file.getName());
					HttpEntity multipart = builder.build();

					uploadFile.setEntity(multipart);

					CloseableHttpResponse response = httpClient.execute(uploadFile);
					HttpEntity responseEntity = response.getEntity();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			} catch (NullPointerException ex) {
				JOptionPane.showMessageDialog(this, "No file selected.\nPlease select a java file.", "Error", JOptionPane.WARNING_MESSAGE);
			}
		});
		submitPanel.add(submit);

		JPanel clarificationsPanel = new JPanel(null);
		clarificationsPanel.setPreferredSize(new Dimension(500, 500));

		cList = new JList<>();
		cList.setBounds(12, 12, 476, 200);
		cList.setBorder(new CompoundBorder(
				BorderFactory.createLineBorder(new Color(122, 138, 152)),
				BorderFactory.createEmptyBorder(8, 8, 8, 8)
		));
		cList.setBackground(new Color(254, 254, 255));
		clarificationsPanel.add(cList);

		JButton viewC = new JButton("View");
		viewC.setBounds(12, 224, 232, 25);
		viewC.addActionListener(e -> {
			if (cList.getSelectedIndex() != -1) {
				int id = Integer.parseInt(cList.getSelectedValue().split("\\.")[0]);
				clarificationDatas.stream().filter(data -> data.getId() == id).forEach(data -> new ClarificationDisplay(data.getProblem(), data.getText(), data.getResponse()));
			}
		});
		clarificationsPanel.add(viewC);

		JButton refreshC = new JButton("Refresh");
		refreshC.setBounds(256, 224, 232, 25);
		refreshC.addActionListener(e -> updateCList(true));
		clarificationsPanel.add(refreshC);

		JSeparator separator2 = new JSeparator();
		separator2.setBounds(12, 261, 476, 2);
		separator2.setForeground(new Color(122, 138, 152));
		clarificationsPanel.add(separator2);

		JLabel problemChooserLabelC = new JLabel("Problem:");
		problemChooserLabelC.setBounds(12, 273, 74, 25);
		clarificationsPanel.add(problemChooserLabelC);

		JComboBox problemsC = new JComboBox<>(listOfProblems);
		problemsC.setBounds(96, 273, 393, 25);
		clarificationsPanel.add(problemsC);

		JTextArea textAreaC = new JTextArea();
		textAreaC.setLineWrap(true);
		textAreaC.setWrapStyleWord(true);
		textAreaC.setBorder(new CompoundBorder(
				BorderFactory.createLineBorder(new Color(122, 138, 152)),
				BorderFactory.createEmptyBorder(8, 8, 8, 8)
		));
		textAreaC.setBackground(new Color(254, 254, 255));

		JScrollPane areaScrollPane = new JScrollPane(textAreaC);
		areaScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		areaScrollPane.setBounds(12, 312, 477, 134);
		clarificationsPanel.add(areaScrollPane);

		JButton submitC = new JButton("Submit Clarification");
		submitC.setBounds(170, 458, 160, 30);
		submitC.addActionListener(e -> {
			if (textAreaC.getText().length() > 2048) {
				JOptionPane.showMessageDialog(this, "Clarification body is too long.\nMaximum of 2048 characters allowed.", "Error", JOptionPane.WARNING_MESSAGE);
			} else if (textAreaC.getText().length() < 20) {
				JOptionPane.showMessageDialog(this, "Clarification body is too short.\nClarifications must be at least 20 characters, but no more than 2048.", "Error", JOptionPane.WARNING_MESSAGE);
			} else {
				Connection conn = null;
				PreparedStatement stmt = null;
				try {
					Class.forName(JDBC_DRIVER);

					conn = DriverManager.getConnection(Main.Configuration.get("jdbc_mysql_address"), Main.Configuration.get("mysql_user"), Main.Configuration.get("mysql_pass"));

					String sql = "INSERT INTO clarifications (team, problem, text) VALUES (?, ?, ?)";
					stmt = conn.prepareStatement(sql);

					stmt.setInt(1, Integer.parseInt(String.valueOf(Main.accountID)));
					stmt.setString(2, String.valueOf(problemsC.getSelectedItem()));
					stmt.setString(3, String.valueOf(textAreaC.getText()));

					textAreaC.setText("");

					stmt.executeUpdate();

					stmt.close();
					conn.close();

					updateCList(false);
				} catch (Exception ex) {
					ex.printStackTrace();
				} finally {
					try {
						if (stmt != null) {
							stmt.close();
						}
					} catch (Exception ex2) {
						ex2.printStackTrace();
					}
					try {
						if (conn != null) {
							conn.close();
						}
					} catch (Exception ex2) {
						ex2.printStackTrace();
					}
				}
			}
		});
		clarificationsPanel.add(submitC);

		pane.addTab("Submit", submitPanel);
		pane.addTab("Clarifications", clarificationsPanel);

		Timer timer = new Timer();
		TimerTask myTask = new TimerTask() {
			@Override
			public void run() {
				updateCList(false);
			}
		};
		timer.schedule(myTask, 10000, 10000);

		updateCList(false);

		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}

	private void updateCList(boolean forced) {
		Instant now = Instant.now();
		boolean query = false;
		if (!forced) query = true;
		if (forced) {
			if ((now.getEpochSecond() - old.getEpochSecond()) > 10) {
				query = true;
				old = now;
			}
		}
		if (query) {
			Connection conn = null;
			PreparedStatement stmt = null;
			try {
				Class.forName(JDBC_DRIVER);

				conn = DriverManager.getConnection(Main.Configuration.get("jdbc_mysql_address"), Main.Configuration.get("mysql_user"), Main.Configuration.get("mysql_pass"));

				String sql = "SELECT * FROM clarifications WHERE response IS NOT NULL";
				stmt = conn.prepareStatement(sql);

				ResultSet rs = stmt.executeQuery();

				ArrayList<String> clarifications = new ArrayList<>();
				clarificationDatas.clear();
				StringBuilder builder = new StringBuilder();
				while (rs.next()) {
					builder.setLength(0);
					builder.append(rs.getInt("id")).append(".  ").append(rs.getString("problem")).append(" - ").append(rs.getString("text"));
					clarifications.add(builder.toString());
					clarificationDatas.add(new ClarificationData(rs.getInt("id"), rs.getString("problem"), rs.getString("text"), rs.getString("response")));

					int rsId = Integer.parseInt(rs.getString("id"));
					if (rsId > lastId) {
						lastId = rsId;
						displayClarification(rs);
					}
				}
				int selected = cList.getSelectedIndex();
				String[] listOfItems = new String[clarifications.size()];
				for (int i = 0; i < clarifications.size(); i++) {
					listOfItems[i] = clarifications.get(i);
				}
				cList.setListData(listOfItems);
				cList.setSelectedIndex(selected);

				stmt.close();
				conn.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			} finally {
				try {
					if (stmt != null) {
						stmt.close();
					}
				} catch (Exception ex2) {
					ex2.printStackTrace();
				}
				try {
					if (conn != null) {
						conn.close();
					}
				} catch (Exception ex2) {
					ex2.printStackTrace();
				}
			}
		}
	}

	private void displayClarification(ResultSet row) {
		try {
			String problem = row.getString("problem");
			String text = row.getString("text");
			String response = row.getString("response");
			new ClarificationDisplay(problem, text, response);
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}
}
