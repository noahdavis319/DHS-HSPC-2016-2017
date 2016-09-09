package hspc.submissionsprogram;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Noah Davis on 9/7/2016.
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
class ResultDisplay extends JFrame {
    ResultDisplay(int code, String problemName) {
        this.setTitle(problemName);
        this.setResizable(false);

        JPanel panel = new JPanel(null);
        panel.setPreferredSize(new Dimension(500, 200));
        this.add(panel);

        JLabel problemLabel = new JLabel(problemName);
        problemLabel.setBounds(12, 12, 476, 25);
        problemLabel.setForeground(Color.BLUE);
        problemLabel.setHorizontalAlignment(JLabel.CENTER);
        panel.add(problemLabel);

        JLabel textLabel = new JLabel();
        textLabel.setBounds(12, 37, 476, 97);
        textLabel.setForeground(Color.BLUE);
        textLabel.setHorizontalAlignment(JLabel.CENTER);
        textLabel.setVerticalAlignment(JLabel.CENTER);

        switch (code) {
            case 1:
                textLabel.setText("Success!");
                break;
            case 2:
                textLabel.setText("Compilation error!");
                break;
            case 3:
                textLabel.setText("Outputs do not match!");
                break;
            case 4:
                textLabel.setText("Execution lasted longer than " + getExecutionAsString());
                break;
            case 5:
                textLabel.setText("A runtime error has occurred.");
                break;
            default:
                textLabel.setText("An error has occurred. Please contact an administrator.");
                break;
        }

        panel.add(textLabel);

        JButton close = new JButton("Close");
        close.setBounds(12, 158, 476, 30);
        close.addActionListener(e -> this.dispose());
        panel.add(close);

        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    private static String getExecutionAsString() {
        int executionTimeInSeconds = Integer.parseInt(Main.Configuration.get("execution_time"));
        int hours   = (int) Math.floor(executionTimeInSeconds / 3600);
        int minutes = (int) Math.floor(executionTimeInSeconds % 3600 / 60);
        int seconds = (int) Math.floor(executionTimeInSeconds % 3600 % 60);
        if (hours > 0) {
            return hours + ":" + String.format("%02d", minutes) + ":" + String.format("%02d", seconds) + ((hours == 1) ? " hour." : " hours.");
        } else if (minutes > 0) {
            return minutes + ":" + String.format("%02d", seconds) + ((minutes == 1) ? " minute." : " minutes.");
        } else {
            return seconds + ((seconds == 1) ? " second." : " seconds.");
        }
    }
}
