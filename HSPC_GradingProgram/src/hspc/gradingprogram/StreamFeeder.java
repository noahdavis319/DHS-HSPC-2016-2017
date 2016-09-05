package hspc.gradingprogram;

import java.io.*;

/**
 * Created by Noah Davis on 4/24/2016.
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
 * Sends information to the standard input of an OutputStream of a JavaProcess.
 */
class StreamFeeder extends Thread {
    private final OutputStream os;
    private final int problem;
    private final StringBuilder builder = new StringBuilder();

    /**
     * Default constructor. Sets the OutputSteam and problem number.
     *
     * @param os      The OutputStream to feed data into.
     * @param problem The problem of which the data belongs to.
     */
    StreamFeeder(OutputStream os, int problem) {
        this.os = os;
        this.problem = problem;
    }

    /**
     * Reads the problems input data and sends it into the OutputStream.
     */
    public void run() {
        try {
            OutputStreamWriter osr = new OutputStreamWriter(os);
            BufferedWriter bw = new BufferedWriter(osr);
            BufferedReader br = new BufferedReader(new FileReader(ProblemToPath(problem)));
            try {
                String line = br.readLine();
                while (line != null) {
                    builder.setLength(0);

                    builder.append(line).append("\n");
                    bw.write(builder.toString());

                    line = br.readLine();
                }
            } finally {
                try {
                    bw.flush();
                } catch (IOException x) {
                    x.printStackTrace();
                }
                try {
                    bw.close();
                } catch (IOException x) {
                    x.printStackTrace();
                }
                try {
                    br.close();
                } catch (IOException x) {
                    x.printStackTrace();
                }
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    /**
     * Converts a problem integer into an input file path.
     *
     * @param problem The problem number to convert.
     * @return A path, as a string, to the problem's input data.
     */
    private String ProblemToPath(int problem) {
        return Main.Configuration.get("soldir") + File.separator + String.format("%02d", problem) + File.separator + "input";
    }
}
