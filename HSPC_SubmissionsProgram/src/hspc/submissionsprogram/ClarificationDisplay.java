package hspc.submissionsprogram;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Noah Davis on 5/3/2016.
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

class ClarificationDisplay extends JFrame {
	ClarificationDisplay(String problem, String text, String response) {
		this.setTitle(problem);
		this.setResizable(false);

		JPanel panel = new JPanel(null);
		panel.setPreferredSize(new Dimension(500, 415));
		this.add(panel);

		JLabel problemLabel = new JLabel(problem);
		problemLabel.setBounds(12, 12, 476, 25);
		problemLabel.setForeground(Color.BLUE);
		problemLabel.setHorizontalAlignment(JLabel.CENTER);
		panel.add(problemLabel);

		JLabel textLabel = new JLabel("Clarification");
		textLabel.setBounds(12, 37, 476, 25);
		textLabel.setForeground(Color.BLUE);
		textLabel.setHorizontalAlignment(JLabel.CENTER);
		panel.add(textLabel);

		UIManager.put("TextArea.margin", new Insets(8, 8, 8, 8));
		JTextArea textArea = new JTextArea(text);
		textArea.setEditable(false);
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);

		JScrollPane areaScrollPane = new JScrollPane(textArea);
		areaScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		areaScrollPane.setBounds(12, 61, 476, 138);
		panel.add(areaScrollPane);

		JLabel responseLabel = new JLabel("Response");
		responseLabel.setBounds(12, 199, 476, 25);
		responseLabel.setForeground(Color.BLUE);
		responseLabel.setHorizontalAlignment(JLabel.CENTER);
		panel.add(responseLabel);

		JTextArea textAreaS = new JTextArea(response);
		textAreaS.setEditable(false);
		textAreaS.setLineWrap(true);
		textAreaS.setWrapStyleWord(true);

		JScrollPane areaScrollPaneS = new JScrollPane(textAreaS);
		areaScrollPaneS.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		areaScrollPaneS.setBounds(12, 223, 476, 138);
		panel.add(areaScrollPaneS);

		JButton close = new JButton("Close");
		close.setBounds(170, 373, 160, 30);
		close.addActionListener(e -> this.dispose());
		panel.add(close);

		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}
}
