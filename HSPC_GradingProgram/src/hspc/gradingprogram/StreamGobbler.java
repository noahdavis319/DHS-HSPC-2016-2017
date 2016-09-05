package hspc.gradingprogram;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Noah Davis on 4/24/2016.
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
 * Converts data from an InputStream to a String.
 */
class StreamGobbler extends Thread {
    private final InputStream is;
    private final StringBuilder builder = new StringBuilder();

    /**
     * Default constructor.
     *
     * @param is The InputStream to read from.
     */
    StreamGobbler(InputStream is) {
        this.is = is;
    }

    /**
     * Reads from the InputStream and builds the String.
     */
    public void run() {
        try {
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line;
            while ((line = br.readLine()) != null) {
                builder.append(line).append('\n');
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    /**
     * Returns the data read from the InputStream.
     *
     * @return The InputStream data as a String.
     */
    String getResponse() {
        return builder.toString();
    }
}
