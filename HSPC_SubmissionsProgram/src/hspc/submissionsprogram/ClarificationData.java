package hspc.submissionsprogram;

/**
 * Created by Noah Davis on 5/5/2016.
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

class ClarificationData {
	private final int id;
	private final String problem;
	private final String text;
	private final String response;

	ClarificationData(int id, String problem, String text, String response) {
		this.id = id;
		this.problem = problem;
		this.text = text;
		this.response = response;
	}

	int getId() {
		return this.id;
	}

	String getProblem() {
		return this.problem;
	}

	String getText() {
		return this.text;
	}

	String getResponse() {
		return this.response;
	}
}
