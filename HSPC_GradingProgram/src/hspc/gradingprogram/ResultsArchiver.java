package hspc.gradingprogram;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

/**
 * Created by Noah Davis on 5/8/2016.
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
 * Saves the standard output and standard error of a submission to the file system.
 */
class ResultsArchiver {

    /**
     * Saves the standard output and standard error of a submission to the file system.
     *
     * @param name The name of the submission on the file system.
     * @param results The Map object returned from the JavaProcess for the submission.
     */
    static void SaveResults(String name, Map<String, Object> results) {
        // Save output data
        BufferedWriter out = null;
        if (results.containsKey("output")) {
            try {
                FileWriter fStream = new FileWriter(Main.Configuration.get("subdir") + File.separator + name + File.separator + "output", true);
                out = new BufferedWriter(fStream);
                out.write(results.get("output").toString());
            } catch (IOException e) {
                System.err.println("Error: " + e.getMessage());
            } finally {
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }

        // Save error data
        if (results.containsKey("error")) {
            try {
                FileWriter fStream = new FileWriter(Main.Configuration.get("subdir") + File.separator + name + File.separator + "error", true);
                out = new BufferedWriter(fStream);
                out.write(results.get("error").toString());
            } catch (IOException e) {
                System.err.println("Error: " + e.getMessage());
            } finally {
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }
    }
}
