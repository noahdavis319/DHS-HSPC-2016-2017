package hspc.gradingprogram;

import java.io.File;
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
 * Creates a FileSystem watcher to watch for new submissions.
 * Once a new submission has been created a StartWatcher is created to watch for a start file.
 */
class SubmissionWatcher extends Thread {

    private WatchService watcher;
    private Path dir;

    /**
     * Default constructor. Creates and configures a FileSystem watcher.
     *
     * @throws Exception Handles exception for threading.
     */
    SubmissionWatcher() throws Exception {
        watcher = FileSystems.getDefault().newWatchService();
        dir = Paths.get(Main.Configuration.get("subdir"));
        dir.register(watcher, ENTRY_CREATE);
        start();
    }

    /**
     * Watches the submissions directory for new submissions.
     * Creates a start watcher when a new submission is created.
     */
    public void run() {
        while (!isInterrupted()) {
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
                    // New submission created
                    // Create StartWatcher for this directory
                    try {
                        // Check if the created file is a directory
                        if (new File(dir.toString()).isDirectory()) {
                            Path p = Paths.get(dir.toString() + "\\" + fileName);
                            new StartWatcher(p);
                        }
                    } catch (java.lang.Exception x) {
                        x.printStackTrace();
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
