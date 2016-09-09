package hspc.gradingprogram;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by Noah Davis on 3/19/2016.
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
 * Creates an external process to compile and execute submissions.
 */
class JavaProcess {

    private boolean input;
    private HashMap<String, String> config;

    /**
     * Default constructor.
     */
    JavaProcess() {
    }

    /**
     * Creates a JavaProcess based on submission data.
     *
     * @param config Information about the submission that is being acted on.
     */
    JavaProcess(HashMap<String, String> config) {
        this.input = true;
        this.config = config;
    }

    /**
     * Compiles or executes the submission depending on the parameters specified in the command object.
     *
     * @param command Flags to be sent to the Java process.
     * @return The process code, standard output, and standard error of this process call.
     */
    Map<String, Object> Execute(List<String> command) {
        HashMap<String, Object> data = new HashMap<>();

        ProcessBuilder pb = new ProcessBuilder(command);
        try {
            Process shell = pb.start();

            if (input) {
                StreamFeeder inputFeeder = new StreamFeeder(shell.getOutputStream(), Integer.parseInt(config.get("problem").substring(0, 2)));
                inputFeeder.start();
            }

            // Capture output from the process
            StreamGobbler errorGobbler = new StreamGobbler(shell.getErrorStream());
            StreamGobbler outputGobbler = new StreamGobbler(shell.getInputStream());
            errorGobbler.start();
            outputGobbler.start();

            // Wait for the shell to finish and get the return code
            int shellExitStatus;
            if (shell.waitFor(Long.parseLong(Main.Configuration.get("timeout")), TimeUnit.SECONDS)) {
                shellExitStatus = shell.exitValue();
            } else {
                // MIN_VALUE is used here to represent that the process took over 30 seconds
                shellExitStatus = Integer.MIN_VALUE;
            }

            data.put("code", shellExitStatus);
            data.put("output", outputGobbler.getResponse());
            data.put("error", errorGobbler.getResponse());

            shell.destroyForcibly();

            return data;
        } catch (IOException e) {
            Display.AddLine(" - Error occurred while executing Java command. Error Description (IOException): "
                    + e.getMessage());
        } catch (InterruptedException e) {
            Display.AddLine(" - Error occurred while executing Java command. Error Description (InterruptedException): "
                    + e.getMessage());
        }

        data.put("code", -1);
        return data;
    }
}