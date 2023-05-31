    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        session.setMaxInactiveInterval(300);
        String member_underscoretype;
        String save_underscoretype;
        String action;
        String notice;
        if ((String) session.getAttribute("member_underscoretype") != null) {
            member_underscoretype = (String) session.getAttribute("member_underscoretype");
        } else {
            notice = "You must login first!";
            request.setAttribute("notice", notice);
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/index.jsp");
            dispatcher.forward(request, response);
            return;
        }
        if (request.getParameter("action") != null) {
            action = (String) request.getParameter("action");
        } else {
            if (member_underscoretype.equals("student")) {
                if (session.getAttribute("person") != null) {
                    Person person = (Person) session.getAttribute("person");
                    Student student = person.getStudent();
                    request.setAttribute("student", student);
                    RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/member/student.jsp");
                    dispatcher.forward(request, response);
                    return;
                } else {
                    notice = "You are not logged in!";
                    request.setAttribute("notice", notice);
                    RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/index.jsp");
                    dispatcher.forward(request, response);
                    return;
                }
            } else if (member_underscoretype.equals("alumni")) {
                if (session.getAttribute("person") != null) {
                    Person person = (Person) session.getAttribute("person");
                    Alumni alumni = person.getAlumni();
                    request.setAttribute("alumni", alumni);
                    RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/member/alumni.jsp");
                    dispatcher.forward(request, response);
                    return;
                } else {
                    notice = "You are not logged in!";
                    request.setAttribute("notice", notice);
                    RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/index.jsp");
                    dispatcher.forward(request, response);
                    return;
                }
            } else if (member_underscoretype.equals("hospital")) {
                if (session.getAttribute("person") != null) {
                    Person person = (Person) session.getAttribute("person");
                    Hospital hospital = person.getHospital();
                    request.setAttribute("hospital", hospital);
                    RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/member/hospital.jsp");
                    dispatcher.forward(request, response);
                    return;
                } else {
                    notice = "You are not logged in!";
                    request.setAttribute("notice", notice);
                    RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/index.jsp");
                    dispatcher.forward(request, response);
                    return;
                }
            } else {
                notice = "I don't understand your request.  Please try again.";
                request.setAttribute("notice", notice);
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/index.jsp");
                dispatcher.forward(request, response);
                return;
            }
        }
        if (action.equals("save_underscorestudent")) {
            Person person = (Person) session.getAttribute("person");
            Student cur_underscorestudent = person.getStudent();
            int person_underscoreid = Integer.parseInt((String) session.getAttribute("person_underscoreid"));
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
                    RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/member/student.jsp");
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
                    RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/member/student.jsp");
                    dispatcher.forward(request, response);
                    return;
                }
            }
            if (!new_underscorestudent.updateStudent(person_underscoreid)) {
                notice = "There was an error saving your information.  Please try again.";
                request.setAttribute("notice", notice);
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/member/student.jsp");
                dispatcher.forward(request, response);
                return;
            }
            Person updated_underscoreperson = new_underscorestudent.getPerson(person_underscoreid);
            session.setAttribute("person", updated_underscoreperson);
            notice = "Information successfully updated.";
            request.setAttribute("notice", notice);
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/index.jsp");
            dispatcher.forward(request, response);
            return;
        } else if (action.equals("save_underscorealumni")) {
            Person person = (Person) session.getAttribute("person");
            Alumni cur_underscorealumni = person.getAlumni();
            int person_underscoreid = Integer.parseInt((String) session.getAttribute("person_underscoreid"));
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
                    RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/member/alumni.jsp");
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
                    RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/member/alumni.jsp");
                    dispatcher.forward(request, response);
                    return;
                }
            }
            if (!new_underscorealumni.updateAlumni(person_underscoreid)) {
                session.setAttribute("alumni", new_underscorealumni);
                notice = "There was an error saving your information.  Please try again.";
                request.setAttribute("notice", notice);
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/member/alumni.jsp");
                dispatcher.forward(request, response);
                return;
            }
            Person updated_underscoreperson = new_underscorealumni.getPerson(person_underscoreid);
            session.setAttribute("person", updated_underscoreperson);
            notice = "Information successfully updated.";
            request.setAttribute("notice", notice);
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/index.jsp");
            dispatcher.forward(request, response);
            return;
        } else if (action.equals("save_underscorehospital")) {
            Person person = (Person) session.getAttribute("person");
            Hospital cur_underscorehospital = person.getHospital();
            int person_underscoreid = Integer.parseInt((String) session.getAttribute("person_underscoreid"));
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
            if (request.getParameter("password") != null && !request.getParameter("password").equals("")) {
                password = request.getParameter("password");
                MessageDigest md = null;
                try {
                    md = MessageDigest.getInstance("MD5");
                    md.update(password.getBytes("UTF-8"));
                } catch (Exception x) {
                    notice = "Could not encrypt your password.  Please try again.";
                    request.setAttribute("notice", notice);
                    RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/member/hospital.jsp");
                    dispatcher.forward(request, response);
                    return;
                }
                password = (new BASE64Encoder()).encode(md.digest());
            } else {
                password = cur_underscorehospital.getPassword();
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
                    RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/member/hospital.jsp");
                    dispatcher.forward(request, response);
                    return;
                }
            }
            if (!new_underscorehospital.updateHospital(person_underscoreid)) {
                session.setAttribute("hospital", new_underscorehospital);
                notice = "There was an error saving your information.  Please try again.";
                request.setAttribute("notice", notice);
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/member/hospital.jsp");
                dispatcher.forward(request, response);
                return;
            }
            Person updated_underscoreperson = new_underscorehospital.getPerson(person_underscoreid);
            session.setAttribute("person", updated_underscoreperson);
            notice = "Information successfully updated.";
            request.setAttribute("notice", notice);
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/index.jsp");
            dispatcher.forward(request, response);
            return;
        } else {
            notice = "There was an error with your request.  Please try again.";
            request.setAttribute("notice", notice);
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/member/hospital.jsp");
            dispatcher.forward(request, response);
            return;
        }
    }

