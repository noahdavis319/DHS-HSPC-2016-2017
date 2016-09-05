package hspc.gradingprogram;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Noah Davis on 3/20/2016.
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
 * Executes a submission using the JavaProcess class.
 */
class SubmissionExecuter {

    private final Path dir;
    private final String name;
    private final HashMap<String, String> config;

    /**
     * Default constructor.
     *
     * @param dir    The directory of the submission.
     * @param name   The name of the submission on the file system.
     * @param config The configuration data of the submission.
     */
    SubmissionExecuter(Path dir, String name, HashMap<String, String> config) {
        this.dir = dir;
        this.name = name;
        this.config = config;
    }

    /**
     * Calls to a JavaProcess object specifying the flags that should be passed.
     *
     * @return Returns the result of the execution as a Map object.
     */
    Map<String, Object> ExecuteSubmission() {
        List<String> command = new ArrayList<>();
        command.add("java");                            // We want to use the Java compiler
        command.add("-classpath");                      // We want to assign classpath directory
        command.add(dir.toString());                    // Set the classpath directory
        command.add(name);                              // Set the directory to the file we want to execute

        return new JavaProcess(config).Execute(command);
    }
}
