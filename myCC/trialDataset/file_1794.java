    public void execute(PaymentInfoMagcard payinfo) {
        if (payinfo.getTotal().compareTo(BigDecimal.ZERO) > 0) {
            try {
                StringBuffer sb = new StringBuffer();
                sb.append("x_underscorelogin=");
                sb.append(URLEncoder.encode(m_underscoresCommerceID, "UTF-8"));
                sb.append("&x_underscorepassword=");
                sb.append(URLEncoder.encode(m_underscoresCommercePassword, "UTF-8"));
                sb.append("&x_underscoreversion=3.1");
                sb.append("&x_underscoretest_underscorerequest=");
                sb.append(m_underscorebTestMode);
                sb.append("&x_underscoremethod=CC");
                sb.append("&x_underscoretype=");
                sb.append(OPERATIONVALIDATE);
                sb.append("&x_underscoreamount=");
                NumberFormat formatter = new DecimalFormat("000.00");
                String amount = formatter.format(payinfo.getTotal());
                sb.append(URLEncoder.encode((String) amount, "UTF-8"));
                sb.append("&x_underscoredelim_underscoredata=TRUE");
                sb.append("&x_underscoredelim_underscorechar=|");
                sb.append("&x_underscorerelay_underscoreresponse=FALSE");
                sb.append("&x_underscoreexp_underscoredate=");
                String tmp = payinfo.getExpirationDate();
                sb.append(tmp.charAt(2));
                sb.append(tmp.charAt(3));
                sb.append(tmp.charAt(0));
                sb.append(tmp.charAt(1));
                sb.append("&x_underscorecard_underscorenum=");
                sb.append(URLEncoder.encode(payinfo.getCardNumber(), "UTF-8"));
                sb.append("&x_underscoredescription=Shop+Transaction");
                String[] cc_underscorename = payinfo.getHolderName().split(" ");
                sb.append("&x_underscorefirst_underscorename=");
                if (cc_underscorename.length > 0) {
                    sb.append(URLEncoder.encode(cc_underscorename[0], "UTF-8"));
                }
                sb.append("&x_underscorelast_underscorename=");
                if (cc_underscorename.length > 1) {
                    sb.append(URLEncoder.encode(cc_underscorename[1], "UTF-8"));
                }
                URL url = new URL(ENDPOINTADDRESS);
                URLConnection connection = url.openConnection();
                connection.setDoOutput(true);
                connection.setUseCaches(false);
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                DataOutputStream out = new DataOutputStream(connection.getOutputStream());
                out.write(sb.toString().getBytes());
                out.flush();
                out.close();
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                line = in.readLine();
                in.close();
                String[] ccRep = line.split("\\|");
                if ("1".equals(ccRep[0])) {
                    payinfo.paymentOK((String) ccRep[4]);
                } else {
                    payinfo.paymentError(AppLocal.getIntString("message.paymenterror") + "\n" + ccRep[0] + " -- " + ccRep[3]);
                }
            } catch (UnsupportedEncodingException eUE) {
                payinfo.paymentError(AppLocal.getIntString("message.paymentexceptionservice") + "\n" + eUE.getMessage());
            } catch (MalformedURLException eMURL) {
                payinfo.paymentError(AppLocal.getIntString("message.paymentexceptionservice") + "\n" + eMURL.getMessage());
            } catch (IOException e) {
                payinfo.paymentError(AppLocal.getIntString("message.paymenterror") + "\n" + e.getMessage());
            }
        } else {
            payinfo.paymentError(AppLocal.getIntString("message.paymentrefundsnotsupported"));
        }
    }

