    private void updateUser(AddEditUserForm addform, HttpServletRequest request) throws Exception {
        Session hbsession = HibernateUtil.currentSession();
        try {
            Transaction tx = hbsession.beginTransaction();
            NvUsers user = (NvUsers) hbsession.load(NvUsers.class, addform.getLogin());
            if (!addform.getPassword().equalsIgnoreCase("")) {
                MessageDigest md = (MessageDigest) MessageDigest.getInstance("MD5").clone();
                md.update(addform.getPassword().getBytes("UTF-8"));
                byte[] pd = md.digest();
                StringBuffer app = new StringBuffer();
                for (int i = 0; i < pd.length; i++) {
                    String s2 = Integer.toHexString(pd[i] & 0xFF);
                    app.append((s2.length() == 1) ? "0" + s2 : s2);
                }
                user.setPassword(app.toString());
            }
            ActionErrors errors = new ActionErrors();
            HashMap cAttrs = addform.getCustomAttrs();
            Query q1 = hbsession.createQuery("from org.nodevision.portal.hibernate.om.NvCustomAttrs as a");
            Iterator attrs = q1.iterate();
            HashMap attrInfos = new HashMap();
            while (attrs.hasNext()) {
                NvCustomAttrs element = (NvCustomAttrs) attrs.next();
                attrInfos.put(element.getAttrName(), element.getAttrType());
                NvCustomValuesId id = new NvCustomValuesId();
                id.setNvUsers(user);
                NvCustomValues value = new NvCustomValues();
                id.setNvCustomAttrs(element);
                value.setId(id);
                if (element.getAttrType().equalsIgnoreCase("String")) {
                    ByteArrayOutputStream bout = new ByteArrayOutputStream();
                    ObjectOutputStream serializer = new ObjectOutputStream(bout);
                    serializer.writeObject(cAttrs.get(element.getAttrName()).toString());
                    value.setAttrValue(Hibernate.createBlob(bout.toByteArray()));
                } else if (element.getAttrType().equalsIgnoreCase("Boolean")) {
                    Boolean valueBoolean = Boolean.FALSE;
                    if (cAttrs.get(element.getAttrName()) != null) {
                        valueBoolean = Boolean.TRUE;
                    }
                    ByteArrayOutputStream bout = new ByteArrayOutputStream();
                    ObjectOutputStream serializer = new ObjectOutputStream(bout);
                    serializer.writeObject(valueBoolean);
                    value.setAttrValue(Hibernate.createBlob(bout.toByteArray()));
                } else if (element.getAttrType().equalsIgnoreCase("Date")) {
                    Date date = new Date(0);
                    if (!cAttrs.get(element.getAttrName()).toString().equalsIgnoreCase("")) {
                        String bdate = cAttrs.get(element.getAttrName()).toString();
                        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                        date = df.parse(bdate);
                    }
                    ByteArrayOutputStream bout = new ByteArrayOutputStream();
                    ObjectOutputStream serializer = new ObjectOutputStream(bout);
                    serializer.writeObject(date);
                    value.setAttrValue(Hibernate.createBlob(bout.toByteArray()));
                }
                hbsession.saveOrUpdate(value);
                hbsession.flush();
            }
            String bdate = addform.getUser_underscorebdate();
            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
            Date parsedDate = df.parse(bdate);
            user.setTimezone(addform.getTimezone());
            user.setLocale(addform.getLocale());
            user.setBdate(new BigDecimal(parsedDate.getTime()));
            user.setGender(addform.getUser_underscoregender());
            user.setEmployer(addform.getEmployer());
            user.setDepartment(addform.getDepartment());
            user.setJobtitle(addform.getJobtitle());
            user.setNamePrefix(addform.getName_underscoreprefix());
            user.setNameGiven(addform.getName_underscoregiven());
            user.setNameFamily(addform.getName_underscorefamliy());
            user.setNameMiddle(addform.getName_underscoremiddle());
            user.setNameSuffix(addform.getName_underscoresuffix());
            user.setHomeName(addform.getHome_underscorename());
            user.setHomeStreet(addform.getHome_underscorestreet());
            user.setHomeStateprov(addform.getHome_underscorestateprov());
            user.setHomePostalcode(addform.getHome_underscorepostalcode().equalsIgnoreCase("") ? new Integer(0) : new Integer(addform.getHome_underscorepostalcode()));
            user.setHomeOrganization(addform.getHome_underscoreorganization_underscorename());
            user.setHomeCountry(addform.getHome_underscorecountry());
            user.setHomeCity(addform.getHome_underscorecity());
            user.setHomePhoneIntcode((addform.getHome_underscorephone_underscoreintcode().equalsIgnoreCase("")) ? new Integer(0) : Integer.valueOf(addform.getHome_underscorephone_underscoreintcode()));
            user.setHomePhoneLoccode((addform.getHome_underscorephone_underscoreloccode().equalsIgnoreCase("")) ? new Integer(0) : Integer.valueOf(addform.getHome_underscorephone_underscoreloccode()));
            user.setHomePhoneNumber((addform.getHome_underscorephone_underscorenumber().equalsIgnoreCase("")) ? new Integer(0) : Integer.valueOf(addform.getHome_underscorephone_underscorenumber()));
            user.setHomePhoneExt((addform.getHome_underscorephone_underscoreext().equalsIgnoreCase("")) ? new Integer(0) : Integer.valueOf(addform.getHome_underscorephone_underscoreext()));
            user.setHomePhoneComment(addform.getHome_underscorephone_underscorecommment());
            user.setHomeFaxIntcode((addform.getHome_underscorefax_underscoreintcode().equalsIgnoreCase("")) ? new Integer(0) : Integer.valueOf(addform.getHome_underscorefax_underscoreintcode()));
            user.setHomeFaxLoccode((addform.getHome_underscorefax_underscoreloccode().equalsIgnoreCase("")) ? new Integer(0) : Integer.valueOf(addform.getHome_underscorefax_underscoreloccode()));
            user.setHomeFaxNumber((addform.getHome_underscorefax_underscorenumber().equalsIgnoreCase("")) ? new Integer(0) : Integer.valueOf(addform.getHome_underscorefax_underscorenumber()));
            user.setHomeFaxExt((addform.getHome_underscorefax_underscoreext().equalsIgnoreCase("")) ? new Integer(0) : Integer.valueOf(addform.getHome_underscorefax_underscoreext()));
            user.setHomeFaxComment(addform.getHome_underscorefax_underscorecommment());
            user.setHomeMobileIntcode((addform.getHome_underscoremobile_underscoreintcode().equalsIgnoreCase("")) ? new Integer(0) : Integer.valueOf(addform.getHome_underscoremobile_underscoreintcode()));
            user.setHomeMobileLoccode((addform.getHome_underscoremobile_underscoreloccode().equalsIgnoreCase("")) ? new Integer(0) : Integer.valueOf(addform.getHome_underscoremobile_underscoreloccode()));
            user.setHomeMobileNumber((addform.getHome_underscoremobile_underscorenumber().equalsIgnoreCase("")) ? new Integer(0) : Integer.valueOf(addform.getHome_underscoremobile_underscorenumber()));
            user.setHomeMobileExt((addform.getHome_underscoremobile_underscoreext().equalsIgnoreCase("")) ? new Integer(0) : Integer.valueOf(addform.getHome_underscoremobile_underscoreext()));
            user.setHomeMobileComment(addform.getHome_underscoremobile_underscorecommment());
            user.setHomePagerIntcode((addform.getHome_underscorepager_underscoreintcode().equalsIgnoreCase("")) ? new Integer(0) : Integer.valueOf(addform.getHome_underscorepager_underscoreintcode()));
            user.setHomePagerLoccode((addform.getHome_underscorepager_underscoreloccode().equalsIgnoreCase("")) ? new Integer(0) : Integer.valueOf(addform.getHome_underscorepager_underscoreloccode()));
            user.setHomePagerNumber((addform.getHome_underscorepager_underscorenumber().equalsIgnoreCase("")) ? new Integer(0) : Integer.valueOf(addform.getHome_underscorepager_underscorenumber()));
            user.setHomePagerExt((addform.getHome_underscorepager_underscoreext().equalsIgnoreCase("")) ? new Integer(0) : Integer.valueOf(addform.getHome_underscorepager_underscoreext()));
            user.setHomePagerComment(addform.getHome_underscorepager_underscorecommment());
            user.setHomeUri(addform.getHome_underscoreuri());
            user.setHomeEmail(addform.getHome_underscoreemail());
            user.setBusinessName(addform.getBusiness_underscorename());
            user.setBusinessStreet(addform.getBusiness_underscorestreet());
            user.setBusinessStateprov(addform.getBusiness_underscorestateprov());
            user.setBusinessPostalcode((addform.getBusiness_underscorepostalcode().equalsIgnoreCase("")) ? new Integer(0) : Integer.valueOf(addform.getBusiness_underscorepostalcode()));
            user.setBusinessOrganization(addform.getBusiness_underscoreorganization_underscorename());
            user.setBusinessCountry(addform.getBusiness_underscorecountry());
            user.setBusinessCity(addform.getBusiness_underscorecity());
            user.setBusinessPhoneIntcode((addform.getBusiness_underscorephone_underscoreintcode().equalsIgnoreCase("")) ? new Integer(0) : Integer.valueOf(addform.getBusiness_underscorephone_underscoreintcode()));
            user.setBusinessPhoneLoccode((addform.getBusiness_underscorephone_underscoreloccode().equalsIgnoreCase("")) ? new Integer(0) : Integer.valueOf(addform.getBusiness_underscorephone_underscoreloccode()));
            user.setBusinessPhoneNumber((addform.getBusiness_underscorephone_underscorenumber().equalsIgnoreCase("")) ? new Integer(0) : Integer.valueOf(addform.getBusiness_underscorephone_underscorenumber()));
            user.setBusinessPhoneExt((addform.getBusiness_underscorephone_underscoreext().equalsIgnoreCase("")) ? new Integer(0) : Integer.valueOf(addform.getBusiness_underscorephone_underscoreext()));
            user.setBusinessPhoneComment(addform.getBusiness_underscorephone_underscorecommment());
            user.setBusinessFaxIntcode((addform.getBusiness_underscorefax_underscoreintcode().equalsIgnoreCase("")) ? new Integer(0) : Integer.valueOf(addform.getBusiness_underscorefax_underscoreintcode()));
            user.setBusinessFaxLoccode((addform.getBusiness_underscorefax_underscoreloccode().equalsIgnoreCase("")) ? new Integer(0) : Integer.valueOf(addform.getBusiness_underscorefax_underscoreloccode()));
            user.setBusinessFaxNumber((addform.getBusiness_underscorefax_underscorenumber().equalsIgnoreCase("")) ? new Integer(0) : Integer.valueOf(addform.getBusiness_underscorefax_underscorenumber()));
            user.setBusinessFaxExt((addform.getBusiness_underscorefax_underscoreext().equalsIgnoreCase("")) ? new Integer(0) : Integer.valueOf(addform.getBusiness_underscorefax_underscoreext()));
            user.setBusinessFaxComment(addform.getBusiness_underscorefax_underscorecommment());
            user.setBusinessMobileIntcode((addform.getBusiness_underscoremobile_underscoreintcode().equalsIgnoreCase("")) ? new Integer(0) : Integer.valueOf(addform.getBusiness_underscoremobile_underscoreintcode()));
            user.setBusinessMobileLoccode((addform.getBusiness_underscoremobile_underscoreloccode().equalsIgnoreCase("")) ? new Integer(0) : Integer.valueOf(addform.getBusiness_underscoremobile_underscoreloccode()));
            user.setBusinessMobileNumber((addform.getBusiness_underscoremobile_underscorenumber().equalsIgnoreCase("")) ? new Integer(0) : Integer.valueOf(addform.getBusiness_underscoremobile_underscorenumber()));
            user.setBusinessMobileExt((addform.getBusiness_underscoremobile_underscoreext().equalsIgnoreCase("")) ? new Integer(0) : Integer.valueOf(addform.getBusiness_underscoremobile_underscoreext()));
            user.setBusinessMobileComment(addform.getBusiness_underscoremobile_underscorecommment());
            user.setBusinessPagerIntcode((addform.getBusiness_underscorepager_underscoreintcode().equalsIgnoreCase("")) ? new Integer(0) : Integer.valueOf(addform.getBusiness_underscorepager_underscoreintcode()));
            user.setBusinessPagerLoccode((addform.getBusiness_underscorepager_underscoreloccode().equalsIgnoreCase("")) ? new Integer(0) : Integer.valueOf(addform.getBusiness_underscorepager_underscoreloccode()));
            user.setBusinessPagerNumber((addform.getBusiness_underscorepager_underscorenumber().equalsIgnoreCase("")) ? new Integer(0) : Integer.valueOf(addform.getBusiness_underscorepager_underscorenumber()));
            user.setBusinessPagerExt((addform.getBusiness_underscorepager_underscoreext().equalsIgnoreCase("")) ? new Integer(0) : Integer.valueOf(addform.getBusiness_underscorepager_underscoreext()));
            user.setBusinessPagerComment(addform.getBusiness_underscorepager_underscorecommment());
            user.setBusinessUri(addform.getBusiness_underscoreuri());
            user.setBusinessEmail(addform.getBusiness_underscoreemail());
            String hqlDelete = "delete org.nodevision.portal.hibernate.om.NvUserRoles where login = :login";
            int deletedEntities = hbsession.createQuery(hqlDelete).setString("login", user.getLogin()).executeUpdate();
            String[] selectedGroups = addform.getSelectedGroups();
            Set newGroups = new HashSet();
            for (int i = 0; i < selectedGroups.length; i++) {
                NvUserRolesId userroles = new NvUserRolesId();
                userroles.setNvUsers(user);
                userroles.setNvRoles((NvRoles) hbsession.load(NvRoles.class, selectedGroups[i]));
                NvUserRoles newRole = new NvUserRoles();
                newRole.setId(userroles);
                newGroups.add(newRole);
            }
            user.setSetOfNvUserRoles(newGroups);
            hbsession.update(user);
            hbsession.flush();
            if (!hbsession.connection().getAutoCommit()) {
                tx.commit();
            }
        } finally {
            HibernateUtil.closeSession();
        }
    }

