package hspc.submissionsprogram;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Created by Noah Davis on 4/26/2016.
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

class LoginDisplay extends JFrame {

	private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	//private static final String DB_URL = "jdbc:mysql://localhost/hspc?useSSL=false";

	LoginDisplay() {
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.setResizable(false);

		WindowListener exitListener = new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		};
		this.addWindowListener(exitListener);

		JPanel panel = new JPanel(null);
		panel.setPreferredSize(new Dimension(500, 200));
		this.add(panel);

		JLabel title = new JLabel("Dominion High School Programming Contest");
		title.setFont(new Font(UIManager.getDefaults().getFont("Label.font").getName(), Font.BOLD, 16));
		title.setBounds(0, 25, 500, 25);
		title.setHorizontalAlignment(JLabel.CENTER);
		title.setVerticalAlignment(JLabel.CENTER);
		panel.add(title);

		JLabel userLabel = new JLabel("Username:");
		userLabel.setBounds(25, 75, 65, 25);
		panel.add(userLabel);

		JLabel passLabel = new JLabel("Password:");
		passLabel.setBounds(25, 125, 65, 25);
		panel.add(passLabel);

		JTextField userField = new JTextField();
		userField.setBounds(100, 75, 375, 25);
		panel.add(userField);

		JTextField passField = new JTextField();
		passField.setBounds(100, 125, 375, 25);
		panel.add(passField);

		JButton login = new JButton("Login");
		login.setBounds(170, 160, 160, 30);
		login.addActionListener(e -> {
			Connection conn = null;
			PreparedStatement stmt = null;
			try {
				Class.forName(JDBC_DRIVER);

				conn = DriverManager.getConnection(Main.Configuration.get("jdbc_mysql_address"), Main.Configuration.get("mysql_user"), Main.Configuration.get("mysql_pass"));

				String sql = "SELECT id, username, password FROM teams WHERE username=? AND password=?";
				stmt = conn.prepareStatement(sql);
				stmt.setString(1, userField.getText());
				stmt.setString(2, passField.getText());
				ResultSet rs = stmt.executeQuery();

				if (!rs.next()) {
					JOptionPane.showMessageDialog(this, "Invalid login credentials.", "Error", JOptionPane.ERROR_MESSAGE);
				} else {
					do {
						JOptionPane.showMessageDialog(this, "Successfully logged in!", "Success", JOptionPane.INFORMATION_MESSAGE);
						Main.login(Integer.toString(rs.getInt("id")));

						this.dispose();
						//this.setVisible(false);
					} while (rs.next());
				}

				rs.close();
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
		});
		panel.add(login);

		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}
}
