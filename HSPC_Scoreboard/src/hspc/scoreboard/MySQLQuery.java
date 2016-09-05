package hspc.scoreboard;

import javax.naming.Context;
import javax.naming.InitialContext;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Noah Davis on 5/10/2016.
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

class MySQLQuery {
    private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";

    static ArrayList<HashMap<String, String>> executeQuery(String sql) {
        ArrayList<HashMap<String, String>> data = null;
        try {
            Context env = (Context) new InitialContext().lookup("java:comp/env");
            Connection conn = null;
            PreparedStatement stmt = null;
            try {
                Class.forName(JDBC_DRIVER);
                conn = DriverManager.getConnection((String) env.lookup("jdbc_mysql_address"), (String) env.lookup("mysql_user"), (String) env.lookup("mysql_pass"));
                stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery();
                data = new ResultSetData(rs).getData();
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
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return data;
    }
}

class ResultSetData {
    private final ArrayList<HashMap<String, String>> data = new ArrayList<>();

    ResultSetData(ResultSet rs) {
        try {
            ResultSetMetaData metaData = rs.getMetaData();
            int colCount = metaData.getColumnCount();
            while (rs.next()) {
                HashMap<String, String> rowData = new HashMap<>();
                for (int i = 1; i <= colCount; i++) {
                    Object columnName = metaData.getColumnLabel(i);
                    Object columnValue = rs.getObject(i);
                    rowData.put(String.valueOf(columnName), String.valueOf(columnValue));
                }
                data.add(rowData);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    ArrayList<HashMap<String, String>> getData() {
        return this.data;
    }
}
