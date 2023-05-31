    private ContactModel convertJajahContactToContact(com.funambol.jajah.www.Contact jajahContact) throws JajahException {
        String temp;
        if (log.isTraceEnabled()) {
            log.trace("Converting Jajah contact to Foundation contact: Name:" + jajahContact.getName() + " Email:" + jajahContact.getEmail());
        }
        try {
            ContactModel contactModel;
            Contact contact = new Contact();
            if (jajahContact.getName() != null && jajahContact.getName().equals("") == false) {
                if (log.isDebugEnabled()) {
                    log.debug("NAME: " + jajahContact.getName());
                }
                contact.getName().getFirstName().setPropertyValue(jajahContact.getName());
            }
            if (jajahContact.getEmail() != null && jajahContact.getEmail().equals("") == false) {
                if (log.isDebugEnabled()) {
                    log.debug("EMAIL1_underscoreADDRESS: " + jajahContact.getEmail());
                }
                Email email1 = new Email();
                email1.setEmailType(SIFC.EMAIL1_underscoreADDRESS);
                email1.setPropertyValue(jajahContact.getEmail());
                contact.getPersonalDetail().addEmail(email1);
            }
            if (jajahContact.getMobile() != null && jajahContact.getMobile().equals("") == false) {
                if (log.isDebugEnabled()) {
                    log.debug("MOBILE_underscoreTELEPHONE_underscoreNUMBER: " + jajahContact.getMobile());
                }
                Phone phone = new Phone();
                phone.setPhoneType(SIFC.MOBILE_underscoreTELEPHONE_underscoreNUMBER);
                temp = jajahContact.getMobile().replace("-", "");
                if (!(temp.startsWith("+") || temp.startsWith("00"))) temp = "+".concat(temp);
                phone.setPropertyValue(temp);
                contact.getPersonalDetail().addPhone(phone);
            }
            if (jajahContact.getLandline() != null && jajahContact.getLandline().equals("") == false) {
                if (log.isDebugEnabled()) {
                    log.debug("HOME_underscoreTELEPHONE_underscoreNUMBER: " + jajahContact.getLandline());
                }
                Phone phone = new Phone();
                phone.setPhoneType(SIFC.HOME_underscoreTELEPHONE_underscoreNUMBER);
                temp = jajahContact.getLandline().replace("-", "");
                if (!(temp.startsWith("+") || temp.startsWith("00"))) temp = "+".concat(temp);
                phone.setPropertyValue(temp);
                contact.getPersonalDetail().addPhone(phone);
            }
            if (jajahContact.getOffice() != null && jajahContact.getOffice().equals("") == false) {
                if (log.isDebugEnabled()) {
                    log.debug("BUSINESS_underscoreTELEPHONE_underscoreNUMBER: " + jajahContact.getOffice());
                }
                Phone phone = new Phone();
                phone.setPhoneType(SIFC.BUSINESS_underscoreTELEPHONE_underscoreNUMBER);
                temp = jajahContact.getOffice().replace("-", "");
                if (!(temp.startsWith("+") || temp.startsWith("00"))) temp = "+".concat(temp);
                phone.setPropertyValue(temp);
                contact.getBusinessDetail().addPhone(phone);
            }
            if (log.isDebugEnabled()) {
                log.debug("CONTACT_underscoreID: " + jajahContact.getId());
            }
            contactModel = new ContactModel(String.valueOf(jajahContact.getId()), contact);
            ContactToSIFC convert = new ContactToSIFC(null, null);
            String sifObject = convert.convert(contactModel);
            MessageDigest m = MessageDigest.getInstance("MD5");
            m.update(sifObject.getBytes());
            String md5Hash = (new BigInteger(m.digest())).toString();
            contactModel.setMd5Hash(md5Hash);
            return contactModel;
        } catch (Exception e) {
            throw new JajahException("JAJAH  - convertJajahContactToContact error: " + e.getMessage());
        }
    }

