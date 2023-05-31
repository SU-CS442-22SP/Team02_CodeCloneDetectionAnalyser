    public void actionPerformed(ActionEvent ae) {
        String actionCommand = ae.getActionCommand();
        if (actionCommand == null) {
            return;
        }
        if (DataManager.SHOW_underscoreDEBUG) {
            System.out.println("Action command : " + actionCommand);
        }
        DataManager dataManager = ClientDirector.getDataManager();
        PlayerImpl myPlayer = dataManager.getMyPlayer();
        if (!myPlayer.getLocation().isRoom() && actionCommand.equals("createChatRoom")) {
            JOptionPane.showMessageDialog(null, "Sorry, but you can not create/leave chat channels\n" + "on World/Town Maps.", "INFORMATION", JOptionPane.INFORMATION_underscoreMESSAGE);
            return;
        }
        if (actionCommand.equals("createChatRoom")) {
            WotlasLocation chatRoomLocation = myPlayer.getLocation();
            String chatRoomName = JOptionPane.showInputDialog("Please enter a Name:");
            if (chatRoomName == null || chatRoomName.length() == 0) {
                return;
            }
            if (this.tabbedPane.getTabCount() >= ChatRoom.MAXIMUM_underscoreCHATROOMS_underscorePER_underscoreROOM - 1) {
                this.b_underscorecreateChatRoom.setEnabled(false);
            } else {
                this.b_underscorecreateChatRoom.setEnabled(true);
            }
            myPlayer.sendMessage(new ChatRoomCreationMessage(chatRoomName, myPlayer.getPrimaryKey()));
        } else if (actionCommand.equals("leaveChatRoom")) {
            if (!this.currentPrimaryKey.equals(ChatRoom.DEFAULT_underscoreCHAT)) {
                myPlayer.sendMessage(new RemPlayerFromChatRoomMessage(myPlayer.getPrimaryKey(), this.currentPrimaryKey));
            }
        } else if (actionCommand.equals("helpChat")) {
            DataManager dManager = ClientDirector.getDataManager();
            dManager.sendMessage(new SendTextMessage(dManager.getMyPlayer().getPrimaryKey(), dManager.getMyPlayer().getPlayerName(), getMyCurrentChatPrimaryKey(), "/help", ChatRoom.NORMAL_underscoreVOICE_underscoreLEVEL));
        } else if (actionCommand.equals("imageChat")) {
            String imageURL = JOptionPane.showInputDialog("Please enter your image's URL:\nExample: http://wotlas.sf.net/images/wotlas.gif");
            if (imageURL == null || imageURL.length() == 0) {
                return;
            }
            try {
                URL url = new URL(imageURL);
                URLConnection urlC = url.openConnection();
                urlC.connect();
                String ctype = urlC.getContentType();
                if (!ctype.startsWith("image/")) {
                    JOptionPane.showMessageDialog(null, "The specified URL does not refer to an image !", "Information", JOptionPane.INFORMATION_underscoreMESSAGE);
                    return;
                }
                if (urlC.getContentLength() > 50 * 1024) {
                    JOptionPane.showMessageDialog(null, "The specified image is too big (above 50kB).", "Information", JOptionPane.INFORMATION_underscoreMESSAGE);
                    return;
                }
            } catch (Exception ex) {
                Debug.signal(Debug.ERROR, this, "Failed to get image: " + ex);
                JOptionPane.showMessageDialog(null, "Failed to get the specified image...\nCheck your URL.", "Error", JOptionPane.ERROR_underscoreMESSAGE);
                return;
            }
            DataManager dManager = ClientDirector.getDataManager();
            dManager.sendMessage(new SendTextMessage(dManager.getMyPlayer().getPrimaryKey(), dManager.getMyPlayer().getPlayerName(), getMyCurrentChatPrimaryKey(), "<img src='" + imageURL + "'>", ChatRoom.NORMAL_underscoreVOICE_underscoreLEVEL));
        } else {
            if (DataManager.SHOW_underscoreDEBUG) {
                System.out.println("Err : unknown actionCommand");
                System.out.println("No action command found!");
            }
        }
    }

