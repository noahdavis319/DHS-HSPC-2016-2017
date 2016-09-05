package hspc.gradingprogram;

import java.io.File;
import java.nio.file.Paths;
import java.util.HashMap;

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
 * Main class for the grading program.
 * <p>
 * Creates a submission manager, loads configuration data, and creates a display.
 * Starts watching for new submission when activating via the display.
 */
class Main {
    //static final SubmissionManager Manager = new SubmissionManager();
    static final ConcurrentManager Manager = new ConcurrentManager();
    static HashMap<String, String> Configuration;
    private static boolean HasStartedGrading = false;

    /**
     * Main method.
     * <p>
     * Loads configuration information from the configuration file.
     * Instantiates and shows a display.
     *
     * @param args Command line arguments. Unused.
     * @throws Exception Throws exception for threading.
     */
    public static void main(String[] args) throws Exception {
        Configuration = new ConfigReader(Paths.get(System.getProperty("user.dir") + File.separator + "config.ini")).getData();
        Manager.CreateManagers(Integer.valueOf(Configuration.get("concurrent_graders")));
        new Display();
    }

    /**
     * Starts the grading watchers.
     *
     * @throws Exception Throws exception for threading.
     */
    static void StartGrading() throws Exception {
        if (!HasStartedGrading) {
            new SubmissionWatcher();
            HasStartedGrading = true;
        }
    }
}