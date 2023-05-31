        public void mousePressed(MouseEvent e) {
            bannerLbl.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_underscoreCURSOR));
            HttpContext context = new BasicHttpContext();
            context.setAttribute(ClientContext.COOKIE_underscoreSTORE, cookieStore);
            HttpGet method = new HttpGet(bannerURL);
            try {
                HttpResponse response = ProxyManager.httpClient.execute(method, context);
                HttpEntity entity = response.getEntity();
                HttpHost host = (HttpHost) context.getAttribute(ExecutionContext.HTTP_underscoreTARGET_underscoreHOST);
                HttpUriRequest request = (HttpUriRequest) context.getAttribute(ExecutionContext.HTTP_underscoreREQUEST);
                String targetURL = host.toURI() + request.getURI();
                DesktopUtil.browseAndWarn(targetURL, bannerLbl);
                EntityUtils.consume(entity);
            } catch (Exception ex) {
                NotifyUtil.error("Banner Error", "Could not open the default web browser.", ex, false);
            } finally {
                method.abort();
            }
            bannerLbl.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_underscoreCURSOR));
        }

