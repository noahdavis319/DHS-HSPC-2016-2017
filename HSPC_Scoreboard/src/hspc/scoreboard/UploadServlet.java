package hspc.scoreboard;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.*;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Noah Davis on 5/6/2016.
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

@WebServlet("/submit")
@MultipartConfig
public class UploadServlet extends HttpServlet {
    private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    //private static final String DB_URL = "jdbc:mysql://localhost/hspc?useSSL=false";
    private final SecureRandom random = new SecureRandom();

    private String CreateUniqueSubmissionID() {
        return new BigInteger(130, random).toString(32);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendRedirect("index.jsp");
    }

    private String getFileName(final Part part) {
        for (String content : part.getHeader("content-disposition").split(";")) {
            if (content.trim().startsWith("filename")) {
                return content.substring(content.indexOf('=') + 1).trim().replace("\"", "");
            }
        }
        return null;
    }

    @SuppressWarnings("ConstantConditions")
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String accountID = request.getParameter("accountID");
        String problem = request.getParameter("problem");
        Part filePart = request.getPart("submission");
        String fileName = getFileName(filePart);

        try {
            Context env = (Context) new InitialContext().lookup("java:comp/env");
            Connection conn = null;
            PreparedStatement stmt = null;
            File uploads = new File((String) env.lookup("subdir"));
            String folderName = CreateUniqueSubmissionID();
            File folder = new File(uploads.getAbsolutePath() + File.separator + folderName);
            System.out.println(folder.getAbsolutePath());
            if (folder.mkdir()) {
                try {
                    Class.forName(JDBC_DRIVER);

                    conn = DriverManager.getConnection((String) env.lookup("jdbc_mysql_address"), (String) env.lookup("mysql_user"), (String) env.lookup("mysql_pass"));

                    String sql = "INSERT INTO submissions (team, problem, problem_id, name) VALUES (?, ?, ?, ?)";
                    stmt = conn.prepareStatement(sql);

                    stmt.setInt(1, Integer.parseInt(String.valueOf(accountID)));
                    stmt.setString(2, String.valueOf(problem));
                    stmt.setInt(3, Integer.parseInt(String.valueOf(problem).substring(0, 2)));
                    stmt.setString(4, String.valueOf(folderName));

                    stmt.executeUpdate();

                    stmt.close();
                    conn.close();

                    File file = new File(folder.getAbsolutePath() + File.separator + fileName);

                    try (InputStream input = filePart.getInputStream()) {
                        Files.copy(input, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    }

                    BufferedWriter out = null;
                    try {
                        StringBuilder builder = new StringBuilder();
                        String separator = System.getProperty("line.separator");
                        builder.append("teamid: ").append(accountID).append(separator);
                        builder.append("problem: ").append(problem).append(separator);
                        builder.append("name: ").append(folderName).append(separator);

                        FileWriter fStream = new FileWriter(folder.getAbsoluteFile() + File.separator + "config", true);
                        out = new BufferedWriter(fStream);
                        out.write(builder.toString());
                    } catch (IOException e) {
                        System.err.println("Error: " + e.getMessage());
                    } finally {
                        if (out != null) {
                            try {
                                out.close();
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                        }
                    }

                    try {
                        new File(folder.getAbsolutePath() + File.separator + "start").createNewFile();
                    } catch (IOException ioe) {
                        System.out.println("Error while creating empty file: " + ioe);
                    }

                    ArrayList<HashMap<String, String>> rs = MySQLQuery.executeQuery("SELECT COUNT(*) as count FROM submissions");

                    PrintWriter output = response.getWriter();
                    output.println(rs.get(0).get("count"));
                    output.close();

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
            } else {
                System.out.println("Start file could not be created.");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}