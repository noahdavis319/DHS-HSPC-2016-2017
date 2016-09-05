package hspc.gradingprogram;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

/**
 * Created by Noah Davis on 3/18/2016.
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

/**
 * Creates a display for the user to interact with.
 * Allows the user to start various watchers for new submissions.
 */
class Display extends JFrame {

    private static JTextArea textArea;

    /**
     * Creates a frame with a start button and a scrolling text area for log messages.
     */
    Display() {
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.setResizable(false);

        JPanel panel = new JPanel(null);
        panel.setPreferredSize(new Dimension(500, 350));
        this.add(panel);

        WindowListener exitListener = new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        };
        this.addWindowListener(exitListener);

        JButton button = new JButton();
        button.setBounds(10, 10, 480, 30);
        button.setText("Start Grading");
        button.addActionListener(e -> {
            try {
                StartMainGrading();
                button.setText("Running...");
            } catch (java.lang.Exception x) {
                x.printStackTrace();
            }
        });
        panel.add(button);

        UIManager.put("TextArea.margin", new Insets(8, 8, 8, 8));
        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        DefaultCaret caret = (DefaultCaret)textArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        JScrollPane areaScrollPane = new JScrollPane(textArea);
        areaScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        areaScrollPane.setBounds(10, 50, 480, 290);
        panel.add(areaScrollPane);

        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    /**
     * Adds a line to the text area for logging.
     *
     * @param line A message to show in the log.
     */
    static void AddLine(String line) {
        try {
            textArea.append(line);
            textArea.append(System.getProperty("line.separator"));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Starts the grading watchers.
     *
     * @throws Exception Handles exception for threading.
     */
    private void StartMainGrading() throws Exception {
        Main.StartGrading();
    }
}
