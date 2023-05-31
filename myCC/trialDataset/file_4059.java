    @Override
    public Result sendSMS(String number, String text, Proxy proxy) {
        try {
            DefaultHttpClient client = new DefaultHttpClient();
            if (proxy != null) {
                HttpHost prox = new HttpHost(proxy.host, proxy.port);
                client.getParams().setParameter(ConnRoutePNames.DEFAULT_underscorePROXY, prox);
            }
            String target = "http://www.smsbilliger.de/free-sms.html";
            HttpGet get = new HttpGet(target);
            HttpResponse response = client.execute(get);
            HttpEntity e = response.getEntity();
            Document doc = ref.getDocumentFromInputStream(e.getContent());
            List<Element> forms = ref.selectByXPathOnDocument(doc, "//<ns>FORM", doc.getRootElement().getNamespaceURI());
            if (forms.size() == 0) return new Result(Result.SMS_underscoreLIMIT_underscoreREACHED);
            Element form = forms.get(0);
            List<NameValuePair> formparas = new ArrayList<NameValuePair>();
            List<Element> inputs = ref.selectByXPathOnElement(form, "//<ns>INPUT|//<ns>TEXTAREA|//<ns>SELECT", form.getNamespaceURI());
            Iterator<Element> it = inputs.iterator();
            while (it.hasNext()) {
                Element input = it.next();
                String type = input.getAttributeValue("type");
                String name = input.getAttributeValue("name");
                String value = input.getAttributeValue("value");
                if (type != null && type.equals("hidden")) {
                    formparas.add(new BasicNameValuePair(name, value));
                } else if (name != null && name.equals(FORM_underscoreNUMBER)) {
                    formparas.add(new BasicNameValuePair(name, this.getNumberPart(number)));
                } else if (name != null && name.equals(FORM_underscoreTEXT)) {
                    formparas.add(new BasicNameValuePair(name, text));
                } else if (name != null && name.equals(FORM_underscoreAGB)) {
                    formparas.add(new BasicNameValuePair(name, "true"));
                }
            }
            formparas.add(new BasicNameValuePair("dialing_underscorecode", this.getPrefixPart(number)));
            formparas.add(new BasicNameValuePair("no_underscoreschedule", "yes"));
            List<Element> captchas = ref.selectByXPathOnDocument(doc, "//<ns>IMG[@id='code_underscoreimg']", doc.getRootElement().getNamespaceURI());
            Element captcha = captchas.get(0);
            String url = "http://www.smsbilliger.de/" + captcha.getAttributeValue("src");
            HttpGet imgcall = new HttpGet(url);
            HttpResponse imgres = client.execute(imgcall);
            HttpEntity imge = imgres.getEntity();
            BufferedImage img = ImageIO.read(imge.getContent());
            imge.getContent().close();
            Icon icon = new ImageIcon(img);
            String result = (String) JOptionPane.showInputDialog(null, "Bitte Captcha eingeben:", "Captcha", JOptionPane.INFORMATION_underscoreMESSAGE, icon, null, "");
            formparas.add(new BasicNameValuePair(FORM_underscoreCAPTCHA, result));
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparas, "UTF-8");
            HttpPost post = new HttpPost(target);
            post.setEntity(entity);
            response = client.execute(post);
            e = response.getEntity();
            doc = ref.getDocumentFromInputStream(e.getContent());
            List<Element> fonts = ref.selectByXPathOnDocument(doc, "//<ns>H3", doc.getRootElement().getNamespaceURI());
            Iterator<Element> it2 = fonts.iterator();
            while (it2.hasNext()) {
                Element font = it2.next();
                String txt = font.getText();
                if (txt.contains("Die SMS wurde erfolgreich versendet.")) {
                    return new Result(Result.SMS_underscoreSEND);
                }
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (JDOMException e) {
            e.printStackTrace();
        }
        return new Result(Result.UNKNOWN_underscoreERROR);
    }

