<%--
 * Created by Noah Davis on 5/8/2016.
 * 
 * This work is licensed under a
 * Creative Commons Attribution 4.0
 * International License.
 * 
 * You can read more about the license by
 * visiting the link provided below.
 * http://creativecommons.org/licenses/by/4.0/legalcode
 * 
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
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="hspc.scoreboard.Scoreboard" %>
<html>
<head>
    <title>Scoreboard</title>
</head>
<body>
<%
    out.println(new Scoreboard().CreateTable());
%>
</body>
</html>
