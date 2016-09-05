package hspc.gradingprogram;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

/**
 * Created by Noah Davis on 4/26/2016.
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
 * Updates the MySQL database to contain the results of the submission.
 */
class ResultsHandler {

    private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    //private static final String DB_URL = "jdbc:mysql://localhost/hspc?useSSL=false";
    private final String name;
    private final int code;

    /**
     * Default constructor.
     *
     * @param name The name of the submission on the file system.
     * @param code The return code of the submission.
     */
    ResultsHandler(String name, int code) {
        this.name = name;
        this.code = code;
        this.Execute();
    }

    /**
     * Updates the MySQL database to contain the results of the submission.
     */
    private void Execute() {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            Class.forName(JDBC_DRIVER);

            conn = DriverManager.getConnection(Main.Configuration.get("jdbc_mysql_address"), Main.Configuration.get("mysql_user"), Main.Configuration.get("mysql_pass"));

            String sql = "UPDATE submissions SET code=? WHERE name=?";
            stmt = conn.prepareStatement(sql);

            stmt.setInt(1, Integer.parseInt(String.valueOf(code)));
            stmt.setString(2, String.valueOf(name));

            stmt.executeUpdate();

            stmt.close();
            conn.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (Exception ex2) {
                ex2.printStackTrace();
            }
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception ex2) {
                ex2.printStackTrace();
            }
        }
    }
}
