package hspc.gradingprogram;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;

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
 * Watches a submission directory for a start file.
 * Creates a Grader thread once a start file has been found.
 */
class StartWatcher extends Thread {

    private final Path dir;
    private WatchService watcher;

    /**
     * Default constructor. Creates and configures a FileSystem watcher.
     *
     * @param dir The directory of the submission.
     * @throws Exception Exception for threading.
     */
    StartWatcher(Path dir) throws Exception {
        this.dir = dir;
        watcher = FileSystems.getDefault().newWatchService();
        dir.register(watcher, ENTRY_CREATE);
        start();
    }

    /**
     * Watches the submission directory for a start file.
     * Creates a Grader for this submission and adds the Grader object to the SubmissionManager for handling.
     */
    public void run() {
        while (!this.isInterrupted()) {
            WatchKey key;
            try {
                // Wait for a key to be available
                key = watcher.take();
            } catch (InterruptedException x) {
                x.printStackTrace();
                return;
            }

            for (WatchEvent<?> event : key.pollEvents()) {
                // Get the event type
                WatchEvent.Kind<?> kind = event.kind();

                // Get the file name
                @SuppressWarnings("unchecked")
                WatchEvent<Path> ev = (WatchEvent<Path>) event;
                Path fileName = ev.context();
                if (kind == ENTRY_CREATE) {
                    /**
                     * Check if the file created is named "start" as this
                     * means that the submission files were all moved
                     * over and are ready to be graded.
                     */
                    if (fileName.toString().toLowerCase().equals("start")) {
                        // Delete the start file
                        try {
                            Files.delete(Paths.get(dir + File.separator + fileName));
                        } catch (IOException x) {
                            x.printStackTrace();
                        }

                        // Run a grading class
                        try {
                            Main.Manager.AddGrader(dir.getFileName().toString(), new Grader(dir));
                        } catch (Exception x) {
                            x.printStackTrace();
                        }

                        // Stop this thread
                        this.interrupt();
                    }
                }
            }

            // IMPORTANT: The key must be reset after processed
            boolean valid = key.reset();
            if (!valid) {
                break;
            }
        }
    }
}
