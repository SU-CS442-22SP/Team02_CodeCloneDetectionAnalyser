    private void Reserve() throws SQLException {
        Statement stbookings, stchartwl;
        String sp = "";
        if (useragent) sp = "agent"; else sp = "user";
        String userbooksql = "";
        String agentbooksql = "";
        String bookingid = String.valueOf(System.currentTimeMillis());
        String currentcoach;
        String currentseat;
        try {
            if (useragent) {
                agentbooksql = "update hp_underscoreadministrator.agent_underscorebookings set BOOKINGS = xmlquery('copy $new := $BOOKINGS modify do insert ";
                agentbooksql += " <detail booking_underscoreid=\"" + booking_underscoredetails.getTicketno() + "\" status=\"open\" train_underscoreno=\"" + booking_underscoredetails.getTrain_underscoreno() + "\" source=\"" + booking_underscoredetails.getSource() + "\" dest=\"" + booking_underscoredetails.getDestination() + "\" dep_underscoredate=\"" + booking_underscoredetails.getDate() + "\" > ";
            } else if (!useragent) {
                userbooksql = "update hp_underscoreadministrator.user_underscorebookings set BOOKINGS = xmlquery('copy $new := $BOOKINGS modify do insert ";
                userbooksql += " <detail booking_underscoreid=\"" + booking_underscoredetails.getTicketno() + "\" status=\"open\" train_underscoreno=\"" + booking_underscoredetails.getTrain_underscoreno() + "\" source=\"" + booking_underscoredetails.getSource() + "\" dest=\"" + booking_underscoredetails.getDestination() + "\" dep_underscoredate=\"" + booking_underscoredetails.getDate() + "\" > ";
            }
            for (int tickpos = 0; tickpos < booking_underscoredetails.getNoOfPersons(); tickpos++) {
                currentcoach = coach.get(tickpos);
                currentseat = seatno.get(tickpos);
                if (!currentcoach.equals("WL")) {
                    String chartavailupdsql = "update hp_underscoreadministrator.chart_underscorewl_underscoreorder set AVAILABLE_underscoreBOOKED = xmlquery('copy $new := $AVAILABLE_underscoreBOOKED   modify do insert ";
                    chartavailupdsql += "<seat number=\"" + currentseat + "\"><details user_underscoreid=\"" + booking_underscoredetails.getUserId() + "\" usertype=\"" + sp + "\" ticket_underscoreno=\"" + booking_underscoredetails.getTicketno() + "\" name=\"" + booking_underscoredetails.getNameAt(tickpos) + "\" age=\"" + booking_underscoredetails.getAgeAt(tickpos) + "\" sex=\"" + booking_underscoredetails.getSexAt(tickpos) + "\" type=\"primary\"  /></seat>";
                    chartavailupdsql += " into $new/status/class[@name=\"" + booking_underscoredetails.getTclass() + "\"]/coach[@number=\"" + currentcoach + "\"] ";
                    chartavailupdsql += " return  $new' ) where train_underscoreno like '" + booking_underscoredetails.getTrain_underscoreno() + "' and date = '" + booking_underscoredetails.getDate() + "' ";
                    System.out.println(chartavailupdsql);
                    stchartwl = conn.createStatement();
                    int updstat = stchartwl.executeUpdate(chartavailupdsql);
                    if (updstat > 0) System.out.println("chart_underscorewl  availability  updated");
                } else if (currentcoach.equals("WL")) {
                    String chartwlupdsql = "update hp_underscoreadministrator.chart_underscorewl_underscoreorder set WAITLISTING = xmlquery('copy $new := $WAITLISTING modify do insert ";
                    chartwlupdsql += "<details user_underscoreid=\"" + booking_underscoredetails.getUserId() + "\" usertype=\"" + sp + "\" ticket_underscoreno=\"" + booking_underscoredetails.getTicketno() + "\" name=\"" + booking_underscoredetails.getNameAt(tickpos) + "\" age=\"" + booking_underscoredetails.getAgeAt(tickpos) + "\" sex=\"" + booking_underscoredetails.getSexAt(tickpos) + "\" type=\"primary\" /></seat>";
                    chartwlupdsql += " into $new/status/class[@name=\"" + booking_underscoredetails.getTclass() + "\"] ";
                    chartwlupdsql += " return  $new' ) where train_underscoreno like '" + booking_underscoredetails.getTrain_underscoreno() + "' and date = '" + booking_underscoredetails.getDate() + "' ";
                    System.out.println(chartwlupdsql);
                    stchartwl = conn.createStatement();
                    int updstat = stchartwl.executeUpdate(chartwlupdsql);
                    if (updstat > 0) System.out.println("chart_underscorewl  waitlisting  updated");
                }
                if (useragent) agentbooksql += "<person><coach>" + currentcoach + "</coach><seat>" + currentseat + "</seat></person>"; else userbooksql += "<person><coach>" + currentcoach + "</coach><seat>" + currentseat + "</seat></person>";
            }
            if (useragent) {
                agentbooksql += "</detail>   as first into $new/book return  $new' ) where agent_underscoreid like '" + booking_underscoredetails.getUserId() + "'";
                System.out.println(agentbooksql);
                stbookings = conn.createStatement();
                int updstat = stbookings.executeUpdate(agentbooksql);
                if (updstat > 0) System.out.println("agent bookings updated");
            } else {
                userbooksql += "</detail>   as first into $new/book return  $new' ) where user_underscoreid like '" + booking_underscoredetails.getUserId() + "'";
                System.out.println(userbooksql);
                stbookings = conn.createStatement();
                int updstat = stbookings.executeUpdate(userbooksql);
                if (updstat > 0) System.out.println("user bookings  updated");
            }
        } catch (SQLException e) {
            conn.rollback();
            e.printStackTrace();
        }
    }

