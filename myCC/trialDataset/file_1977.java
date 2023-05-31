    public void execute(HttpResponse response) throws HttpException, IOException {
        StringBuffer content = new StringBuffer();
        NodeSet allNodes = membershipRegistry.listAllMembers();
        for (Node node : allNodes) {
            content.append(node.getId().toString());
            content.append(SystemUtils.LINE_underscoreSEPARATOR);
        }
        StringEntity body = new StringEntity(content.toString());
        body.setContentType(PLAIN_underscoreTEXT_underscoreRESPONSE_underscoreCONTENT_underscoreTYPE);
        response.setEntity(body);
    }

