package hspc.gradingprogram;

import java.util.ArrayList;

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
 * Handles queueing for concurrent submission grading.
 * <p>
 * Creates a user defined amount of SubmissionManagers and assigns submissions to the next available SubmissionManager for load balancing.
 */
class ConcurrentManager {
    private static final ArrayList<SubmissionManager> managers = new ArrayList<>();
    private static int managerIndex = 0;

    /**
     * Creates a defined amount of SubmissionManagers.
     *
     * @param numberMangers The number of SubmissionManagers to create.
     */
    void CreateManagers(int numberMangers) {
        for (int i = 0; i < numberMangers; i++) {
            managers.add(new SubmissionManager());
        }
    }

    /**
     * Adds a Grader object to a SubmissionManager to be handled.
     *
     * @param grader The Grader object to add.
     */
    void AddGrader(String name, Grader grader) {
        Display.AddLine("Added submission " + name + " @" + managers.get(managerIndex).toString().split("@")[1]);
        managers.get(managerIndex).AddGrader(new Grader(grader.getDir(), managers.get(managerIndex)));
        if (managerIndex == (managers.size() - 1)) {
            managerIndex = 0;
        } else {
            managerIndex++;
        }
    }
}
