package hspc.gradingprogram;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

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
 * Compiles and executes submissions.
 * Compiles submissions and then parses over the compilation results.
 * If the compilation results contained no errors then execute the submission.
 * Once the submission has finished execution it parses the standard output and error output to determine if the submission was correct.
 */
class Grader extends Thread {

    private final Path dir;
    private SubmissionManager manager;
    private Map<String, Object> compilationResults;

    /**
     * Default constructor assigning a path to the submission.
     *
     * @param dir The directory of the submission.
     */
    Grader(Path dir) {
        this.dir = dir;
    }

    /**
     * Constructor assigning a path and a manager.
     *
     * @param dir     The directory of the submission.
     * @param manager The SubmissionManager of this Grader object.
     */
    Grader(Path dir, SubmissionManager manager) {
        this.dir = dir;
        this.manager = manager;
    }

    Path getDir() {
        return this.dir;
    }

    /**
     * Takes a path variable and scans for the first Java file it finds, returning the file name.
     *
     * @param dir The submission directory to look for the Java file.
     * @return The filename of the submitted Java file.
     */
    private String GetSubmissionName(Path dir) {
        File directory = new File(dir.toString());
        File[] files = directory.listFiles((d, name) -> name.endsWith(".java"));
        return files[0].toString();
    }

    /**
     * Compiles the submission and determines if the submission was able to successfully compile.
     * If the submission was able to successfully compile then attempt to execute the submission.
     * If the submission was able to fully run, then compare the output of the execution and the expected output for the associated problem.
     * Makes an external call to a ResultsHandler object to report the status of the submission.
     */
    public void run() {
        Display.AddLine("Grading " + dir.getFileName() + " @" + manager.toString().split("@")[1]);
        while (!this.isInterrupted()) {
            String fileName = Paths.get(GetSubmissionName(dir)).getFileName().toString();
            SubmissionCompiler compiler = new SubmissionCompiler(dir, fileName);

            ConfigReader cr = new ConfigReader(Paths.get(dir + File.separator + "config"));
            HashMap<String, String> config = cr.getData();

            int problemNumber = Integer.parseInt(config.get("problem").substring(0, 2));
            String name = String.valueOf(config.get("name"));

            try {
                compilationResults = compiler.CompileSubmission();
            } catch (Exception x) {
                x.printStackTrace();
            }
            if ((int) compilationResults.get("code") == 0) {
                fileName = fileName.substring(0, fileName.lastIndexOf('.'));

                SubmissionExecuter executer = new SubmissionExecuter(dir, fileName, config);
                try {
                    Map<String, Object> executionResults = executer.ExecuteSubmission();
                    if ((int) executionResults.get("code") == 0) {
                        Display.AddLine(" - Comparing outputs.");

                        OutputComparator oc = new OutputComparator(problemNumber, executionResults.get("output").toString());

                        if (oc.Compare()) {
                            Display.AddLine(" - The solution is correct!");
                            new ResultsHandler(name, 1);
                        } else {
                            Display.AddLine(" - The solution is NOT correct!");
                            new ResultsHandler(name, 3);
                        }
                    } else if ((int) executionResults.get("code") == Integer.MIN_VALUE) {
                        Display.AddLine(" - Code execution took longer than 30 seconds.");
                        new ResultsHandler(name, 4);
                    } else {
                        Display.AddLine("\n - Error.\n\n" + executionResults.get("error") + "\n");
                        new ResultsHandler(name, 5);
                    }
                    ResultsArchiver.SaveResults(name, executionResults);
                } catch (Exception x) {
                    x.printStackTrace();
                }
            } else {
                Display.AddLine(" - Compilation error.");
                new ResultsHandler(name, 2);
            }
            Display.AddLine(" - Done.");
            end();
        }
    }

    /**
     * Ends this Grader object, reporting to the SubmissionManager that a new Grader object can begin its process.
     */
    private void end() {
        this.interrupt();
        manager.AlertFinish();
    }
}
