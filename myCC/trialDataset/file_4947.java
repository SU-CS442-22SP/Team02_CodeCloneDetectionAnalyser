    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletConfig config = getServletConfig();
        ServletContext context = config.getServletContext();
        try {
            String driver = context.getInitParameter("driver");
            Class.forName(driver);
            String dbURL = context.getInitParameter("db");
            String username = context.getInitParameter("username");
            String password = "";
            connection = DriverManager.getConnection(dbURL, username, password);
        } catch (ClassNotFoundException e) {
            System.out.println("Database driver not found.");
        } catch (SQLException e) {
            System.out.println("Error opening the db connection: " + e.getMessage());
        }
        String action = "";
        String notice;
        String error = "";
        HttpSession session = request.getSession();
        session.setMaxInactiveInterval(300);
        if (request.getParameter("action") != null) {
            action = request.getParameter("action");
        } else {
            notice = "Unknown action!";
            request.setAttribute("notice", notice);
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/admin/index.jsp");
            dispatcher.forward(request, response);
            return;
        }
        if (action.equals("edit_underscoreevents")) {
            String sql;
            String month_underscorename = "";
            int month;
            int year;
            Event event;
            if (request.getParameter("month") != null) {
                month = Integer.parseInt(request.getParameter("month"));
                String temp = request.getParameter("year_underscorenum");
                year = Integer.parseInt(temp);
                int month_underscorenum = month - 1;
                event = new Event(year, month_underscorenum, 1);
                month_underscorename = event.getMonthName();
                year = event.getYearNumber();
                if (month < 10) {
                    sql = "SELECT * FROM event WHERE date LIKE '" + year + "-0" + month + "-%'";
                } else {
                    sql = "SELECT * FROM event WHERE date LIKE '" + year + "-" + month + "-%'";
                }
            } else {
                event = new Event();
                month_underscorename = event.getMonthName();
                month = event.getMonthNumber() + 1;
                year = event.getYearNumber();
                sql = "SELECT * FROM event WHERE date LIKE '" + year + "-%" + month + "-%'";
            }
            try {
                dbStatement = connection.createStatement();
                dbResultSet = dbStatement.executeQuery(sql);
                request.setAttribute("resultset", dbResultSet);
                request.setAttribute("year", Integer.toString(year));
                request.setAttribute("month", Integer.toString(month));
                request.setAttribute("month_underscorename", month_underscorename);
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/admin/edit_underscoreevents.jsp");
                dispatcher.forward(request, response);
                return;
            } catch (SQLException e) {
                notice = "Error retrieving events from the database.";
                request.setAttribute("notice", notice);
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/admin/index.jsp");
                dispatcher.forward(request, response);
                return;
            }
        } else if (action.equals("edit_underscoreevent")) {
            int id = Integer.parseInt(request.getParameter("id"));
            Event event = new Event();
            event = event.getEvent(id);
            if (event != null) {
                request.setAttribute("event", event);
                request.setAttribute("id", Integer.toString(id));
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/admin/add_underscoreevent.jsp");
                dispatcher.forward(request, response);
                return;
            } else {
                notice = "Error retrieving event from the database.";
                request.setAttribute("notice", notice);
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/admin/index.jsp");
                dispatcher.forward(request, response);
                return;
            }
        } else if (action.equals("save_underscoreevent")) {
            String title = request.getParameter("title");
            String description = request.getParameter("description");
            String month = request.getParameter("month");
            String day = request.getParameter("day");
            String year = request.getParameter("year");
            String start_underscoretime = "";
            String end_underscoretime = "";
            if (request.getParameter("all_underscoreday") == null) {
                String start_underscorehour = request.getParameter("start_underscorehour");
                String start_underscoreminutes = request.getParameter("start_underscoreminutes");
                String start_underscoreampm = request.getParameter("start_underscoreampm");
                String end_underscorehour = request.getParameter("end_underscorehour");
                String end_underscoreminutes = request.getParameter("end_underscoreminutes");
                String end_underscoreampm = request.getParameter("end_underscoreampm");
                if (Integer.parseInt(start_underscorehour) < 10) {
                    start_underscorehour = "0" + start_underscorehour;
                }
                if (Integer.parseInt(end_underscorehour) < 10) {
                    end_underscorehour = "0" + end_underscorehour;
                }
                start_underscoretime = start_underscorehour + ":" + start_underscoreminutes + " " + start_underscoreampm;
                end_underscoretime = end_underscorehour + ":" + end_underscoreminutes + " " + end_underscoreampm;
            }
            Event event = null;
            if (!start_underscoretime.equals("") && !end_underscoretime.equals("")) {
                event = new Event(title, description, month, day, year, start_underscoretime, end_underscoretime);
            } else {
                event = new Event(title, description, month, day, year);
            }
            if (event.saveEvent()) {
                notice = "Calendar event saved!";
                request.setAttribute("notice", notice);
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/admin/index.jsp");
                dispatcher.forward(request, response);
                return;
            } else {
                notice = "Error saving calendar event.";
                request.setAttribute("notice", notice);
                request.setAttribute("event", event);
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/admin/add_underscoreevent.jsp");
                dispatcher.forward(request, response);
                return;
            }
        } else if (action.equals("update_underscoreevent")) {
            String title = request.getParameter("title");
            String description = request.getParameter("description");
            String month = request.getParameter("month");
            String day = request.getParameter("day");
            String year = request.getParameter("year");
            String start_underscoretime = "";
            String end_underscoretime = "";
            int id = Integer.parseInt(request.getParameter("id"));
            if (request.getParameter("all_underscoreday") == null) {
                String start_underscorehour = request.getParameter("start_underscorehour");
                String start_underscoreminutes = request.getParameter("start_underscoreminutes");
                String start_underscoreampm = request.getParameter("start_underscoreampm");
                String end_underscorehour = request.getParameter("end_underscorehour");
                String end_underscoreminutes = request.getParameter("end_underscoreminutes");
                String end_underscoreampm = request.getParameter("end_underscoreampm");
                if (Integer.parseInt(start_underscorehour) < 10) {
                    start_underscorehour = "0" + start_underscorehour;
                }
                if (Integer.parseInt(end_underscorehour) < 10) {
                    end_underscorehour = "0" + end_underscorehour;
                }
                start_underscoretime = start_underscorehour + ":" + start_underscoreminutes + " " + start_underscoreampm;
                end_underscoretime = end_underscorehour + ":" + end_underscoreminutes + " " + end_underscoreampm;
            }
            Event event = null;
            if (!start_underscoretime.equals("") && !end_underscoretime.equals("")) {
                event = new Event(title, description, month, day, year, start_underscoretime, end_underscoretime);
            } else {
                event = new Event(title, description, month, day, year);
            }
            if (event.updateEvent(id)) {
                notice = "Calendar event updated!";
                request.setAttribute("notice", notice);
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/admin/index.jsp");
                dispatcher.forward(request, response);
                return;
            } else {
                notice = "Error updating calendar event.";
                request.setAttribute("notice", notice);
                request.setAttribute("event", event);
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/admin/add_underscoreevent.jsp");
                dispatcher.forward(request, response);
                return;
            }
        } else if (action.equals("delete_underscoreevent")) {
            int id = Integer.parseInt(request.getParameter("id"));
            Event event = new Event();
            if (event.deleteEvent(id)) {
                notice = "Calendar event successfully deleted.";
                request.setAttribute("notice", notice);
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/admin?action=edit_underscoreevents");
                dispatcher.forward(request, response);
                return;
            } else {
                notice = "Error deleting event from the database.";
                request.setAttribute("notice", notice);
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/admin?action=edit_underscoreevents");
                dispatcher.forward(request, response);
                return;
            }
        } else if (action.equals("edit_underscoremembers")) {
            String sql = "SELECT * FROM person ORDER BY lname";
            if (request.getParameter("member_underscoretype") != null) {
                String member_underscoretype = request.getParameter("member_underscoretype");
                if (member_underscoretype.equals("all")) {
                    sql = "SELECT * FROM person ORDER BY lname";
                } else {
                    sql = "SELECT * FROM person where member_underscoretype LIKE '" + member_underscoretype + "' ORDER BY lname";
                }
                request.setAttribute("member_underscoretype", member_underscoretype);
            }
            try {
                dbStatement = connection.createStatement();
                dbResultSet = dbStatement.executeQuery(sql);
                request.setAttribute("resultset", dbResultSet);
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/admin/edit_underscoremembers.jsp");
                dispatcher.forward(request, response);
                return;
            } catch (SQLException e) {
                notice = "Error retrieving members from the database.";
                request.setAttribute("notice", notice);
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/admin/index.jsp");
                dispatcher.forward(request, response);
                return;
            }
        } else if (action.equals("edit_underscoreperson")) {
            int id = Integer.parseInt(request.getParameter("id"));
            String member_underscoretype = request.getParameter("member_underscoretype");
            Person person = new Person();
            person = person.getPerson(id);
            if (member_underscoretype.equals("student")) {
                Student student = person.getStudent();
                request.setAttribute("student", student);
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/admin/edit_underscorestudent.jsp");
                dispatcher.forward(request, response);
                return;
            } else if (member_underscoretype.equals("alumni")) {
                Alumni alumni = person.getAlumni();
                request.setAttribute("alumni", alumni);
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/admin/edit_underscorealumni.jsp");
                dispatcher.forward(request, response);
                return;
            } else if (member_underscoretype.equals("hospital")) {
                Hospital hospital = person.getHospital(id);
                request.setAttribute("hospital", hospital);
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/admin/edit_underscorehospital.jsp");
                dispatcher.forward(request, response);
                return;
            }
        } else if (action.equals("update_underscorealumni")) {
            int person_underscoreid = Integer.parseInt(request.getParameter("person_underscoreid"));
            Person person = new Person();
            person = person.getPerson(person_underscoreid);
            Alumni cur_underscorealumni = person.getAlumni();
            String fname = request.getParameter("fname");
            String lname = request.getParameter("lname");
            String address1 = request.getParameter("address1");
            String address2 = request.getParameter("address2");
            String city = request.getParameter("city");
            String state = request.getParameter("state");
            String zip = request.getParameter("zip");
            String email = request.getParameter("email");
            String company_underscorename = request.getParameter("company_underscorename");
            String position = request.getParameter("position");
            int mentor = 0;
            if (request.getParameter("mentor") != null) {
                mentor = 1;
            }
            String graduation_underscoredate = request.getParameter("graduation_underscoreyear") + "-" + request.getParameter("graduation_underscoremonth") + "-01";
            String password = "";
            if (request.getParameter("password") != null) {
                password = request.getParameter("password");
                MessageDigest md = null;
                try {
                    md = MessageDigest.getInstance("MD5");
                    md.update(password.getBytes("UTF-8"));
                } catch (Exception x) {
                    notice = "Could not encrypt your password.  Please try again.";
                    request.setAttribute("notice", notice);
                    RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/admin?action=edit_underscoremembers");
                    dispatcher.forward(request, response);
                    return;
                }
                password = (new BASE64Encoder()).encode(md.digest());
            } else {
                password = cur_underscorealumni.getPassword();
            }
            int is_underscoreadmin = 0;
            if (request.getParameter("is_underscoreadmin") != null) {
                is_underscoreadmin = 1;
            }
            Alumni new_underscorealumni = new Alumni(fname, lname, address1, address2, city, state, zip, email, password, is_underscoreadmin, company_underscorename, position, graduation_underscoredate, mentor);
            if (!new_underscorealumni.getEmail().equals(cur_underscorealumni.getEmail())) {
                if (new_underscorealumni.checkEmailIsRegistered()) {
                    notice = "That email address is already registered!";
                    request.setAttribute("notice", notice);
                    request.setAttribute("alumni", new_underscorealumni);
                    RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/admin?action=edit_underscoremembers");
                    dispatcher.forward(request, response);
                    return;
                }
            }
            if (!new_underscorealumni.updateAlumni(person_underscoreid)) {
                session.setAttribute("alumni", new_underscorealumni);
                notice = "There was an error saving your information.  Please try again.";
                request.setAttribute("notice", notice);
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/admin?action=edit_underscoremembers");
                dispatcher.forward(request, response);
                return;
            }
            notice = "Member information successfully updated.";
            request.setAttribute("notice", notice);
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/admin?action=edit_underscoremembers");
            dispatcher.forward(request, response);
            return;
        } else if (action.equals("update_underscorehospital")) {
            int person_underscoreid = Integer.parseInt(request.getParameter("person_underscoreid"));
            Person person = new Person();
            person = person.getPerson(person_underscoreid);
            Hospital cur_underscorehospital = person.getHospital(person_underscoreid);
            String fname = request.getParameter("fname");
            String lname = request.getParameter("lname");
            String address1 = request.getParameter("address1");
            String address2 = request.getParameter("address2");
            String city = request.getParameter("city");
            String state = request.getParameter("state");
            String zip = request.getParameter("zip");
            String email = request.getParameter("email");
            String name = request.getParameter("name");
            String phone = request.getParameter("phone");
            String url = request.getParameter("url");
            String password = "";
            if (cur_underscorehospital.getPassword() != null) {
                if (request.getParameter("password") != null && !request.getParameter("password").equals("")) {
                    password = request.getParameter("password");
                    MessageDigest md = null;
                    try {
                        md = MessageDigest.getInstance("MD5");
                        md.update(password.getBytes("UTF-8"));
                    } catch (Exception x) {
                        notice = "Could not encrypt your password.  Please try again.";
                        request.setAttribute("notice", notice);
                        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/admin?action=edit_underscoremembers");
                        dispatcher.forward(request, response);
                        return;
                    }
                    password = (new BASE64Encoder()).encode(md.digest());
                } else {
                    password = cur_underscorehospital.getPassword();
                }
            }
            int is_underscoreadmin = 0;
            if (request.getParameter("is_underscoreadmin") != null) {
                is_underscoreadmin = 1;
            }
            Hospital new_underscorehospital = new Hospital(fname, lname, address1, address2, city, state, zip, email, password, is_underscoreadmin, name, phone, url);
            if (!new_underscorehospital.getEmail().equals(cur_underscorehospital.getEmail())) {
                if (new_underscorehospital.checkEmailIsRegistered()) {
                    notice = "That email address is already registered!";
                    request.setAttribute("notice", notice);
                    request.setAttribute("hospital", new_underscorehospital);
                    RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/admin?action=edit_underscoremembers");
                    dispatcher.forward(request, response);
                    return;
                }
            }
            if (!new_underscorehospital.updateHospital(person_underscoreid)) {
                session.setAttribute("hospital", new_underscorehospital);
                notice = "There was an error saving your information.  Please try again.";
                request.setAttribute("notice", notice);
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/admin?action=edit_underscoremembers");
                dispatcher.forward(request, response);
                return;
            }
            notice = "Information successfully updated.";
            request.setAttribute("notice", notice);
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/admin?action=edit_underscoremembers");
            dispatcher.forward(request, response);
            return;
        } else if (action.equals("update_underscorestudent")) {
            int person_underscoreid = Integer.parseInt(request.getParameter("person_underscoreid"));
            Person person = new Person();
            person = person.getPerson(person_underscoreid);
            Student cur_underscorestudent = person.getStudent();
            String fname = request.getParameter("fname");
            String lname = request.getParameter("lname");
            String address1 = request.getParameter("address1");
            String address2 = request.getParameter("address2");
            String city = request.getParameter("city");
            String state = request.getParameter("state");
            String zip = request.getParameter("zip");
            String email = request.getParameter("email");
            String start_underscoredate = request.getParameter("start_underscoreyear") + "-" + request.getParameter("start_underscoremonth") + "-01";
            String graduation_underscoredate = "";
            if (!request.getParameter("grad_underscoreyear").equals("") && !request.getParameter("grad_underscoremonth").equals("")) {
                graduation_underscoredate = request.getParameter("grad_underscoreyear") + "-" + request.getParameter("grad_underscoremonth") + "-01";
            }
            String password = "";
            if (request.getParameter("password") != null && !request.getParameter("password").equals("")) {
                password = request.getParameter("password");
                MessageDigest md = null;
                try {
                    md = MessageDigest.getInstance("MD5");
                    md.update(password.getBytes("UTF-8"));
                } catch (Exception x) {
                    notice = "Could not encrypt your password.  Please try again.";
                    request.setAttribute("notice", notice);
                    RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/admin?action=edit_underscoremembers");
                    dispatcher.forward(request, response);
                    return;
                }
                password = (new BASE64Encoder()).encode(md.digest());
            } else {
                password = cur_underscorestudent.getPassword();
            }
            int is_underscoreadmin = 0;
            if (request.getParameter("is_underscoreadmin") != null) {
                is_underscoreadmin = 1;
            }
            Student new_underscorestudent = new Student(fname, lname, address1, address2, city, state, zip, email, password, is_underscoreadmin, start_underscoredate, graduation_underscoredate);
            if (!new_underscorestudent.getEmail().equals(cur_underscorestudent.getEmail())) {
                if (new_underscorestudent.checkEmailIsRegistered()) {
                    notice = "That email address is already registered!";
                    request.setAttribute("notice", notice);
                    request.setAttribute("student", new_underscorestudent);
                    RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/admin?action=edit_underscoremembers");
                    dispatcher.forward(request, response);
                    return;
                }
            }
            if (!new_underscorestudent.updateStudent(person_underscoreid)) {
                notice = "There was an error saving your information.  Please try again.";
                request.setAttribute("notice", notice);
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/admin?action=edit_underscoremembers");
                dispatcher.forward(request, response);
                return;
            }
            notice = "Information successfully updated.";
            request.setAttribute("notice", notice);
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/admin?action=edit_underscoremembers");
            dispatcher.forward(request, response);
            return;
        } else if (action.equals("delete_underscoreperson")) {
            int id = Integer.parseInt(request.getParameter("id"));
            String member_underscoretype = request.getParameter("member_underscoretype");
            Person person = new Person();
            person = person.getPerson(id);
            if (person.deletePerson(member_underscoretype)) {
                notice = person.getFname() + ' ' + person.getLname() + " successfully deleted from database.";
                request.setAttribute("notice", notice);
                person = null;
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/admin?action=edit_underscoremembers&member_underscoretype=all");
                dispatcher.forward(request, response);
                return;
            }
        } else if (action.equals("manage_underscorepages")) {
            String sql = "SELECT * FROM pages WHERE parent_underscoreid=0 OR parent_underscoreid IN (SELECT id FROM pages WHERE title LIKE 'root')";
            if (request.getParameter("id") != null) {
                int id = Integer.parseInt(request.getParameter("id"));
                sql = "SELECT * FROM pages WHERE parent_underscoreid=" + id;
            }
            try {
                dbStatement = connection.createStatement();
                dbResultSet = dbStatement.executeQuery(sql);
                request.setAttribute("resultset", dbResultSet);
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/admin/edit_underscorepages.jsp");
                dispatcher.forward(request, response);
                return;
            } catch (SQLException e) {
                notice = "Error retrieving content pages from the database.";
                request.setAttribute("notice", notice);
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/admin/index.jsp");
                dispatcher.forward(request, response);
                return;
            }
        } else if (action.equals("add_underscorepage")) {
            String sql = "SELECT id, title FROM pages WHERE parent_underscoreid=0 OR parent_underscoreid IN (SELECT id FROM pages WHERE title LIKE 'root')";
            try {
                dbStatement = connection.createStatement();
                dbResultSet = dbStatement.executeQuery(sql);
                request.setAttribute("resultset", dbResultSet);
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/admin/add_underscorepage.jsp");
                dispatcher.forward(request, response);
                return;
            } catch (SQLException e) {
                notice = "Error retrieving content pages from the database.";
                request.setAttribute("notice", notice);
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/admin/index.jsp");
                dispatcher.forward(request, response);
                return;
            }
        } else if (action.equals("save_underscorepage")) {
            String title = request.getParameter("title");
            String content = request.getParameter("content");
            Page page = null;
            if (request.getParameter("parent_underscoreid") != null) {
                int parent_underscoreid = Integer.parseInt(request.getParameter("parent_underscoreid"));
                page = new Page(title, content, parent_underscoreid);
            } else {
                page = new Page(title, content, 0);
            }
            if (page.savePage()) {
                notice = "Content page saved!";
                request.setAttribute("notice", notice);
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/admin/index.jsp");
                dispatcher.forward(request, response);
                return;
            } else {
                notice = "There was an error saving the page.";
                request.setAttribute("page", page);
                request.setAttribute("notice", notice);
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/admin/add_underscorepage.jsp");
                dispatcher.forward(request, response);
                return;
            }
        } else if (action.equals("edit_underscorepage")) {
            String sql = "SELECT * FROM pages WHERE parent_underscoreid=0";
            int id = Integer.parseInt(request.getParameter("id"));
            Page page = new Page();
            page = page.getPage(id);
            try {
                dbStatement = connection.createStatement();
                dbResultSet = dbStatement.executeQuery(sql);
                request.setAttribute("resultset", dbResultSet);
            } catch (SQLException e) {
                notice = "Error retrieving content pages from the database.";
                request.setAttribute("notice", notice);
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/admin/index.jsp");
                dispatcher.forward(request, response);
                return;
            }
            if (page != null) {
                request.setAttribute("page", page);
                request.setAttribute("id", Integer.toString(id));
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/admin/add_underscorepage.jsp");
                dispatcher.forward(request, response);
                return;
            } else {
                notice = "Error retrieving content page from the database.";
                request.setAttribute("notice", notice);
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/admin/index.jsp");
                dispatcher.forward(request, response);
                return;
            }
        } else if (action.equals("update_underscorepage")) {
            int id = Integer.parseInt(request.getParameter("id"));
            String title = request.getParameter("title");
            String content = request.getParameter("content");
            int parent_underscoreid = 0;
            if (request.getParameter("parent_underscoreid") != null) {
                parent_underscoreid = Integer.parseInt(request.getParameter("parent_underscoreid"));
            }
            Page page = new Page(title, content, parent_underscoreid);
            if (page.updatePage(id)) {
                notice = "Content page was updated successfully.";
                request.setAttribute("notice", notice);
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/admin/index.jsp");
                dispatcher.forward(request, response);
                return;
            } else {
                notice = "Error updating the content page.";
                request.setAttribute("notice", notice);
                request.setAttribute("page", page);
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/admin/add_underscorepage.jsp");
                dispatcher.forward(request, response);
                return;
            }
        } else if (action.equals("delete_underscorepage")) {
            int id = Integer.parseInt(request.getParameter("id"));
            Page page = new Page();
            if (page.deletePage(id)) {
                notice = "Content page (and sub pages) deleted successfully.";
                request.setAttribute("notice", notice);
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/admin/index.jsp");
                dispatcher.forward(request, response);
                return;
            } else {
                notice = "Error deleting the content page(s).";
                request.setAttribute("notice", notice);
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/admin/index.jsp");
                dispatcher.forward(request, response);
                return;
            }
        } else if (action.equals("list_underscoreresidencies")) {
            Residency residency = new Residency();
            dbResultSet = residency.getResidencies();
            request.setAttribute("result", dbResultSet);
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/admin/list_underscoreresidencies.jsp");
            dispatcher.forward(request, response);
            return;
        } else if (action.equals("delete_underscoreresidency")) {
            int job_underscoreid = Integer.parseInt(request.getParameter("id"));
            Residency residency = new Residency();
            if (residency.deleteResidency(job_underscoreid)) {
                notice = "Residency has been successfully deleted.";
                request.setAttribute("notice", notice);
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/admin?action=list_underscoreresidencies");
                dispatcher.forward(request, response);
                return;
            } else {
                notice = "Error deleting the residency.";
                request.setAttribute("notice", notice);
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/index.jsp");
                dispatcher.forward(request, response);
                return;
            }
        } else if (action.equals("edit_underscoreresidency")) {
            int job_underscoreid = Integer.parseInt(request.getParameter("id"));
            Residency residency = new Residency();
            dbResultSet = residency.getResidency(job_underscoreid);
            if (dbResultSet != null) {
                try {
                    int hId = dbResultSet.getInt("hospital_underscoreid");
                    String hName = residency.getHospitalName(hId);
                    request.setAttribute("hName", hName);
                    dbResultSet.beforeFirst();
                } catch (SQLException e) {
                    error = "There was an error retreiving the residency.";
                    session.setAttribute("error", error);
                    RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/error.jsp");
                    dispatcher.forward(request, response);
                    return;
                }
                request.setAttribute("result", dbResultSet);
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/admin/edit_underscoreresidency.jsp");
                dispatcher.forward(request, response);
                return;
            } else {
                notice = "There was an error in locating the residency you selected.";
                request.setAttribute("notice", notice);
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/index.jsp");
                dispatcher.forward(request, response);
                return;
            }
        } else if (action.equals("new_underscoreresidency")) {
            Residency residency = new Residency();
            dbResultSet = residency.getHospitals();
            request.setAttribute("result", dbResultSet);
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/admin/add_underscoreresidency.jsp");
            dispatcher.forward(request, response);
            return;
        } else if (action.equals("add_underscoreresidency")) {
            Person person = (Person) session.getAttribute("person");
            if (person.isAdmin()) {
                String hName = request.getParameter("hName");
                String title = request.getParameter("title");
                String description = request.getParameter("description");
                String start_underscoremonth = request.getParameter("startDateMonth");
                String start_underscoreday = request.getParameter("startDateDay");
                String start_underscoreyear = request.getParameter("startDateYear");
                String start_underscoredate = start_underscoreyear + start_underscoremonth + start_underscoreday;
                String end_underscoremonth = request.getParameter("endDateMonth");
                String end_underscoreday = request.getParameter("endDateDay");
                String end_underscoreyear = request.getParameter("endDateYear");
                String end_underscoredate = end_underscoreyear + end_underscoremonth + end_underscoreday;
                String deadline_underscoremonth = request.getParameter("deadlineDateMonth");
                String deadline_underscoreday = request.getParameter("deadlineDateDay");
                String deadline_underscoreyear = request.getParameter("deadlineDateYear");
                String deadline_underscoredate = deadline_underscoreyear + deadline_underscoremonth + deadline_underscoreday;
                int hId = Integer.parseInt(request.getParameter("hId"));
                Residency residency = new Residency(title, start_underscoredate, end_underscoredate, deadline_underscoredate, description, hId);
                if (residency.saveResidency()) {
                    notice = "Residency has been successfully saved.";
                    request.setAttribute("notice", notice);
                    RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/admin?action=list_underscoreresidencies");
                    dispatcher.forward(request, response);
                    return;
                } else {
                    notice = "Error saving the residency.";
                    request.setAttribute("notice", notice);
                    RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/index.jsp");
                    dispatcher.forward(request, response);
                    return;
                }
            }
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/index.jsp");
            dispatcher.forward(request, response);
            return;
        } else if (action.equals("update_underscoreresidency")) {
            Person person = (Person) session.getAttribute("person");
            int job_underscoreid = Integer.parseInt(request.getParameter("job_underscoreid"));
            if (person.isAdmin()) {
                String hName = request.getParameter("hName");
                String title = request.getParameter("title");
                String description = request.getParameter("description");
                String start_underscoremonth = request.getParameter("startDateMonth");
                String start_underscoreday = request.getParameter("startDateDay");
                String start_underscoreyear = request.getParameter("startDateYear");
                String start_underscoredate = start_underscoreyear + start_underscoremonth + start_underscoreday;
                String end_underscoremonth = request.getParameter("endDateMonth");
                String end_underscoreday = request.getParameter("endDateDay");
                String end_underscoreyear = request.getParameter("endDateYear");
                String end_underscoredate = end_underscoreyear + end_underscoremonth + end_underscoreday;
                String deadline_underscoremonth = request.getParameter("deadlineDateMonth");
                String deadline_underscoreday = request.getParameter("deadlineDateDay");
                String deadline_underscoreyear = request.getParameter("deadlineDateYear");
                String deadline_underscoredate = deadline_underscoreyear + deadline_underscoremonth + deadline_underscoreday;
                int hId = Integer.parseInt(request.getParameter("hId"));
                Residency residency = new Residency(job_underscoreid, title, start_underscoredate, end_underscoredate, deadline_underscoredate, description);
                if (residency.updateResidency(job_underscoreid)) {
                    notice = "Residency has been successfully saved.";
                    request.setAttribute("notice", notice);
                    RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/admin?action=list_underscoreresidencies");
                    dispatcher.forward(request, response);
                    return;
                } else {
                    notice = "Error saving the residency.";
                    request.setAttribute("notice", notice);
                    RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/index.jsp");
                    dispatcher.forward(request, response);
                    return;
                }
            }
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/index.jsp");
            dispatcher.forward(request, response);
            return;
        } else if (action.equals("add_underscorehospital")) {
            Person person = (Person) session.getAttribute("person");
            if (person.isAdmin()) {
                String name = request.getParameter("name");
                String url = request.getParameter("url");
                String address1 = request.getParameter("address1");
                String address2 = request.getParameter("address2");
                String city = request.getParameter("city");
                String state = request.getParameter("state");
                String zip = request.getParameter("zip");
                String phone = request.getParameter("phone");
                String lname = request.getParameter("name");
                Hospital hospital = new Hospital(lname, address1, address2, city, state, zip, name, phone, url);
                if (!hospital.saveHospitalAdmin()) {
                    notice = "There was an error saving your information.  Please try again.";
                    request.setAttribute("notice", notice);
                    RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/admin/index.jsp");
                    dispatcher.forward(request, response);
                    return;
                }
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/admin?action=new_underscoreresidency");
                dispatcher.forward(request, response);
                return;
            } else {
                notice = "Unknown request.  Please try again.";
                request.setAttribute("notice", notice);
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/admin/index.jsp");
                dispatcher.forward(request, response);
                return;
            }
        } else if (action.equals("Get Admin News List")) {
            News news = new News();
            if (news.getNewsList() != null) {
                dbResultSet = news.getNewsList();
                request.setAttribute("result", dbResultSet);
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/admin/list.jsp");
                dispatcher.forward(request, response);
                return;
            } else {
                notice = "Could not get news list.";
                request.setAttribute("notice", notice);
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("admin/index.jsp");
                dispatcher.forward(request, response);
                return;
            }
        } else if (action.equals("Get News List")) {
            News news = new News();
            if (news.getNewsList() != null) {
                dbResultSet = news.getNewsList();
                request.setAttribute("result", dbResultSet);
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/admin/news_underscorelist.jsp");
                dispatcher.forward(request, response);
                return;
            } else {
                notice = "Could not get news list.";
                request.setAttribute("notice", notice);
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/gsu_underscorefhce/index.jsp");
                dispatcher.forward(request, response);
                return;
            }
        } else if (action.equals("detail")) {
            String id = request.getParameter("id");
            News news = new News();
            if (news.getNewsDetail(id) != null) {
                dbResultSet = news.getNewsDetail(id);
                request.setAttribute("result", dbResultSet);
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/admin/news_underscoredetail.jsp");
                dispatcher.forward(request, response);
                return;
            } else {
                notice = "Could not get news detail.";
                request.setAttribute("notice", notice);
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/admin/index.jsp");
                dispatcher.forward(request, response);
                return;
            }
        } else if (action.equals("delete")) {
            int id = 0;
            id = Integer.parseInt(request.getParameter("id"));
            News news = new News();
            if (news.deleteNews(id)) {
                notice = "News successfully deleted.";
                request.setAttribute("notice", notice);
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/admin?action=Get Admin News List");
                dispatcher.forward(request, response);
                return;
            } else {
                notice = "Error deleting the news.";
                request.setAttribute("notice", notice);
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/admin?action=Get Admin News List");
                dispatcher.forward(request, response);
                return;
            }
        } else if (action.equals("edit")) {
            int id = Integer.parseInt(request.getParameter("id"));
            News news = new News();
            news = news.getNews(id);
            if (news != null) {
                request.setAttribute("news", news);
                request.setAttribute("id", Integer.toString(id));
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/admin/news_underscoreupdate.jsp");
                dispatcher.forward(request, response);
                return;
            } else {
                notice = "Error retrieving news from the database.";
                request.setAttribute("notice", notice);
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/admin/index.jsp");
                dispatcher.forward(request, response);
                return;
            }
        } else if (action.equals("Update News")) {
            String title = request.getParameter("title");
            String date = (request.getParameter("year")) + (request.getParameter("month")) + (request.getParameter("day"));
            String content = request.getParameter("content");
            int id = Integer.parseInt(request.getParameter("newsid"));
            News news = new News(title, date, content);
            if (news.updateNews(id)) {
                notice = "News successfully updated.";
                request.setAttribute("notice", notice);
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/admin?action=Get Admin News List");
                dispatcher.forward(request, response);
                return;
            } else {
                notice = "Could not update news.";
                request.setAttribute("notice", notice);
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/admin?action=Get Admin News List");
                dispatcher.forward(request, response);
                return;
            }
        } else if (action.equals("Add News")) {
            String id = "";
            String title = request.getParameter("title");
            String date = request.getParameter("year") + "-" + request.getParameter("month") + "-" + request.getParameter("day");
            String content = request.getParameter("content");
            News news = new News(title, date, content);
            if (news.addNews()) {
                notice = "News successfully added.";
                request.setAttribute("notice", notice);
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/admin?action=Get Admin News List");
                dispatcher.forward(request, response);
                return;
            } else {
                notice = "Could not add news.";
                request.setAttribute("notice", notice);
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("admin/index.jsp");
                dispatcher.forward(request, response);
                return;
            }
        } else if (action.equals("manage_underscoremship")) {
            Mentor mentor = new Mentor();
            dbResultSet = mentor.getMentorships();
            if (dbResultSet != null) {
                request.setAttribute("result", dbResultSet);
            } else {
                notice = "There are no current mentorships.";
                request.setAttribute("notice", notice);
            }
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/admin/list_underscorementorships.jsp");
            dispatcher.forward(request, response);
            return;
        } else if (action.equals("delete_underscoremship")) {
            int mentorship_underscoreid = Integer.parseInt(request.getParameter("id"));
            Mentor mentor = new Mentor();
            if (mentor.delMentorship(mentorship_underscoreid)) {
                notice = "Mentorship successfully deleted.";
                request.setAttribute("notice", notice);
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/admin?action=manage_underscoremship");
                dispatcher.forward(request, response);
                return;
            } else {
                notice = "Error deleting the mentorship.";
                request.setAttribute("notice", notice);
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/admin?action=manage_underscoremship");
                dispatcher.forward(request, response);
                return;
            }
        } else if (action.equals("new_underscoremship")) {
            Mentor mentor = new Mentor();
            ResultSet alumnis = null;
            ResultSet students = null;
            alumnis = mentor.getAlumnis();
            students = mentor.getStudents();
            request.setAttribute("alumni_underscoreresult", alumnis);
            request.setAttribute("student_underscoreresult", students);
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/admin/create_underscoremship.jsp");
            dispatcher.forward(request, response);
            return;
        } else if (action.equals("create_underscoremship")) {
            int student_underscoreid = Integer.parseInt(request.getParameter("student_underscoreid"));
            int alumni_underscoreid = Integer.parseInt(request.getParameter("alumni_underscoreid"));
            Mentor mentor = new Mentor();
            if (mentor.addMentorship(student_underscoreid, alumni_underscoreid)) {
                notice = "Mentorship successfully created.";
                request.setAttribute("notice", notice);
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/admin?action=manage_underscoremship");
                dispatcher.forward(request, response);
                return;
            } else {
                notice = "There was an error creating the mentorship.";
                request.setAttribute("notice", notice);
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/admin/create_underscoremship.jsp");
                dispatcher.forward(request, response);
                return;
            }
        }
    }

