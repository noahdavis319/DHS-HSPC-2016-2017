package hspc.gradingprogram;

import java.util.ArrayList;

/**
 * Created by Noah Davis on 5/7/2016.
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
 * Handles Grader objects such that only one runs at a time.
 */
class SubmissionManager {

    private static final ArrayList<Grader> Graders = new ArrayList<>();
    private Grader running = null;

    /**
     * Adds a Grader object to be graded.
     *
     * @param grader The Grader object to add.
     */
    void AddGrader(Grader grader) {
        Graders.add(grader);
        StartFirst();
    }

    /**
     * Tells the SubmissionManager that a new Grader object can start.
     */
    void AlertFinish() {
        if (Graders.size() > 0) {
            Graders.remove(0);
            StartFirst();
        }
    }

    /**
     * Starts the next Grader object.
     */
    private void StartFirst() {
        if (Graders.size() > 0 && Graders.get(0) != running) {
            if (!Graders.get(0).isAlive()) {
                running = Graders.get(0);
                Graders.get(0).start();
            }
        }
    }
}
