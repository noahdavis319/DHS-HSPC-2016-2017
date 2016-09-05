package hspc.scoreboard;

import javax.servlet.http.HttpServlet;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Noah Davis on 5/9/2016.
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

public class Scoreboard extends HttpServlet {
    public String CreateTable() {
        StringBuilder builder = new StringBuilder();
        try {
            ArrayList<HashMap<String, String>> rs = MySQLQuery.executeQuery("SELECT\n" +
                    "\tA.team AS team,\n" +
                    "\tA.name as team_name,\n" +
                    "\tIFNULL(number_correct_problems, 0) AS number_correct_problems,\n" +
                    "\tIFNULL((number_failed_attempts * 15), 0) AS penalty_time,\n" +
                    "\tIFNULL(correct_data, \"NULL\") AS correct_data,\n" +
                    "\tIFNULL(attempt_data, \"NULL\") AS attempt_data\n" +
                    "FROM (\n" +
                    "\tSELECT\n" +
                    "\t\tid AS team,\n" +
                    "\t\tname\n" +
                    "\tFROM\n" +
                    "\t\thspc.teams\n" +
                    ") AS A\n" +
                    "LEFT JOIN (\n" +
                    "\tSELECT\n" +
                    "\t\tteam,\n" +
                    "\t\tGROUP_CONCAT(CONCAT(problem, \"|\", time) ORDER BY problem_id ASC SEPARATOR '^') AS correct_data\n" +
                    "\tFROM (\n" +
                    "\t\tSELECT\n" +
                    "\t\t\tteam,\n" +
                    "\t\t\tproblem,\n" +
                    "\t\t\tproblem_id,\n" +
                    "\t\t\ttime\n" +
                    "\t\tFROM \n" +
                    "\t\t\thspc.submissions\n" +
                    "\t\tWHERE\n" +
                    "\t\t\tcode = 1\n" +
                    "\t\tGROUP BY\n" +
                    "\t\t\tteam,\n" +
                    "\t\t\tproblem\n" +
                    "\t) AS AA\n" +
                    "\tGROUP BY\n" +
                    "\t\tteam\n" +
                    ") AS B\n" +
                    "ON\n" +
                    "\t(A.team = B.team)\n" +
                    "LEFT JOIN (\n" +
                    "\tSELECT\n" +
                    "\t\tteam,\n" +
                    "\t\tGROUP_CONCAT(CONCAT(problem, \"|\", count) ORDER BY problem_id ASC SEPARATOR '^') as attempt_data\n" +
                    "\tFROM (\n" +
                    "\t\tSELECT\n" +
                    "\t\t\tteam,\n" +
                    "\t\t\tproblem,\n" +
                    "\t\t\tCOUNT(*) AS count,\n" +
                    "\t\t\tproblem_id\n" +
                    "\t\tFROM\n" +
                    "\t\t\thspc.submissions\n" +
                    "\t\tWHERE\n" +
                    "\t\t\tcode > 1\n" +
                    "\t\tGROUP BY\n" +
                    "\t\t\tteam,\n" +
                    "\t\t\tproblem\n" +
                    "\t) AS BB\n" +
                    "\tGROUP BY\n" +
                    "\t\tteam\n" +
                    ") AS C\n" +
                    "ON\n" +
                    "\t(A.team = C.team)\n" +
                    "LEFT JOIN (\n" +
                    "\tSELECT\n" +
                    "\t\tteam,\n" +
                    "\t\tCOUNT(*) AS number_correct_problems,\n" +
                    "\t\tSUM(time) as sum_time_correct\n" +
                    "\tFROM\n" +
                    "\t\thspc.submissions\n" +
                    "\tWHERE\n" +
                    "\t\tcode = 1\n" +
                    "\tGROUP BY\n" +
                    "\t\tteam\n" +
                    ") AS D\n" +
                    "ON\n" +
                    "\t(A.team = D.team)\n" +
                    "LEFT JOIN (\n" +
                    "\tSELECT\n" +
                    "\t\tteam,\n" +
                    "\t\tCOUNT(*) AS number_failed_attempts\n" +
                    "\tFROM\n" +
                    "\t\thspc.submissions\n" +
                    "\tWHERE\n" +
                    "\t\tcode > 1\n" +
                    "\tGROUP BY\n" +
                    "\t\tteam\n" +
                    ") AS E\n" +
                    "ON\n" +
                    "\t(A.team = E.team)\n" +
                    "ORDER BY\n" +
                    "\tnumber_correct_problems DESC,\n" +
                    "\tsum_time_correct ASC,\n" +
                    "\tnumber_failed_attempts ASC");

            builder.append("<table style=\"width: 100%\">\n" +
                    "\t<tr>\n" +
                    "\t\t<th style=\"width: 11%\">\n" +
                    "\t\t\tName\n" +
                    "\t\t</th>\n" +
                    "\t\t<th style=\"width: 11%\">\n" +
                    "\t\t\tCorrect\n" +
                    "\t\t</th>\n" +
                    "\t\t<th style=\"width: 11%\">\n" +
                    "\t\t\tTime\n" +
                    "\t\t</th>\n");
            for (int i = 0; i < 6; i++) {
                builder.append("\t\t<th style=\"width: 11%\">\n" +
                        "\t\t\tProblem ");
                builder.append((i + 1));
                builder.append("\n" +
                        "\t\t</th>\n");
            }
            builder.append("</tr>");
            StringBuilder loopBuilder = new StringBuilder();
            for (HashMap<String, String> row : rs) {
                builder.append("<tr>");

                builder.append("<td align=\"center\">");
                builder.append(row.get("team_name"));
                builder.append("</td>");

                String[] problemData = row.get("correct_data").split("\\^");
                builder.append("<td align=\"center\">");
                if (!row.get("correct_data").equals("NULL")) {
                    for (int i = 0; i < problemData.length; i++) {
                        loopBuilder.append(Integer.parseInt(problemData[i].substring(0, 2)));
                        if (i < problemData.length - 1)
                            loopBuilder.append(", ");
                    }
                    builder.append(loopBuilder.toString());
                }
                loopBuilder.setLength(0);

                builder.append("</td>");

                builder.append("<td align=\"center\">");

                Calendar dateC = new GregorianCalendar();
                dateC.set(Calendar.HOUR_OF_DAY, 0);
                dateC.set(Calendar.MINUTE, 0);
                dateC.set(Calendar.SECOND, 0);
                dateC.set(Calendar.MILLISECOND, 0);

                Date morningDate = dateC.getTime();

                long morning_time = morningDate.getTime();
                long total_time = 0;
                if (!row.get("correct_data").equals("NULL")) {
                    SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date;
                    for (String pData : problemData) {
                        date = inputFormat.parse(pData.split("\\|")[1]);
                        total_time += (date.getTime() - morning_time);
                    }
                    total_time /= 1000;
                    builder.append(String.format("%02d", (int) Math.floor(total_time / 3600)));
                    builder.append(":");
                    builder.append(String.format("%02d", (int) Math.floor(total_time % 3600 / 60)));
                    builder.append(":");
                    builder.append(String.format("%02d", (int) Math.floor(total_time % 3600 % 60)));
                } else {
                    builder.append("N/A3");
                }

                builder.append("</td>");

                String[] c_split = row.get("correct_data").split("\\^");
                if (row.get("correct_data").equals("NULL")) {
                    for (int i = 0; i < 6; i++) {
                        builder.append("<td align=\"center\">");
                        builder.append("N/A5");
                        builder.append("</td>");
                    }
                } else {
                    for (int i = 0; i < 6; i++) {
                        builder.append("<td align=\"center\" ");
                        boolean found = false;
                        for (String c : c_split) {
                            if (Integer.parseInt(c.substring(0, 2)) == (i + 1)) {
                                builder.append("style='background-color:lime'>");
                                builder.append(c.split("\\|")[1].split(" ")[1]);
                                found = true;
                            }
                        }
                        if (!found) {
                            builder.append("N/A");
                        }
                        builder.append("</td>");
                    }
                }

                builder.append("</tr>");
            }
            builder.append("</table>");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return builder.toString();
    }
}
