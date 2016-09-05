package hspc.gradingprogram;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;

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
 * Loads an INI file into a HashMap object.
 * Does not follow the full INI format specifications.
 */
class ConfigReader {
    private final HashMap<String, String> configData;

    /**
     * Reads a configuration file and loads the settings into a HashMap
     *
     * @param dir The path to the configuration file.
     */
    ConfigReader(Path dir) {
        this.configData = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(dir.toString()))) {
            for (String line; (line = br.readLine()) != null; ) {
                if (line.length() > 0 && line.charAt(0) != ';' && line.charAt(0) != '[' && line.charAt(0) != ']') {
                    String lineNoComment = line.split(";")[0];
                    configData.put((lineNoComment.substring(0, lineNoComment.indexOf(":"))).trim(), lineNoComment.substring(lineNoComment.indexOf(":") + 1).trim());
                }
            }
        } catch (IOException x) {
            Display.AddLine("Error loading config file:\n" + x);
        }
    }

    /**
     * Returns the parsed configuration settings.
     *
     * @return The configuration settings.
     */
    HashMap<String, String> getData() {
        return configData;
    }
}
