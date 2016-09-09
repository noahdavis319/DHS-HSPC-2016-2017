package hspc.submissionsprogram;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Noah Davis on 9/6/2016.
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
class ResultWatcher {
    private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    private int rowIndex;

    ResultWatcher(int rowIndex) {
        this.rowIndex = rowIndex;
        this.watch();
    }

    private void watch() {
        Timer timer = new Timer();
        TimerTask checkTask = new TimerTask() {
            @Override
            public void run() {
                if (check()) {
                    timer.cancel();
                }
            }
        };
        timer.schedule(checkTask, 10000, 10000);
    }

    private boolean check() {
        boolean returnValue = false;
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            Class.forName(JDBC_DRIVER);

            conn = DriverManager.getConnection(Main.Configuration.get("jdbc_mysql_address"), Main.Configuration.get("mysql_user"), Main.Configuration.get("mysql_pass"));

            String sql = "SELECT code, problem FROM submissions WHERE id = ?";
            stmt = conn.prepareStatement(sql);

            stmt.setInt(1, rowIndex);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int code = rs.getInt("code");
                if (code != 0) {
                    AppDisplay.displayResult(code, rs.getString("problem"));
                    returnValue = true;
                }
            }

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
        return returnValue;
    }
}
