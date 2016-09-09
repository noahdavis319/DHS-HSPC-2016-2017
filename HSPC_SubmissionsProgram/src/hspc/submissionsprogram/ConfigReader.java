package hspc.submissionsprogram;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 * Created by Noah Davis on 4/24/2016.
 * Modified by Noah Davis on 5/3/2016.
 * - Reads from packaged files.
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

class ConfigReader {
	private final HashMap<String, String> configData;

	ConfigReader() {
		this.configData = new HashMap<>();
		try {
			InputStream is = getClass().getResourceAsStream("/hspc/submissionsprogram/config.ini");
			try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
				for (String line; (line = br.readLine()) != null; ) {
					if (line.length() > 0 && line.charAt(0) != ';' && line.trim().charAt(0) != ';' && line.charAt(0) != '[' && line.charAt(0) != ']') {
						String lineNoComment = line.split(";")[0];
						configData.put((lineNoComment.substring(0, lineNoComment.indexOf(":"))).trim(), lineNoComment.substring(lineNoComment.indexOf(":") + 1).trim());
					}
				}
			} catch (IOException x) {
				System.out.println("Error loading config.ini file:");
				x.printStackTrace();
			}
		} catch (Exception ex) {
			System.out.println("Configuration file (config.ini) not found.");
			ex.printStackTrace();
		}
	}

	HashMap<String, String> getData() {
		return configData;
	}
}
